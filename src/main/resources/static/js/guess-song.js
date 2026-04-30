let timerValue = 100;
let timerInterval = null;
let isPlaying = false;
let currentScore = 0;

let sessionCode = null;
let currentRound = 1;
let totalRounds = 5;
let audio = null;

async function startGame() {
    try {
        const response = await fetch("/api/deezer/start", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                playerName: "Player",
                totalRounds: totalRounds
            })
        });

        const data = await response.json();

        sessionCode = data.sessionCode;
        currentRound = data.currentRound;
        totalRounds = data.totalRounds;

        setupRound(data);

    } catch (error) {
        console.error("Error starting game:", error);
        alert("Could not start game. Check the server logs.");
    }
}

function setupRound(data) {
    document.getElementById("playButton").style.display = "none";
    document.getElementById("guessSection").style.display = "block";

    document.getElementById("statusMessage").textContent =
        "Round " + data.currentRound + " of " + data.totalRounds + " — Artist: " + data.artistName;

    const feedback = document.getElementById("feedback");
    feedback.style.display = "none";
    feedback.className = "feedback";

    timerValue = 100;

    const timerElement = document.getElementById("timer");
    timerElement.textContent = timerValue;
    timerElement.style.color = "#667eea";

    document.getElementById("guessInput").value = "";
    document.getElementById("guessInput").focus();

    if (audio) {
        audio.pause();
        audio = null;
    }

    if (data.previewUrl && data.previewUrl.length > 0) {
        audio = new Audio(data.previewUrl);
        audio.play().catch(error => {
            console.error("Audio play error:", error);
        });
    }

    isPlaying = true;
    clearInterval(timerInterval);
    timerInterval = setInterval(updateTimer, 300);
}

function updateTimer() {
    if (timerValue > 0 && isPlaying) {
        timerValue--;

        const timerElement = document.getElementById("timer");
        timerElement.textContent = timerValue;

        if (timerValue < 30) {
            timerElement.style.color = "#dc3545";
        } else if (timerValue < 60) {
            timerElement.style.color = "#ffc107";
        } else {
            timerElement.style.color = "#667eea";
        }
    } else if (timerValue === 0) {
        endRound(false, "Time's up!");
    }
}

async function submitGuess() {
    if (!isPlaying) return;

    const guess = document.getElementById("guessInput").value.trim();

    if (guess === "") {
        alert("Please enter a song name!");
        return;
    }

    try {
        const response = await fetch("/api/deezer/guess", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                sessionCode: sessionCode,
                guess: guess,
                points: timerValue
            })
        });

        const result = await response.json();

        if (result.correct) {
            currentScore += result.pointsEarned;
            document.getElementById("yourScore").textContent = currentScore + " pts";
            endRound(true, "Correct! You earned " + result.pointsEarned + " points!");
        } else {
            const feedback = document.getElementById("feedback");
            feedback.textContent = "Incorrect! Try again!";
            feedback.className = "feedback incorrect";

            document.getElementById("guessInput").value = "";
            document.getElementById("guessInput").focus();
        }

    } catch (error) {
        console.error("Error submitting guess:", error);
        alert("Could not submit guess.");
    }
}

function endRound(won, message) {
    isPlaying = false;
    clearInterval(timerInterval);

    if (audio) {
        audio.pause();
        audio = null;
    }

    const feedback = document.getElementById("feedback");

    if (won) {
        feedback.textContent = message;
        feedback.className = "feedback correct";
    } else {
        feedback.textContent = message;
        feedback.className = "feedback incorrect";
    }

    document.getElementById("guessSection").style.display = "none";

    const playButton = document.getElementById("playButton");
    playButton.style.display = "block";
    playButton.textContent = "Next Round";
    playButton.onclick = nextRound;
}

async function nextRound() {
    try {
        const response = await fetch("/api/deezer/next-round", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                sessionCode: sessionCode
            })
        });

        const data = await response.json();

        if (data.gameComplete) {
            document.getElementById("statusMessage").textContent =
                "Game complete! Final score: " + currentScore + " pts";

            const playButton = document.getElementById("playButton");
            playButton.textContent = "Play Again";
            playButton.onclick = startGame;

            return;
        }

        setupRound(data);

    } catch (error) {
        console.error("Error loading next round:", error);
        alert("Could not load next round.");
    }
}
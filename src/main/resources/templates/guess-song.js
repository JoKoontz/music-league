let timerValue = 100;
let timerInterval = null;
let isPlaying = false;
let currentScore = 0;

// Replace this later with backend data
const correctAnswer = "boom";

function startGame() {
    document.getElementById("playButton").style.display = "none";
    document.getElementById("guessSection").style.display = "block";
    document.getElementById("statusMessage").textContent = "Listen and guess the song!";

    const feedback = document.getElementById("feedback");
    feedback.style.display = "none";
    feedback.className = "feedback";

    timerValue = 100;
    const timerElement = document.getElementById("timer");
    timerElement.textContent = timerValue;
    timerElement.style.color = "#667eea";

    isPlaying = true;
    timerInterval = setInterval(updateTimer, 300);

    document.getElementById("guessInput").value = "";
    document.getElementById("guessInput").focus();

    console.log("Song is now playing...");
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
        endGame(false);
    }
}

function submitGuess() {
    if (!isPlaying) return;

    const guess = document.getElementById("guessInput").value.trim();

    if (guess === "") {
        alert("Please enter a song name!");
        return;
    }

    const isCorrect = guess.toLowerCase() === correctAnswer.toLowerCase();

    if (isCorrect) {
        endGame(true);
    } else {
        const feedback = document.getElementById("feedback");
        feedback.textContent = "Incorrect! Try again!";
        feedback.className = "feedback incorrect";

        document.getElementById("guessInput").value = "";
        document.getElementById("guessInput").focus();
    }
}

function endGame(won) {
    isPlaying = false;
    clearInterval(timerInterval);

    const feedback = document.getElementById("feedback");

    if (won) {
        const pointsEarned = timerValue;
        currentScore += pointsEarned;

        feedback.textContent = `Correct! You earned ${pointsEarned} points!`;
        feedback.className = "feedback correct";

        document.getElementById("yourScore").textContent = currentScore + " pts";
        document.getElementById("statusMessage").textContent = "Great job! The song was: " + correctAnswer;
    } else {
        feedback.textContent = "Time's up! The song was: " + correctAnswer;
        feedback.className = "feedback incorrect";

        document.getElementById("statusMessage").textContent = "Better luck next time!";
    }

    document.getElementById("guessSection").style.display = "none";

    const playButton = document.getElementById("playButton");
    playButton.style.display = "block";
    playButton.textContent = "Play Next Round";
}
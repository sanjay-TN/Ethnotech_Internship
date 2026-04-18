let count = 0;
const counter = document.getElementById("counter");
const increaseBtn = document.getElementById("increase");
const decreaseBtn = document.getElementById("decrease");
const resetBtn = document.getElementById("reset");

function updateCounter() {
  counter.textContent = count;
  if (count > 0) {
    counter.className = "counter positive";
  } else if (count < 0) {
    counter.className = "counter negative";
  } else {
    counter.className = "counter zero";
  }
}

increaseBtn.addEventListener("click", () => {
  count++;
  updateCounter();
});

decreaseBtn.addEventListener("click", () => {
  count--;
  updateCounter();
});

resetBtn.addEventListener("click", () => {
  count = 0;
  updateCounter();
});

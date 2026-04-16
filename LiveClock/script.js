function updateClock() {
  const now = new Date();

  let hours = now.getHours();
  const minutes = String(now.getMinutes()).padStart(2, "0");
  const seconds = String(now.getSeconds()).padStart(2, "0");

  // Determine AM or PM
  const ampm = hours >= 12 ? "PM" : "AM";

  // Convert to 12-hour format
  hours = hours % 12;
  hours = hours ? hours : 12; // The hour '0' should be '12'
  const hoursStr = String(hours).padStart(2, "0");

  // Combine into a string
  const timeString = `${hoursStr}:${minutes}:${seconds} ${ampm}`;

  // Update the DOM
  document.getElementById("clock").textContent = timeString;
}

// --- The Heartbeat ---
// Run the function every 1000ms (1 second)
setInterval(updateClock, 1000);

// Call it once immediately so the clock doesn't start at 00:00:00
updateClock();

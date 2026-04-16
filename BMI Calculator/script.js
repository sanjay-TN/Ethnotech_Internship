window.onload = function () {
  document.getElementById("submit").addEventListener("click", function () {
    let height = parseFloat(document.getElementById("height").value);
    let weight = parseFloat(document.getElementById("weight").value);

    if (isNaN(height) || isNaN(weight) || height <= 0 || weight <= 0) {
      alert("Please enter valid positive values for height and weight.");
      return;
    }

    let heightinmeters = height / 100;

    let bmi = weight / (heightinmeters * heightinmeters);

    bmi = bmi.toFixed(2);

    let category = "";
    if (bmi < 18.5) {
      category = "Underweight";
    } else if (bmi >= 18.5 && bmi < 24.9) {
      category = "Normal weight";
    } else if (bmi >= 25 && bmi < 29.9) {
      category = "Overweight";
    } else {
      category = "Obesity";
    }

    alert("Your BMI is: " + bmi + "\nCategory: " + category);
  });
};

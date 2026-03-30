// Switch forms
function showLogin() {
  document.getElementById("loginForm").classList.remove("d-none");
  document.getElementById("registerForm").classList.add("d-none");
}

function showRegister() {
  document.getElementById("loginForm").classList.add("d-none");
  document.getElementById("registerForm").classList.remove("d-none");
}

// 🔹 Register
async function register() {
  const data = {
    username: document.getElementById("username").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
  };

  if (!data.username || !data.email || !data.password) {
    alert("Please fill all fields");
    return;
  }

  const res = await registerUser(data);

  alert(res.message || "Registered!");
  showLogin();
}

// 🔹 Login
async function login() {
  const data = {
    email: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value,
  };

  if (!data.email || !data.password) {
    alert("Enter email & password");
    return;
  }

  const res = await loginUser(data);

  if (res.token) {
    localStorage.setItem("token", res.token);
    localStorage.setItem("email", data.email);

    // 🔥 IMPORTANT FIX
    localStorage.setItem("userId", res.userId);

    window.location.href = "chat.html";
  } else {
    alert(res.message || "Login failed");
  }
}

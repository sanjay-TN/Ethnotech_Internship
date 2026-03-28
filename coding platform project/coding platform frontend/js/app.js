const BASE_URL = "http://localhost:8080/api";

// ================= GLOBAL AUTH =================
const path = window.location.pathname;
const role = localStorage.getItem("role");
const token = localStorage.getItem("token");

// Not logged in
if (!token && !path.includes("index.html") && !path.includes("register.html")) {
  alert("Please login first");
  window.location.href = "index.html";
}

// Admin protection
if (path.includes("admin.html") && role !== "ADMIN") {
  alert("Admin only");
  window.location.href = "dashboard.html";
}

// Prevent admin accessing user dashboard
if (path.includes("dashboard.html") && role === "ADMIN") {
  window.location.href = "admin.html";
}

// ================= REGISTER =================
const registerForm = document.getElementById("registerForm");

if (registerForm) {
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
      name: document.getElementById("name").value,
      email: document.getElementById("email").value,
      password: document.getElementById("password").value,
      role: document.getElementById("role").value,
    };

    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      alert("Registered!");
      window.location.href = "index.html";
    } else {
      alert("Registration failed");
    }
  });
}

// ================= LOGIN =================
const loginForm = document.getElementById("loginForm");

if (loginForm) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
      email: document.getElementById("email").value,
      password: document.getElementById("password").value,
    };

    const res = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      const result = await res.json();

      localStorage.setItem("token", result.token);
      localStorage.setItem("role", result.role);

      if (result.role === "ADMIN") {
        window.location.href = "admin.html";
      } else {
        window.location.href = "dashboard.html";
      }
    } else {
      alert("Login failed");
    }
  });
}

// ================= DASHBOARD =================
const problemList = document.getElementById("problemList");

if (problemList) fetchProblems();

async function fetchProblems() {
  const res = await fetch(`${BASE_URL}/problems`, {
    headers: { Authorization: "Bearer " + token },
  });

  const problems = await res.json();

  problemList.innerHTML = "";

  problems.forEach((p) => {
    problemList.innerHTML += `
      <div class="col-md-4">
        <div class="card p-3 shadow mb-3">
          <h5>${p.title}</h5>
          <p>${p.description}</p>
          <span class="badge bg-info">${p.difficulty}</span>
          <br><br>
          <button class="btn btn-primary" onclick="openProblem(${p.id})">
            Solve
          </button>
        </div>
      </div>`;
  });
}

// ================= COMMON =================
function logout() {
  localStorage.clear();
  window.location.href = "index.html";
}

function openProblem(id) {
  localStorage.setItem("problemId", id);
  window.location.href = "problem.html";
}

// ================= PROBLEM PAGE =================
if (path.includes("problem.html")) loadProblem();

async function loadProblem() {
  const problemId = localStorage.getItem("problemId");

  const res = await fetch(`${BASE_URL}/problems/${problemId}`, {
    headers: { Authorization: "Bearer " + token },
  });

  const data = await res.json();

  document.getElementById("title").innerText = data.title;
  document.getElementById("description").innerText = data.description;
  document.getElementById("difficulty").innerText = data.difficulty;
}

// ================= SUBMIT =================
async function submitCode() {
  const code = document.getElementById("code").value;
  const problemId = localStorage.getItem("problemId");
  const language = document.getElementById("language").value;

  if (!code) {
    alert("Write code first");
    return;
  }

  const res = await fetch(`${BASE_URL}/submissions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify({
      problemId,
      code,
      language,
    }),
  });

  const result = await res.json();

  document.getElementById("result").innerHTML = `
    <div class="alert ${
      result.status === "PASS" ? "alert-success" : "alert-danger"
    }">
      ${result.status} | Score: ${result.score}
    </div>`;
}

// ================= LEADERBOARD =================
if (document.getElementById("leaderboardTable")) loadLeaderboard();

async function loadLeaderboard() {
  const res = await fetch(`${BASE_URL}/leaderboard`, {
    headers: { Authorization: "Bearer " + token },
  });

  const data = await res.json();

  leaderboardTable.innerHTML = "";

  data.forEach((user, i) => {
    leaderboardTable.innerHTML += `
      <tr>
        <td>${i + 1}</td>
        <td>${user.email}</td>
        <td>${user.totalScore}</td>
      </tr>`;
  });
}

// ================= ADMIN =================
async function addProblem() {
  const data = {
    title: document.getElementById("adminTitle").value,
    description: document.getElementById("adminDescription").value,
    difficulty: document.getElementById("adminDifficulty").value,
  };

  const res = await fetch(`${BASE_URL}/admin/problem`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(data),
  });

  alert(res.ok ? "Problem added!" : "Admin only");
}

async function addTestCase() {
  const data = {
    input: document.getElementById("input").value,
    expectedOutput: document.getElementById("output").value,
    problem: {
      id: document.getElementById("problemId").value,
    },
  };

  const res = await fetch(`${BASE_URL}/admin/testcase`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    body: JSON.stringify(data),
  });

  alert(res.ok ? "Test case added!" : "Admin only");
}

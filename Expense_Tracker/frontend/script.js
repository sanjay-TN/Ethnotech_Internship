const API_BASE = "http://localhost:8080";
const CATEGORIES = ["Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Education", "Other"];

let charts = {};
let expensesCache = [];

document.addEventListener("DOMContentLoaded", () => {
    applyStoredTheme();
    bindThemeToggle();
    bindLogout();

    const page = document.body.dataset.page;
    if (page === "login") initLogin();
    if (page === "register") initRegister();
    if (page === "dashboard") initDashboard();
    if (page === "expenses") initExpenses();
    if (page === "reports") initReports();
});

async function request(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        const message = data?.error || data?.message || "Request failed";
        if (response.status === 401 && !["login", "register"].includes(document.body.dataset.page)) {
            window.location.href = "login.html";
        }
        throw new Error(message);
    }

    return data;
}

function initLogin() {
    document.getElementById("loginForm").addEventListener("submit", async (event) => {
        event.preventDefault();
        if (!validateForm(event.currentTarget)) return;

        try {
            await request("/login", {
                method: "POST",
                body: JSON.stringify({
                    email: value("email"),
                    password: value("password")
                })
            });
            toast("Login successful", "success");
            setTimeout(() => window.location.href = "dashboard.html", 350);
        } catch (error) {
            toast(error.message, "error");
        }
    });
}

function initRegister() {
    document.getElementById("registerForm").addEventListener("submit", async (event) => {
        event.preventDefault();
        if (!validateForm(event.currentTarget)) return;

        try {
            await request("/register", {
                method: "POST",
                body: JSON.stringify({
                    fullName: value("fullName"),
                    email: value("email"),
                    monthlyIncome: numberValue("monthlyIncome"),
                    password: value("password")
                })
            });
            toast("Account created", "success");
            setTimeout(() => window.location.href = "dashboard.html", 350);
        } catch (error) {
            toast(error.message, "error");
        }
    });
}

async function initDashboard() {
    await ensureAuthenticated();
    document.getElementById("refreshAiBtn").addEventListener("click", loadAiInsights);
    await withLoading(async () => {
        await Promise.all([loadDashboard(), loadAiInsights()]);
    });
}

async function loadDashboard() {
    try {
        const user = await request("/me");
        document.getElementById("welcomeText").textContent = `Welcome back, ${user.fullName}`;
        const dashboard = await request("/dashboard");

        text("totalIncome", money(dashboard.totalIncome));
        text("totalExpenses", money(dashboard.totalExpenses));
        text("remainingBalance", money(dashboard.remainingBalance));
        text("monthExpenses", money(dashboard.currentMonthExpenses));

        renderCategoryChart(dashboard.categorySpending || []);
        renderMonthlyChart(dashboard.monthlySummary || {});
        renderRecentExpenses(dashboard.recentExpenses || []);
    } catch (error) {
        toast(error.message, "error");
    }
}

async function loadAiInsights() {
    try {
        const ai = await request("/ai/analyze");
        const messages = [
            `Predicted monthly expense: ${money(ai.predictedMonthlyExpense)}`,
            ...(ai.smartAlerts || []),
            ...(ai.savingRecommendations || []),
            ...(ai.behaviorAnalysis || []),
            ...(ai.unusualExpenses || [])
        ];
        const container = document.getElementById("aiInsights");
        container.innerHTML = messages.map(message => `<div class="insight-item">${escapeHtml(message)}</div>`).join("");
    } catch (error) {
        toast(error.message, "error");
    }
}

async function initExpenses() {
    await ensureAuthenticated();
    setDefaultDate();
    populateCategorySelects();

    document.getElementById("expenseForm").addEventListener("submit", submitExpense);
    document.getElementById("filterForm").addEventListener("submit", (event) => {
        event.preventDefault();
        loadExpenses();
    });
    document.getElementById("resetExpenseForm").addEventListener("click", resetExpenseForm);

    await loadExpenses();
}

async function submitExpense(event) {
    event.preventDefault();
    if (!validateForm(event.currentTarget)) return;

    const id = value("expenseId");
    const payload = {
        title: value("title"),
        amount: numberValue("amount"),
        transactionType: value("transactionType"),
        category: value("category"),
        expenseDate: value("expenseDate"),
        note: value("note")
    };

    try {
        await request(id ? `/expenses/${id}` : "/expenses", {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(payload)
        });
        toast(id ? "Transaction updated" : "Transaction added", "success");
        resetExpenseForm();
        await loadExpenses();
    } catch (error) {
        toast(error.message, "error");
    }
}

async function loadExpenses() {
    try {
        const params = new URLSearchParams();
        [
            ["query", value("query")],
            ["category", value("filterCategory")],
            ["type", value("filterType")],
            ["fromDate", value("fromDate")],
            ["toDate", value("toDate")],
            ["minAmount", value("minAmount")],
            ["maxAmount", value("maxAmount")]
        ].forEach(([key, val]) => {
            if (val) params.append(key, val);
        });

        expensesCache = await request(`/expenses${params.toString() ? `?${params}` : ""}`);
        renderExpenseTable(expensesCache);
    } catch (error) {
        toast(error.message, "error");
    }
}

function renderExpenseTable(expenses) {
    text("expenseCount", `${expenses.length} item${expenses.length === 1 ? "" : "s"}`);
    const table = document.getElementById("expenseTable");

    if (!expenses.length) {
        table.innerHTML = `<tr><td colspan="6"><div class="empty-state">No transactions found.</div></td></tr>`;
        return;
    }

    table.innerHTML = expenses.map(expense => `
        <tr>
            <td><span class="table-title">${escapeHtml(expense.title)}</span></td>
            <td>${escapeHtml(expense.category)}</td>
            <td>${expense.transactionType}</td>
            <td>${formatDate(expense.expenseDate)}</td>
            <td class="${expense.transactionType === "INCOME" ? "amount-income" : "amount-expense"}">${money(expense.amount)}</td>
            <td>
                <div class="action-row">
                    <button type="button" onclick="editExpense(${expense.id})">Edit</button>
                    <button type="button" onclick="deleteExpense(${expense.id})">Delete</button>
                </div>
            </td>
        </tr>
    `).join("");
}

function editExpense(id) {
    const expense = expensesCache.find(item => item.id === id);
    if (!expense) return;

    setValue("expenseId", expense.id);
    setValue("title", expense.title);
    setValue("amount", expense.amount);
    setValue("transactionType", expense.transactionType);
    setValue("category", expense.category);
    setValue("expenseDate", expense.expenseDate);
    setValue("note", expense.note || "");
    text("expenseFormTitle", "Edit Transaction");
    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function deleteExpense(id) {
    if (!confirm("Delete this transaction?")) return;
    try {
        await request(`/expenses/${id}`, { method: "DELETE" });
        toast("Transaction deleted", "success");
        await loadExpenses();
    } catch (error) {
        toast(error.message, "error");
    }
}

async function initReports() {
    await ensureAuthenticated();
    document.querySelectorAll("[data-report]").forEach(button => {
        button.addEventListener("click", async () => {
            document.querySelectorAll("[data-report]").forEach(tab => tab.classList.remove("active"));
            button.classList.add("active");
            await loadReport(button.dataset.report);
        });
    });
    await loadReport("monthly");
}

async function loadReport(type) {
    try {
        const report = await request(`/reports/${type}`);
        text("trendTitle", `${report.period} Trend`);
        text("reportIncome", money(report.income));
        text("reportExpenses", money(report.expenses));
        text("reportBalance", money(report.balance));
        renderReportLine(report.trend || {});
        renderReportBar(report.categoryBreakdown || []);
    } catch (error) {
        toast(error.message, "error");
    }
}

function renderCategoryChart(categorySpending) {
    const labels = categorySpending.map(item => item.category);
    const values = categorySpending.map(item => Number(item.total));
    buildChart("categoryChart", "doughnut", labels, values, {
        backgroundColor: palette(),
        borderWidth: 0
    });
}

function renderMonthlyChart(summary) {
    buildChart("monthlyChart", "bar", Object.keys(summary), Object.values(summary).map(Number), {
        backgroundColor: "rgba(20, 184, 166, 0.72)",
        borderColor: "#14b8a6",
        borderWidth: 1
    });
}

function renderReportLine(trend) {
    buildChart("reportLineChart", "line", Object.keys(trend), Object.values(trend).map(Number), {
        borderColor: "#14b8a6",
        backgroundColor: "rgba(20, 184, 166, 0.18)",
        fill: true,
        tension: 0.38
    });
}

function renderReportBar(categoryBreakdown) {
    buildChart("reportBarChart", "bar", categoryBreakdown.map(item => item.category), categoryBreakdown.map(item => Number(item.total)), {
        backgroundColor: palette(),
        borderWidth: 0
    });
}

function buildChart(canvasId, type, labels, values, datasetOptions) {
    if (!window.Chart) return;
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;
    if (charts[canvasId]) charts[canvasId].destroy();

    charts[canvasId] = new Chart(canvas, {
        type,
        data: {
            labels: labels.length ? labels : ["No data"],
            datasets: [{
                label: "Amount",
                data: values.length ? values : [0],
                ...datasetOptions
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    labels: { color: getComputedStyle(document.body).getPropertyValue("--text") }
                }
            },
            scales: type === "doughnut" ? {} : {
                x: { ticks: { color: getComputedStyle(document.body).getPropertyValue("--muted") }, grid: { color: "rgba(148, 163, 184, 0.14)" } },
                y: { ticks: { color: getComputedStyle(document.body).getPropertyValue("--muted") }, grid: { color: "rgba(148, 163, 184, 0.14)" } }
            }
        }
    });
}

function renderRecentExpenses(expenses) {
    const container = document.getElementById("recentExpenses");
    if (!expenses.length) {
        container.innerHTML = `<div class="empty-state">No recent transactions yet.</div>`;
        return;
    }

    container.innerHTML = expenses.map(expense => `
        <div class="transaction-item">
            <div>
                <strong>${escapeHtml(expense.title)}</strong>
                <p>${escapeHtml(expense.category)} • ${formatDate(expense.expenseDate)}</p>
            </div>
            <strong class="${expense.transactionType === "INCOME" ? "amount-income" : "amount-expense"}">${money(expense.amount)}</strong>
        </div>
    `).join("");
}

function populateCategorySelects() {
    const options = CATEGORIES.map(category => `<option value="${category}">${category}</option>`).join("");
    document.getElementById("category").innerHTML = options;
    document.getElementById("filterCategory").innerHTML = `<option value="">All categories</option>${options}`;
}

async function ensureAuthenticated() {
    try {
        await request("/me");
    } catch {
        window.location.href = "login.html";
    }
}

function bindLogout() {
    const button = document.getElementById("logoutBtn");
    if (!button) return;
    button.addEventListener("click", async () => {
        try {
            await request("/logout", { method: "POST" });
        } finally {
            window.location.href = "login.html";
        }
    });
}

function bindThemeToggle() {
    const button = document.getElementById("themeToggle");
    if (!button) return;
    button.textContent = document.body.classList.contains("light-mode") ? "Dark" : "Light";
    button.addEventListener("click", () => {
        document.body.classList.toggle("light-mode");
        const light = document.body.classList.contains("light-mode");
        localStorage.setItem("theme", light ? "light" : "dark");
        button.textContent = light ? "Dark" : "Light";
        Object.values(charts).forEach(chart => chart.update());
    });
}

function applyStoredTheme() {
    if (localStorage.getItem("theme") === "light") {
        document.body.classList.add("light-mode");
    }
}

function validateForm(form) {
    if (form.checkValidity()) return true;
    const firstInvalid = form.querySelector(":invalid");
    toast(firstInvalid?.validationMessage || "Please check the form.", "error");
    firstInvalid?.focus();
    return false;
}

async function withLoading(task) {
    const loader = document.getElementById("loading");
    loader?.classList.add("active");
    try {
        await task();
    } finally {
        loader?.classList.remove("active");
    }
}

function resetExpenseForm() {
    document.getElementById("expenseForm").reset();
    setValue("expenseId", "");
    setDefaultDate();
    text("expenseFormTitle", "Add Transaction");
}

function setDefaultDate() {
    const field = document.getElementById("expenseDate");
    if (field) field.valueAsDate = new Date();
}

function value(id) {
    return document.getElementById(id)?.value.trim() || "";
}

function numberValue(id) {
    return Number(value(id) || 0);
}

function setValue(id, val) {
    const element = document.getElementById(id);
    if (element) element.value = val;
}

function text(id, val) {
    const element = document.getElementById(id);
    if (element) element.textContent = val;
}

function money(value) {
    return Number(value || 0).toLocaleString("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 2
    });
}

function formatDate(date) {
    return new Date(`${date}T00:00:00`).toLocaleDateString("en-IN", {
        day: "numeric",
        month: "short",
        year: "numeric"
    });
}

function toast(message, type = "success") {
    const host = document.getElementById("toastHost");
    if (!host) return;
    const node = document.createElement("div");
    node.className = `toast ${type}`;
    node.textContent = message;
    host.appendChild(node);
    setTimeout(() => node.remove(), 3400);
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function palette() {
    return ["#14b8a6", "#f97316", "#6366f1", "#22c55e", "#fb7185", "#eab308", "#06b6d4", "#a855f7"];
}

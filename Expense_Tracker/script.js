const STORAGE_KEY = "expenseTracker.transactions";
const THEME_KEY = "expenseTracker.theme";

const categoryColors = {
  Food: "#2563eb",
  Travel: "#14b8a6",
  Bills: "#f59e0b",
  Shopping: "#ec4899",
  Others: "#64748b"
};

const state = {
  transactions: [],
  chart: null
};

const elements = {
  form: document.getElementById("expenseForm"),
  title: document.getElementById("title"),
  amount: document.getElementById("amount"),
  type: document.getElementById("type"),
  category: document.getElementById("category"),
  date: document.getElementById("date"),
  titleError: document.getElementById("titleError"),
  amountError: document.getElementById("amountError"),
  dateError: document.getElementById("dateError"),
  balanceValue: document.getElementById("balanceValue"),
  incomeValue: document.getElementById("incomeValue"),
  expenseValue: document.getElementById("expenseValue"),
  tableBody: document.getElementById("expenseTableBody"),
  emptyState: document.getElementById("emptyState"),
  emptyChart: document.getElementById("emptyChart"),
  searchInput: document.getElementById("searchInput"),
  filterCategory: document.getElementById("filterCategory"),
  sortBy: document.getElementById("sortBy"),
  clearAllBtn: document.getElementById("clearAllBtn"),
  themeToggle: document.getElementById("themeToggle"),
  chartCanvas: document.getElementById("categoryChart")
};

function init() {
  loadTheme();
  loadTransactions();
  setTodayAsDefault();
  bindEvents();
  render();
}

function bindEvents() {
  elements.form.addEventListener("submit", handleFormSubmit);
  elements.searchInput.addEventListener("input", renderTransactionList);
  elements.filterCategory.addEventListener("change", renderTransactionList);
  elements.sortBy.addEventListener("change", renderTransactionList);
  elements.clearAllBtn.addEventListener("click", clearAllTransactions);
  elements.themeToggle.addEventListener("click", toggleTheme);
}

function handleFormSubmit(event) {
  event.preventDefault();

  if (!validateForm()) {
    return;
  }

  const transaction = {
    id: createId(),
    title: elements.title.value.trim(),
    amount: Number(elements.amount.value),
    type: elements.type.value,
    category: elements.category.value,
    date: elements.date.value
  };

  state.transactions.push(transaction);
  saveTransactions();
  elements.form.reset();
  setTodayAsDefault();
  render();
}

function validateForm() {
  let isValid = true;
  clearErrors();

  if (elements.title.value.trim().length < 2) {
    elements.titleError.textContent = "Enter at least 2 characters.";
    isValid = false;
  }

  if (!elements.amount.value || Number(elements.amount.value) <= 0) {
    elements.amountError.textContent = "Enter an amount greater than 0.";
    isValid = false;
  }

  if (!elements.date.value) {
    elements.dateError.textContent = "Choose a date.";
    isValid = false;
  }

  return isValid;
}

function clearErrors() {
  elements.titleError.textContent = "";
  elements.amountError.textContent = "";
  elements.dateError.textContent = "";
}

function render() {
  renderSummary();
  renderTransactionList();
  renderChart();
}

function renderSummary() {
  const income = state.transactions
    .filter((transaction) => transaction.type === "income")
    .reduce((total, transaction) => total + transaction.amount, 0);

  const expenses = state.transactions
    .filter((transaction) => transaction.type === "expense")
    .reduce((total, transaction) => total + transaction.amount, 0);

  elements.incomeValue.textContent = formatCurrency(income);
  elements.expenseValue.textContent = formatCurrency(expenses);
  elements.balanceValue.textContent = formatCurrency(income - expenses);
}

function renderTransactionList() {
  const transactions = getVisibleTransactions();

  elements.tableBody.innerHTML = "";
  elements.emptyState.style.display = transactions.length ? "none" : "block";

  transactions.forEach((transaction) => {
    const row = document.createElement("tr");
    const sign = transaction.type === "income" ? "+" : "-";

    row.innerHTML = `
      <td class="title-cell">${escapeHtml(transaction.title)}</td>
      <td><span class="pill">${transaction.category}</span></td>
      <td>${formatDate(transaction.date)}</td>
      <td><span class="pill type-${transaction.type}">${capitalize(transaction.type)}</span></td>
      <td class="amount-cell amount-${transaction.type}">${sign}${formatCurrency(transaction.amount)}</td>
      <td>
        <button class="delete-btn" type="button" aria-label="Delete ${escapeHtml(transaction.title)}" data-id="${transaction.id}">
          <i class="fa-regular fa-trash-can"></i>
        </button>
      </td>
    `;

    elements.tableBody.appendChild(row);
  });

  document.querySelectorAll(".delete-btn").forEach((button) => {
    button.addEventListener("click", () => deleteTransaction(button.dataset.id));
  });
}

function getVisibleTransactions() {
  const searchTerm = elements.searchInput.value.trim().toLowerCase();
  const category = elements.filterCategory.value;
  const sortBy = elements.sortBy.value;

  return state.transactions
    .filter((transaction) => {
      const matchesSearch = transaction.title.toLowerCase().includes(searchTerm);
      const matchesCategory = category === "All" || transaction.category === category;
      return matchesSearch && matchesCategory;
    })
    .sort((a, b) => {
      if (sortBy === "oldest") return new Date(a.date) - new Date(b.date);
      if (sortBy === "amountHigh") return b.amount - a.amount;
      if (sortBy === "amountLow") return a.amount - b.amount;
      return new Date(b.date) - new Date(a.date);
    });
}

function renderChart() {
  if (typeof Chart === "undefined") {
    elements.emptyChart.textContent = "Chart library unavailable.";
    elements.emptyChart.style.display = "flex";
    return;
  }

  const totals = getExpenseTotalsByCategory();
  const labels = Object.keys(totals).filter((category) => totals[category] > 0);
  const values = labels.map((category) => totals[category]);

  elements.emptyChart.style.display = values.length ? "none" : "flex";

  if (state.chart) {
    state.chart.destroy();
  }

  state.chart = new Chart(elements.chartCanvas, {
    type: "doughnut",
    data: {
      labels,
      datasets: [
        {
          data: values,
          backgroundColor: labels.map((category) => categoryColors[category]),
          borderWidth: 0,
          hoverOffset: 8
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: "68%",
      plugins: {
        legend: {
          position: "bottom",
          labels: {
            boxWidth: 12,
            color: getComputedStyle(document.body).getPropertyValue("--muted")
          }
        },
        tooltip: {
          callbacks: {
            label: (context) => `${context.label}: ${formatCurrency(context.parsed)}`
          }
        }
      }
    }
  });
}

function getExpenseTotalsByCategory() {
  return state.transactions.reduce((totals, transaction) => {
    if (transaction.type === "expense") {
      totals[transaction.category] = (totals[transaction.category] || 0) + transaction.amount;
    }

    return totals;
  }, { Food: 0, Travel: 0, Bills: 0, Shopping: 0, Others: 0 });
}

function deleteTransaction(id) {
  state.transactions = state.transactions.filter((transaction) => transaction.id !== id);
  saveTransactions();
  render();
}

function clearAllTransactions() {
  if (!state.transactions.length) {
    return;
  }

  const confirmed = confirm("Delete all transactions?");
  if (!confirmed) {
    return;
  }

  state.transactions = [];
  saveTransactions();
  render();
}

function loadTransactions() {
  try {
    const savedTransactions = localStorage.getItem(STORAGE_KEY);
    state.transactions = savedTransactions ? JSON.parse(savedTransactions) : [];
  } catch (error) {
    state.transactions = [];
    localStorage.removeItem(STORAGE_KEY);
  }
}

function saveTransactions() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state.transactions));
}

function loadTheme() {
  const savedTheme = localStorage.getItem(THEME_KEY);
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;

  if (savedTheme === "dark" || (!savedTheme && prefersDark)) {
    document.body.classList.add("dark");
    elements.themeToggle.innerHTML = '<i class="fa-solid fa-sun"></i>';
  }
}

function toggleTheme() {
  const isDark = document.body.classList.toggle("dark");
  elements.themeToggle.innerHTML = isDark
    ? '<i class="fa-solid fa-sun"></i>'
    : '<i class="fa-solid fa-moon"></i>';
  localStorage.setItem(THEME_KEY, isDark ? "dark" : "light");
  renderChart();
}

function setTodayAsDefault() {
  elements.date.value = new Date().toISOString().split("T")[0];
}

function createId() {
  if (globalThis.crypto && typeof globalThis.crypto.randomUUID === "function") {
    return globalThis.crypto.randomUUID();
  }

  return `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

function formatCurrency(amount) {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD"
  }).format(amount);
}

function formatDate(date) {
  return new Intl.DateTimeFormat("en-US", {
    day: "2-digit",
    month: "short",
    year: "numeric"
  }).format(new Date(`${date}T00:00:00`));
}

function capitalize(value) {
  return value.charAt(0).toUpperCase() + value.slice(1);
}

// Prevents user-entered titles from being interpreted as HTML.
function escapeHtml(value) {
  return value
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

init();

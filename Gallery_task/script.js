const phase1Images = [
  { title: "The Incredible Hulk (2008)", url: "https://image.tmdb.org/t/p/w500/qyRwj5VvuTRdJ76o2grP93grNxt.jpg" },
  { title: "Iron Man 2 (2010)", url: "https://image.tmdb.org/t/p/w500/6WBeq4fCfn7AN0o21W9qNcRF2l9.jpg" },
  { title: "Thor (2011)", url: "https://image.tmdb.org/t/p/w500/prSfAi1xGrhLQNxVSUFh61xQ4Qy.jpg" },
  { title: "Captain America: The First Avenger (2011)", url: "https://image.tmdb.org/t/p/w500/vSNxAJTlD0r02V9sPYpOjqDZXUK.jpg" },
  { title: "The Avengers (2012)", url: "https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg" }
];

const phase2Images = [
  { title: "Iron Man 3 (2013)", url: "https://image.tmdb.org/t/p/w500/qhPtAc1TKbMPqNvcdXSOn9Bn7hZ.jpg" },
  { title: "Thor: The Dark World (2013)", url: "https://image.tmdb.org/t/p/w500/wp6OxE4poJ4G7c0U2ZIXasTSMR7.jpg" },
  { title: "Captain America: The Winter Soldier (2014)", url: "https://image.tmdb.org/t/p/w500/tVFRpFw3xTedgPGqxW0AOI8Qhh0.jpg" },
  { title: "Guardians of the Galaxy (2014)", url: "https://image.tmdb.org/t/p/w500/r7vmZjiyZw9rpJMQJdXpjgiCOk9.jpg" },
  { title: "Avengers: Age of Ultron (2015)", url: "https://image.tmdb.org/t/p/w500/4ssDuvEDkSArWEdyBl2X5EHvYKU.jpg" },
  { title: "Ant-Man (2015)", url: "https://image.tmdb.org/t/p/w500/D6e8RJf2qUstnfkTslTXNTUAlT.jpg" }
];

const phase3Images = [
  { title: "Civil War (2016)", url: "https://image.tmdb.org/t/p/w500/rAGiXaUfPzY7CDEyNKUofk3Kw2e.jpg" },
  { title: "Doctor Strange (2016)", url: "https://image.tmdb.org/t/p/w500/uGBVj3bEbCoZbDjjl9wTxcygko1.jpg" },
  { title: "Guardians Vol. 2 (2017)", url: "https://image.tmdb.org/t/p/w500/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg" },
  { title: "Spider-Man: Homecoming (2017)", url: "https://image.tmdb.org/t/p/w500/kY2c7wKgOfQjvbqe7yVzLTYkxJO.jpg" },
  { title: "Thor: Ragnarok (2017)", url: "https://image.tmdb.org/t/p/w500/rzRwTcFvttcN1ZpX2xv4j3tSdJu.jpg" },
  { title: "Black Panther (2018)", url: "https://image.tmdb.org/t/p/w500/uxzzxijgPIY7slzFvMotPv8wjKA.jpg" },
  { title: "Infinity War (2018)", url: "https://image.tmdb.org/t/p/w500/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg" },
  { title: "Ant-Man & Wasp (2018)", url: "https://image.tmdb.org/t/p/w500/eivQmS3wqzqnQWILHLc4FsEfcXP.jpg" },
  { title: "Captain Marvel (2019)", url: "https://image.tmdb.org/t/p/w500/AtsgWhDnHTq68L0lLsUrCnM7TjG.jpg" },
  { title: "Endgame (2019)", url: "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg" },
  { title: "Far From Home (2019)", url: "https://image.tmdb.org/t/p/w500/4q2NNj4S5dG2RLF9CpXsej7yXl.jpg" }
];

const phase4Images = [
  { title: "WandaVision (2021)", url: "https://image.tmdb.org/t/p/w500/glKDfE6btIRcVB5zrjspRIs4r52.jpg" },
  { title: "Falcon & Winter Soldier (2021)", url: "https://image.tmdb.org/t/p/w500/6kbAMLteGO8yyewYau6bJ683sw7.jpg" },
  { title: "Loki Season 1 (2021)", url: "https://image.tmdb.org/t/p/w500/voHUmluYmKyleFkTu3lOXQG702u.jpg" }
];

// Central phase metadata keeps the gallery rendering and modal state in sync.
const phases = [
  {
    id: "phase-1",
    label: "Phase One",
    title: "The Assembly",
    description: "The foundation of Earth's mightiest heroes, built through origin stories and a team-up that changed blockbuster cinema.",
    images: phase1Images
  },
  {
    id: "phase-2",
    label: "Phase Two",
    title: "New Frontiers",
    description: "The universe expands with deeper conflicts, cosmic adventure, and the first hints of the storm still to come.",
    images: phase2Images
  },
  {
    id: "phase-3",
    label: "Phase Three",
    title: "The Infinity Saga Finale",
    description: "Worlds collide as heroes fracture, kingdoms rise, and the Infinity Saga reaches its spectacular conclusion.",
    images: phase3Images
  },
  {
    id: "phase-4",
    label: "Phase Four",
    title: "The Multiverse Saga Begins",
    description: "A bold transition into alternate realities, legacy heroes, and the strange new shape of Marvel storytelling.",
    images: phase4Images
  }
];

const autoplayDelay = 2000;
let activePhase = null;
let activeIndex = 0;
let autoplayTimer = null;
let imageSwapTimer = null;
let isPlaying = true;
let lastTrigger = null;

const phaseGrid = document.getElementById("phaseGrid");
const galleryModal = document.getElementById("galleryModal");
const modalBackdrop = document.getElementById("modalBackdrop");
const closeModalButton = document.getElementById("closeModal");
const togglePlaybackButton = document.getElementById("togglePlayback");
const playbackIcon = document.getElementById("playbackIcon");
const prevButton = document.getElementById("prevButton");
const nextButton = document.getElementById("nextButton");
const galleryImage = document.getElementById("galleryImage");
const imageTitle = document.getElementById("imageTitle");
const imageIndex = document.getElementById("imageIndex");
const modalPhaseLabel = document.getElementById("modalPhaseLabel");
const modalPhaseTitle = document.getElementById("modalPhaseTitle");
const modalPhaseSummary = document.getElementById("modalPhaseSummary");
const progressStrip = document.getElementById("progressStrip");

function createPhaseCards() {
  phaseGrid.innerHTML = phases
    .map((phase, index) => {
      const posterPreview = phase.images
        .slice(0, 3)
        .map((image) => `<img src="${image.url}" alt="${image.title} poster preview" loading="lazy">`)
        .join("");

      return `
        <article class="phase-card" tabindex="0" data-phase-index="${index}" aria-label="${phase.label}: ${phase.title}">
          <div class="card-top">
            <span class="phase-label">${phase.label}</span>
            <span class="phase-count">${phase.images.length} titles</span>
          </div>

          <h3>${phase.title}</h3>
          <p class="phase-description">${phase.description}</p>

          <div class="poster-stack" aria-hidden="true">
            ${posterPreview}
          </div>

          <div class="card-footer">
            <span class="card-cta">Open gallery</span>
            <span class="cta-arrow">-&gt;</span>
          </div>
        </article>
      `;
    })
    .join("");
}

function createProgressIndicators() {
  progressStrip.innerHTML = activePhase.images
    .map(() => '<div class="progress-dot"><span></span></div>')
    .join("");
}

function updateProgressIndicators() {
  const progressDots = progressStrip.querySelectorAll(".progress-dot");

  progressDots.forEach((dot, index) => {
    dot.classList.remove("active", "completed");

    if (index < activeIndex) {
      dot.classList.add("completed");
    } else if (index === activeIndex) {
      dot.classList.add("active");
    }
  });
}

function updatePlaybackButton() {
  playbackIcon.textContent = isPlaying ? "||" : ">";
  togglePlaybackButton.setAttribute("aria-label", isPlaying ? "Pause slideshow" : "Play slideshow");
}

function renderActiveImage(nextIndex) {
  if (!activePhase) {
    return;
  }

  const image = activePhase.images[nextIndex];
  const phaseSnapshot = activePhase;

  // Clear any pending swap so rapid navigation only renders the latest slide request.
  window.clearTimeout(imageSwapTimer);
  galleryImage.classList.add("is-transitioning");

  imageSwapTimer = window.setTimeout(() => {
    if (!activePhase || activePhase !== phaseSnapshot) {
      return;
    }

    galleryImage.src = image.url;
    galleryImage.alt = image.title;
    imageTitle.textContent = image.title;
    imageIndex.textContent = `${String(nextIndex + 1).padStart(2, "0")} / ${String(activePhase.images.length).padStart(2, "0")}`;
    activeIndex = nextIndex;
    updateProgressIndicators();
    galleryImage.classList.remove("is-transitioning");
  }, 150);
}

function stopAutoplay() {
  window.clearInterval(autoplayTimer);
  autoplayTimer = null;
}

function startAutoplay() {
  stopAutoplay();

  if (!activePhase || !isPlaying) {
    return;
  }

  autoplayTimer = window.setInterval(() => {
    showNextImage();
  }, autoplayDelay);
}

function openModal(phaseIndex, triggerElement) {
  activePhase = phases[phaseIndex];
  activeIndex = 0;
  isPlaying = true;
  lastTrigger = triggerElement ?? null;

  modalPhaseLabel.textContent = activePhase.label;
  modalPhaseTitle.textContent = activePhase.title;
  modalPhaseSummary.textContent = activePhase.description;

  createProgressIndicators();
  updatePlaybackButton();
  renderActiveImage(activeIndex);
  galleryModal.classList.add("active");
  galleryModal.setAttribute("aria-hidden", "false");
  document.body.classList.add("modal-open");
  // Move focus into the dialog for keyboard users as soon as the gallery opens.
  closeModalButton.focus();

  startAutoplay();
}

function closeModal() {
  stopAutoplay();
  window.clearTimeout(imageSwapTimer);
  galleryModal.classList.remove("active");
  galleryModal.setAttribute("aria-hidden", "true");
  document.body.classList.remove("modal-open");
  activePhase = null;
  galleryImage.classList.remove("is-transitioning");

  if (lastTrigger) {
    lastTrigger.focus();
  }
}

function showNextImage() {
  if (!activePhase) {
    return;
  }

  const nextIndex = (activeIndex + 1) % activePhase.images.length;
  renderActiveImage(nextIndex);
}

function showPreviousImage() {
  if (!activePhase) {
    return;
  }

  const previousIndex = (activeIndex - 1 + activePhase.images.length) % activePhase.images.length;
  renderActiveImage(previousIndex);
}

function togglePlayback() {
  isPlaying = !isPlaying;
  updatePlaybackButton();

  if (isPlaying) {
    startAutoplay();
  } else {
    stopAutoplay();
  }
}

function handleCardActivation(event) {
  const card = event.target.closest(".phase-card");

  if (!card) {
    return;
  }

  openModal(Number(card.dataset.phaseIndex), card);
}

function handleCardKeyboard(event) {
  if (event.key !== "Enter" && event.key !== " ") {
    return;
  }

  const card = event.target.closest(".phase-card");

  if (!card) {
    return;
  }

  event.preventDefault();
  event.stopPropagation();
  openModal(Number(card.dataset.phaseIndex), card);
}

function handleGlobalKeyboard(event) {
  if (galleryModal.getAttribute("aria-hidden") === "true") {
    return;
  }

  // Keep the slideshow fully usable without a mouse.
  if (event.key === "Escape") {
    closeModal();
  }

  if (event.key === "ArrowRight") {
    showNextImage();
    startAutoplay();
  }

  if (event.key === "ArrowLeft") {
    showPreviousImage();
    startAutoplay();
  }

  if (event.key === " " && !event.target.closest("button")) {
    event.preventDefault();
    togglePlayback();
  }
}

phaseGrid.addEventListener("click", handleCardActivation);
phaseGrid.addEventListener("keydown", handleCardKeyboard);
modalBackdrop.addEventListener("click", closeModal);
closeModalButton.addEventListener("click", closeModal);
togglePlaybackButton.addEventListener("click", togglePlayback);
nextButton.addEventListener("click", () => {
  showNextImage();
  startAutoplay();
});
prevButton.addEventListener("click", () => {
  showPreviousImage();
  startAutoplay();
});
document.addEventListener("keydown", handleGlobalKeyboard);

createPhaseCards();

// ===== Config =====
// If the frontend is deployed separately from the backend (e.g. frontend on
// Vercel, backend on Render), set window.CHAT_API_BASE to the backend's full
// URL before this script loads, e.g.:
//   <script>window.CHAT_API_BASE = "https://your-backend.onrender.com";</script>
// If frontend and backend are served together (recommended - see Dockerfile),
// leave it unset and requests will use the same origin automatically.
const CHAT_ENDPOINT = (window.CHAT_API_BASE || "") + "/api/chat";

document.getElementById("year").textContent = new Date().getFullYear();

// ===== Mobile nav =====
const hamburger = document.getElementById("hamburger");
const nav = document.getElementById("nav");
hamburger.addEventListener("click", () => nav.classList.toggle("open"));
document.querySelectorAll("[data-nav]").forEach(link =>
  link.addEventListener("click", () => nav.classList.remove("open"))
);

// ===== Typing effect =====
const roles = [
  "Java Developer",
  "Core Java Enthusiast",
  "AI/ML Engineer",
  "Computer Vision Developer",
];
const typingEl = document.getElementById("typing-text");
let roleIdx = 0, charIdx = 0, deleting = false;

function typeLoop() {
  const current = roles[roleIdx];
  if (!deleting) {
    charIdx++;
    typingEl.textContent = current.slice(0, charIdx);
    if (charIdx === current.length) {
      deleting = true;
      setTimeout(typeLoop, 1400);
      return;
    }
  } else {
    charIdx--;
    typingEl.textContent = current.slice(0, charIdx);
    if (charIdx === 0) {
      deleting = false;
      roleIdx = (roleIdx + 1) % roles.length;
    }
  }
  setTimeout(typeLoop, deleting ? 40 : 80);
}
typeLoop();

// ===== Scroll reveal =====
const revealEls = document.querySelectorAll(".reveal");
const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add("in-view");
      revealObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.15 });
revealEls.forEach(el => revealObserver.observe(el));

// ===== Animated skill bars =====
const bars = document.querySelectorAll(".bar-fill");
const barObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.style.width = entry.target.dataset.pct + "%";
      barObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.4 });
bars.forEach(b => barObserver.observe(b));

// ===== Animated stat counters =====
const stats = document.querySelectorAll(".stat-num");
const statObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      animateCount(entry.target);
      statObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.6 });
stats.forEach(s => statObserver.observe(s));

function animateCount(el) {
  const target = parseFloat(el.dataset.count);
  const isDecimal = target % 1 !== 0;
  let current = 0;
  const duration = 1200;
  const start = performance.now();
  function step(now) {
    const progress = Math.min((now - start) / duration, 1);
    current = target * progress;
    el.textContent = isDecimal ? current.toFixed(2) : Math.round(current);
    if (progress < 1) requestAnimationFrame(step);
    else el.textContent = isDecimal ? target.toFixed(2) : target;
  }
  requestAnimationFrame(step);
}

// ===== Header shrink on scroll =====
const header = document.getElementById("header");
window.addEventListener("scroll", () => {
  header.style.boxShadow = window.scrollY > 20 ? "0 8px 24px rgba(0,0,0,0.35)" : "none";
});

// ===== AVATAR ANIMATION (real photo, animated ring/glow) =====
const avatarPhotoWrap = document.getElementById("avatar-photo-wrap");
const avatarStatus = document.getElementById("avatar-status");

function startTalking() {
  avatarStatus.textContent = "thinking...";
  avatarPhotoWrap.classList.add("talking");
}

function stopTalking(statusText) {
  avatarPhotoWrap.classList.remove("talking");
  avatarStatus.textContent = statusText || "idle - say hi in chat";
}

// ===== CHAT WIDGET =====
const launcher = document.getElementById("chat-launcher");
const panel = document.getElementById("chat-panel");
const closeBtn = document.getElementById("chat-close");
const messagesEl = document.getElementById("chat-messages");
const form = document.getElementById("chat-form");
const input = document.getElementById("chat-input");
const suggestions = document.getElementById("chat-suggestions");
const chatSubtitle = document.getElementById("chat-subtitle");

function openChat() {
  panel.classList.add("open");
  panel.setAttribute("aria-hidden", "false");
  input.focus();
}
function closeChat() {
  panel.classList.remove("open");
  panel.setAttribute("aria-hidden", "true");
}
launcher.addEventListener("click", () => panel.classList.contains("open") ? closeChat() : openChat());
closeBtn.addEventListener("click", closeChat);
document.getElementById("hero-chat-btn").addEventListener("click", (e) => {
  e.preventDefault();
  openChat();
});

function addMessage(text, who) {
  const wrap = document.createElement("div");
  wrap.className = `msg ${who}`;

  const textEl = document.createElement("div");
  textEl.className = "msg-text";
  textEl.textContent = text;
  wrap.appendChild(textEl);

  const meta = document.createElement("div");
  meta.className = "msg-meta";
  const time = new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  const timeSpan = document.createElement("span");
  timeSpan.textContent = time;
  meta.appendChild(timeSpan);

  if (who === "bot") {
    const copyBtn = document.createElement("button");
    copyBtn.className = "msg-copy";
    copyBtn.type = "button";
    copyBtn.textContent = "copy";
    copyBtn.addEventListener("click", () => {
      navigator.clipboard?.writeText(text).then(() => {
        copyBtn.textContent = "copied!";
        setTimeout(() => (copyBtn.textContent = "copy"), 1200);
      });
    });
    meta.appendChild(copyBtn);
  }
  wrap.appendChild(meta);

  messagesEl.appendChild(wrap);
  messagesEl.scrollTop = messagesEl.scrollHeight;
  return wrap;
}

function addTypingIndicator() {
  const div = document.createElement("div");
  div.className = "msg bot typing";
  div.innerHTML = "<span></span><span></span><span></span>";
  messagesEl.appendChild(div);
  messagesEl.scrollTop = messagesEl.scrollHeight;
  return div;
}

// greet on first open
let greeted = false;
launcher.addEventListener("click", () => {
  if (!greeted) {
    greeted = true;
    setTimeout(() => {
      addMessage("Hey! I'm Darshan's AI assistant. Ask me anything about his skills, projects, or background.", "bot");
    }, 300);
  }
});

suggestions.querySelectorAll("button").forEach(btn => {
  btn.addEventListener("click", () => {
    input.value = btn.dataset.q;
    form.requestSubmit();
  });
});

async function sendMessage(message) {
  addMessage(message, "user");
  input.value = "";
  const typingBubble = addTypingIndicator();
  startTalking();

  try {
    const res = await fetch(CHAT_ENDPOINT, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ message }),
    });
    if (!res.ok) throw new Error("Server error " + res.status);
    const data = await res.json();
    typingBubble.remove();
    addMessage(data.reply, "bot");
    chatSubtitle.textContent = "Powered by a Darshan's AI Assistance";
    stopTalking("just replied ✓");
  } catch (err) {
    typingBubble.remove();
    addMessage(
      "Hmm, I can't reach the Java backend right now. Make sure PortfolioServer is running (see README) — meanwhile, feel free to check the sections above!",
      "bot"
    );
    chatSubtitle.textContent = "backend offline";
    stopTalking("backend offline");
  }
}

form.addEventListener("submit", (e) => {
  e.preventDefault();
  const val = input.value.trim();
  if (!val) return;
  sendMessage(val);
});
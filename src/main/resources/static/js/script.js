document.addEventListener("DOMContentLoaded", () => {

  console.log("🔥 JS READY 🔥");

  let currentTheme = getTheme();
  changeTheme(currentTheme);

  // 👉 BOTH buttons select
  const buttons = document.querySelectorAll("#theme_change_button, #theme_change_button_mobile");

  // set initial text
  buttons.forEach(btn => {
    const span = btn.querySelector("span");
    if (span) {
      span.textContent = currentTheme === "light" ? "Dark" : "Light";
    }
  });

  // click event
  buttons.forEach(btn => {
    btn.addEventListener("click", () => {

      currentTheme = currentTheme === "light" ? "dark" : "light";

      setTheme(currentTheme);
      changeTheme(currentTheme);

      buttons.forEach(b => {
        const span = b.querySelector("span");
        if (span) {
          span.textContent = currentTheme === "light" ? "Dark" : "Light";
        }
      });

    });
  });

});

/* ===== FUNCTIONS ===== */

function changeTheme(theme) {
  document.documentElement.classList.remove("light", "dark");
  document.documentElement.classList.add(theme);
}

function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

function getTheme() {
  return localStorage.getItem("theme") || "light";
}
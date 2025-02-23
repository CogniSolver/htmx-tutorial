document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".navbar a").forEach((link) => {
    link.addEventListener("click", function () {
      if (this.textContent === "Students" || this.textContent === "Schools") {
        document
          .querySelectorAll(".navbar a")
          .forEach((a) => a.classList.remove("active"));
        this.classList.add("active");
      }
    });
  });
});

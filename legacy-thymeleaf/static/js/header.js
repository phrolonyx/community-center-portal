document.addEventListener("DOMContentLoaded", () => {
    const container = document.querySelector(".profile-container");
    const trigger = document.querySelector(".profile-trigger");

    trigger.addEventListener("click", () => {
        container.classList.toggle("active");
    });

    document.addEventListener("click", (e) => {
        if (!container.contains(e.target)) {
            container.classList.remove("active");
        }
    });
});

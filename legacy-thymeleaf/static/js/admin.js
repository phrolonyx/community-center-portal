// =====================================
// POPUP DECONNEXION
// =====================================

document.addEventListener("DOMContentLoaded", () => {
    const adminIcon = document.getElementById("admin-icon");
    const popup = document.getElementById("logout-popup");

    if (!adminIcon || !popup) return;

    // Ouvrir / fermer le popup
    adminIcon.addEventListener("click", (e) => {
        e.stopPropagation();
        popup.style.display = popup.style.display === "block" ? "none" : "block";
    });

    // Fermer si on clique ailleurs
    document.addEventListener("click", (e) => {
        if (!popup.contains(e.target) && e.target !== adminIcon) {
            popup.style.display = "none";
        }
    });
});

// =====================================
//Barre de Recherche User
// =====================================


document.addEventListener("DOMContentLoaded", function () {

    const input1 = document.querySelector(".search-input");
    const tbody1 = document.getElementById("users-body");

    input1.addEventListener("input", function () {

        const value = input1.value.trim();

        fetch("/admin/users/search-ajax?search=" + encodeURIComponent(value))
            .then(response => response.json())
            .then(users => {

                // On vide le tableau
                tbody1.innerHTML = "";

                // On reconstruit les lignes
                users.forEach(user => {

                    const row1 = document.createElement("tr");

                    row1.innerHTML = `
                        <td><button class="user-action">Supprimer</button></td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.role.name}</td>
                        <td>${user.email}</td>
                        <td>${user.enabled ? "oui" : "non"}</td>
                        <td>${user.lastLogin ? user.lastLogin.substring(0,10) : "-"}</td>
                        <td>${user.createdAt.substring(0,10)}</td>
                    `;

                    tbody1.appendChild(row1);
                });
            });
    });
});


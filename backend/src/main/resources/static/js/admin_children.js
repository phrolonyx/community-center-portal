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
//Barre de Recherche Enfants
// =====================================


document.addEventListener("DOMContentLoaded", function () {


    const input2 = document.querySelector(".search-input");
    const tbody2 = document.getElementById("children-body");

    if (!input2 || !tbody2) return;

    input2.addEventListener("input", function () {

        const value = input2.value.trim();

        fetch("/admin/enfants/search-ajax?search=" + encodeURIComponent(value))
            .then(response => response.json())
            .then(children => {

                tbody2.innerHTML = "";

                children.forEach(child => {

                    const row2 = document.createElement("tr");

                    const fullName = child.firstName + " " + child.lastName;
                    const dob = child.dateOfBirth ? child.dateOfBirth.substring(0, 10) : "-";
                    const age = child.age ?? "-";

                    let parents = "Aucun";
                    if (child.parentLinks && child.parentLinks.length > 0) {
                        parents = child.parentLinks
                            .map(link => link.parentProfile.user.firstName + " " + link.parentProfile.user.lastName)
                            .join("<br>");
                    }

                    let conditions = "Aucune";
                    if (child.medicalConditions && child.medicalConditions.length > 0) {
                        conditions = child.medicalConditions
                            .map(cond => cond.condition)
                            .join("<br>");
                    }

                    row2.innerHTML = `
                        <td><button class="user-action">Supprimer</button></td>
                        <td>${fullName}</td>
                        <td>${parents}</td>
                        <td>${dob}</td>
                        <td>${age}</td>
                        <td>${conditions}</td>
                    `;

                    tbody2.appendChild(row2);
                });
            });
    });
});

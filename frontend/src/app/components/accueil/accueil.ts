import { Component, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-accueil',
  standalone: false,
  templateUrl: './accueil.html',
  styleUrl: './accueil.css',
})
export class Accueil implements AfterViewInit {
  // ngAfterViewInit() = méthode Angular appelée quand le DOM du composant est entièrement chargé
  ngAfterViewInit() {

    // Création d’un IntersectionObserver
    // → Il surveille quand un élément entre dans la zone visible de l’écran (viewport)
    const observer = new IntersectionObserver((entries, obs) => {

      // entries = liste des éléments observés + leur état (visible ou non)
      entries.forEach(entry => {

        // entry.isIntersecting = true quand l’élément apparaît à l’écran
        if (entry.isIntersecting) {

          // On ajoute la classe "visible"
          // → déclenche l’animation CSS (fade, slide-up, zoom, etc.)
          entry.target.classList.add("visible");

          // On arrête d’observer cet élément
          // → l’animation ne se joue qu’une seule fois
          obs.unobserve(entry.target);
        }
      });

    }, { threshold: 0.2 }); 
    // threshold: 0.2 = l’animation se déclenche quand 20% de l’élément est visible

    // On sélectionne tous les éléments qui doivent être animés
    // → ceux qui ont les classes fade, slide-up, zoom ou pop
    document.querySelectorAll(".fade, .slide-up, .zoom, .pop")
      .forEach(el => observer.observe(el)); 
      // On demande à l’observer de surveiller chaque élément
  }
}

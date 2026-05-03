import { Component } from '@angular/core';

@Component({
  selector: 'app-animateur-sidebar',
  standalone: false,
  templateUrl: './animateur-sidebar.html',
  styleUrl: './animateur-sidebar.css',
})
export class AnimateurSidebar {
    animateurName = "Animateur"; // statique pour l'instant
    logoutOpen = false;

    toggleLogout() {
      this.logoutOpen = !this.logoutOpen;
    }

    logout() {
      // Pour l'instant statique
      // Plus tard : authService.logout()
      window.location.href = " ";
    }
}

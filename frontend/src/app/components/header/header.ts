import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  menuOuvert = false;
  // Panneau de navigation sur mobile
  navOuverte = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  // Échap referme le panneau mobile et le menu profil
  @HostListener('document:keydown.escape')
  fermerMenus(): void {
    this.navOuverte = false;
    this.menuOuvert = false;
  }

  // Lien Nos activites selon le role
  lienActivites(): string {
    if (this.authService.hasRole('PARENT')) return '/dashboard-parent/activites';
    return '/view-activities';
  }

  allerAuDashboard(): void {
    if (this.authService.hasRole('ADMIN')) {
      this.router.navigate(['/admin/dashboard']);
    } else if (this.authService.hasRole('PARENT')) {
      this.router.navigate(['/dashboard-parent']);
    } else if (this.authService.hasRole('ANIMATEUR')) {
      this.router.navigate(['/animateur/sceances']);
    }
  }

  logout(): void {
    // Vide immediatement le user local (le menu ferme grace au isAuthenticated)
    this.authService.currentUser.set(null);
    this.menuOuvert = false;

    // Notifie le serveur en arriere-plan (best-effort, on attend pas)
    this.authService.logout().subscribe({
      next: () => {},
      error: () => {},
    });

    // Force une vraie redirection (hard reload) pour vider tous les composants/intervals/cache
    window.location.href = '/connexion?logout=1';
  }
}

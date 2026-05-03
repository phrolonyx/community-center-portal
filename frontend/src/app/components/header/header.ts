import { Component, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
  // OnPush permet au template de réagir aux changements de signals
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Header {

  menuOuvert = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  // Rediriger vers le dashboard correspondant au rôle de l'utilisateur
  allerAuDashboard(): void {
    if (this.authService.hasRole('ADMIN')) {
      this.router.navigate(['/admin/dashboard']);
    } else if (this.authService.hasRole('PARENT')) {
      this.router.navigate(['/dashboard-parent']);
    } else if (this.authService.hasRole('ANIMATEUR')) {
      this.router.navigate(['/animateur/sceances']);
    }
  }

  // Déconnexion — vide le signal currentUser et redirige vers connexion
  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.menuOuvert = false;
        this.router.navigate(['/connexion'], { queryParams: { logout: '1' } });
      },
      error: () => {
        // Forcer la déconnexion locale même si le serveur ne répond pas
        this.router.navigate(['/connexion']);
      }
    });
  }
}

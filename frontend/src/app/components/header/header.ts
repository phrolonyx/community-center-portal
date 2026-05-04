import { Component, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Header {
  menuOuvert = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

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
    this.authService.logout().subscribe({
      next: () => {
        this.menuOuvert = false;
        this.router.navigate(['/connexion'], { queryParams: { logout: '1' } });
      },
      error: () => {
        this.router.navigate(['/connexion']);
      }
    });
  }
}

import { Component, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../apiServices/auth.service';

@Component({
  selector: 'app-admin-sidebar',
  standalone: false,
  templateUrl: './admin-sidebar.html',
  styleUrl: './admin-sidebar.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminSidebar {

  logoutOpen = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  toggleLogout(): void {
    this.logoutOpen = !this.logoutOpen;
  }

  // Déconnexion via AuthService — vide le signal et redirige
  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/connexion'], { queryParams: { logout: '1' } });
      },
      error: () => {
        this.router.navigate(['/connexion']);
      }
    });
  }
}

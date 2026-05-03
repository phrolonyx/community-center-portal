import {ChangeDetectionStrategy, Component} from '@angular/core';
import {AuthService} from '../../../apiServices/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-animateur-sidebar',
  standalone: false,
  templateUrl: './animateur-sidebar.html',
  styleUrl: './animateur-sidebar.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnimateurSidebar {
    logoutOpen = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

    toggleLogout() {
      this.logoutOpen = !this.logoutOpen;
    }

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

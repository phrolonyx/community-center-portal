import {ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../../apiServices/auth.service';
import {Router} from '@angular/router';
import {MessageService} from '../../../apiServices/message-service';

@Component({
  selector: 'app-animateur-sidebar',
  standalone: false,
  templateUrl: './animateur-sidebar.html',
  styleUrl: './animateur-sidebar.css',
})
export class AnimateurSidebar implements OnInit, OnDestroy {
    logoutOpen = false;
    // Menu replié/déplié sur mobile
    menuOuvert = false;
    nonLus = 0;
    private intervalId: any;

  constructor(
    public authService: AuthService,
    private router: Router,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.charger();
    this.intervalId = setInterval(() => this.charger(), 15000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
  }

  charger(): void {
    this.messageService.countUnreadAnim().subscribe({
      next: (res) => { this.nonLus = res.count; this.cdr.detectChanges(); },
      error: () => {},
    });
  }

  // Échap referme le menu mobile
  @HostListener('document:keydown.escape')
  fermerMenu(): void {
    this.menuOuvert = false;
    this.logoutOpen = false;
    this.cdr.detectChanges();
  }

    toggleLogout() {
      this.logoutOpen = !this.logoutOpen;
      this.cdr.detectChanges();
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

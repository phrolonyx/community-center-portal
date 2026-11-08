import { ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../apiServices/auth.service';
import { MessageService } from '../../../apiServices/message-service';

@Component({
  selector: 'app-admin-sidebar',
  standalone: false,
  templateUrl: './admin-sidebar.html',
  styleUrl: './admin-sidebar.css',
})
export class AdminSidebar implements OnInit, OnDestroy {

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
    this.messageService.countUnreadAdmin().subscribe({
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

  toggleLogout(): void {
    this.logoutOpen = !this.logoutOpen;
    this.cdr.detectChanges();
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

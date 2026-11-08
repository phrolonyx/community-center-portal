import { ChangeDetectorRef, Component, HostListener, Input, OnDestroy, OnInit } from '@angular/core';
import { MessageService } from '../../../apiServices/message-service';

// Composant Angular dynamique avec data binding (Routing parent)
@Component({
  selector: 'app-parent-sidebar',
  standalone: false,
  templateUrl: './parent-sidebar.html',
  styleUrl: './parent-sidebar.css',
})
export class ParentSidebar implements OnInit, OnDestroy {
  @Input() active: string = '';

  // Menu replié/déplié sur mobile
  menuOuvert = false;

  // Nom de la page courante, affiché dans le bouton du menu mobile
  private readonly libelles: Record<string, string> = {
    apercu: 'Accueil',
    activites: 'Activités',
    enfants: 'Mes enfants',
    inscriptions: 'Inscriptions',
    messages: 'Messages',
    profil: 'Mon profil',
  };

  libelleActif(): string {
    return this.libelles[this.active] ?? 'Menu';
  }

  // Échap referme le menu mobile
  @HostListener('document:keydown.escape')
  fermerMenu(): void {
    this.menuOuvert = false;
  }

  // Compteur de messages non lus pour le badge
  nonLus = 0;
  private intervalId: any;

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.charger();
    // Rafraîchit toutes les 15 secondes
    this.intervalId = setInterval(() => this.charger(), 15000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
  }

  charger(): void {
    this.messageService.countUnread().subscribe({
      next: (res) => { this.nonLus = res.count; this.cdr.detectChanges(); },
      error: () => {},
    });
  }
}

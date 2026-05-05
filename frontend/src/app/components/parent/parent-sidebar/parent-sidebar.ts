import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
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

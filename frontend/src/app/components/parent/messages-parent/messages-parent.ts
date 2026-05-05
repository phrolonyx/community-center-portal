import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { MessageService, MessageDto } from '../../../apiServices/message-service';

interface Sender {
  fullName: string;
}

interface Message {
  id: number;
  subject: string;
  content: string;
  isRead: boolean;
  type: 'ANNULATION' | 'RAPPEL' | 'ANNONCE' | string;
  senderInitials: string;
  sender: Sender;
  createdAt: string;
}

// Composant Angular dynamique - Consommation API REST via HttpClient
@Component({
  selector: 'app-messages-parent',
  standalone: false,
  templateUrl: './messages-parent.html',
  styleUrl: './messages-parent.css',
})
export class MessagesParent implements OnInit, OnDestroy {
  messages: Message[] = [];
  messagesOuverts = new Set<number>();
  private intervalId: any;

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.charger();
    // Rafraichit toutes les 15s pour voir les nouveaux messages
    this.intervalId = setInterval(() => this.charger(), 15000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
  }

  private charger(): void {
    this.messageService.getMesMessages().subscribe({
      next: (data: MessageDto[]) => {
        this.messages = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erreur chargement messages', err),
    });
  }

  get unreadCount(): number {
    return this.messages.filter(m => !m.isRead).length;
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  toggleMessage(msg: Message): void {
    if (this.messagesOuverts.has(msg.id)) {
      this.messagesOuverts.delete(msg.id);
    } else {
      this.messagesOuverts.add(msg.id);
      // Marquer comme lu à l'ouverture
      if (!msg.isRead) {
        this.markAsRead(msg);
      }
    }
  }

  isOpen(msg: Message): boolean {
    return this.messagesOuverts.has(msg.id);
  }

  markAsRead(msg: Message): void {
    this.messageService.marquerLu(msg.id).subscribe({
      next: () => {
        msg.isRead = true;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erreur markAsRead', err),
    });
  }

  // Supprimer un message après confirmation
  supprimer(msg: Message): void {
    if (!confirm('Supprimer ce message ?')) {
      return;
    }
    this.messageService.supprimer(msg.id).subscribe({
      next: () => {
        this.messages = this.messages.filter(m => m.id !== msg.id);
        this.messagesOuverts.delete(msg.id);
        this.cdr.detectChanges();
      },
      error: (err) => {
        alert(err?.error?.error || 'Impossible de supprimer ce message');
      },
    });
  }
}

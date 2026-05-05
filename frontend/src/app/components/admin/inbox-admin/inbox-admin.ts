import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { MessageService, MessageDto } from '../../../apiServices/message-service';

interface Sender { fullName: string; }

interface Message {
  id: number;
  subject: string;
  content: string;
  isRead: boolean;
  type: string;
  senderInitials: string;
  sender: Sender;
  createdAt: string;
}

@Component({
  selector: 'app-inbox-admin',
  standalone: false,
  templateUrl: './inbox-admin.html',
  styleUrl: './inbox-admin.css',
})
export class InboxAdmin implements OnInit, OnDestroy {
  messages: Message[] = [];
  messagesOuverts = new Set<number>();
  private intervalId: any;

  constructor(
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

  private charger(): void {
    this.messageService.getMesMessagesAdmin().subscribe({
      next: (data: MessageDto[]) => { this.messages = data; this.cdr.detectChanges(); },
      error: (err) => console.error('Erreur chargement messages admin', err),
    });
  }

  get unreadCount(): number {
    return this.messages.filter(m => !m.isRead).length;
  }

  toggleMessage(msg: Message): void {
    if (this.messagesOuverts.has(msg.id)) {
      this.messagesOuverts.delete(msg.id);
    } else {
      this.messagesOuverts.add(msg.id);
      if (!msg.isRead) this.markAsRead(msg);
    }
  }

  isOpen(msg: Message): boolean {
    return this.messagesOuverts.has(msg.id);
  }

  markAsRead(msg: Message): void {
    this.messageService.marquerLuAdmin(msg.id).subscribe({
      next: () => { msg.isRead = true; this.cdr.detectChanges(); },
      error: (err) => console.error(err),
    });
  }

  // Suppression cote admin
  supprimer(msg: Message): void {
    if (!confirm('Supprimer ce message ?')) return;
    this.messageService.supprimerAdmin(msg.id).subscribe({
      next: () => {
        this.messages = this.messages.filter(m => m.id !== msg.id);
        this.messagesOuverts.delete(msg.id);
        this.cdr.detectChanges();
      },
      error: () => alert('Impossible de supprimer ce message'),
    });
  }
}

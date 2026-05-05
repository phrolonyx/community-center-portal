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
  selector: 'app-inbox-animateur',
  standalone: false,
  templateUrl: './inbox-animateur.html',
  styleUrl: './inbox-animateur.css',
})
export class InboxAnimateur implements OnInit, OnDestroy {
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
    this.messageService.getMesMessagesAnim().subscribe({
      next: (data: MessageDto[]) => { this.messages = data; this.cdr.detectChanges(); },
      error: (err) => console.error('Erreur chargement messages animateur', err),
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
    this.messageService.marquerLuAnim(msg.id).subscribe({
      next: () => { msg.isRead = true; this.cdr.detectChanges(); },
      error: (err) => console.error(err),
    });
  }

  supprimer(msg: Message): void {
    if (!confirm('Supprimer ce message ?')) return;
    this.messageService.supprimerAnim(msg.id).subscribe({
      next: () => {
        this.messages = this.messages.filter(m => m.id !== msg.id);
        this.messagesOuverts.delete(msg.id);
        this.cdr.detectChanges();
      },
      error: () => alert('Impossible de supprimer ce message'),
    });
  }
}

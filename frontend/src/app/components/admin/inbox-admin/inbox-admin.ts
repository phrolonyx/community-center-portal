import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { MessageDto, MessageService } from '../../../apiServices/message-service';

const POLLING_DELAY_MS = 15_000;

@Component({
  selector: 'app-inbox-admin',
  standalone: false,
  templateUrl: './inbox-admin.html',
  styleUrl: './inbox-admin.css',
})
export class InboxAdmin implements OnInit, OnDestroy {

  messages: MessageDto[] = [];
  messagesOuverts = new Set<number>();
  private intervalId: any;

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.charger();
    this.intervalId = setInterval(() => this.charger(), POLLING_DELAY_MS);
  }

  ngOnDestroy(): void {
    if (this.intervalId) clearInterval(this.intervalId);
  }

  get unreadCount(): number {
    return this.messages.filter(m => !m.isRead).length;
  }

  toggleMessage(msg: MessageDto): void {
    if (this.messagesOuverts.has(msg.id)) {
      this.messagesOuverts.delete(msg.id);
    } else {
      this.messagesOuverts.add(msg.id);
      if (!msg.isRead) this.markAsRead(msg);
    }
  }

  isOpen(msg: MessageDto): boolean {
    return this.messagesOuverts.has(msg.id);
  }

  markAsRead(msg: MessageDto): void {
    this.messageService.marquerLuAdmin(msg.id).subscribe({
      next: () => { msg.isRead = true; this.cdr.detectChanges(); },
      error: () => {},
    });
  }

  supprimer(msg: MessageDto): void {
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

  private charger(): void {
    this.messageService.getMesMessagesAdmin().subscribe({
      next: (data) => { this.messages = data; this.cdr.detectChanges(); },
      error: () => {},
    });
  }
}

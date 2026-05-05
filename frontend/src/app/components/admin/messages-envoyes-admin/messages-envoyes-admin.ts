import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MessageService, SentMessageDto } from '../../../apiServices/message-service';

@Component({
  selector: 'app-messages-envoyes-admin',
  standalone: false,
  templateUrl: './messages-envoyes-admin.html',
  styleUrl: './messages-envoyes-admin.css',
})
export class MessagesEnvoyesAdmin implements OnInit {

  messages: SentMessageDto[] = [];
  ouverts = new Set<number>();

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.messageService.getMessagesEnvoyesAdmin().subscribe({
      next: (data) => { this.messages = data; this.cdr.detectChanges(); },
      error: () => {},
    });
  }

  toggle(id: number): void {
    if (this.ouverts.has(id)) this.ouverts.delete(id);
    else this.ouverts.add(id);
  }

  isOpen(id: number): boolean {
    return this.ouverts.has(id);
  }
}

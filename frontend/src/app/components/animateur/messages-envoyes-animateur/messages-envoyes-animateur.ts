import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MessageService, SentMessageDto } from '../../../apiServices/message-service';

@Component({
  selector: 'app-messages-envoyes-animateur',
  standalone: false,
  templateUrl: './messages-envoyes-animateur.html',
  styleUrl: './messages-envoyes-animateur.css',
})
export class MessagesEnvoyesAnimateur implements OnInit {

  messages: SentMessageDto[] = [];
  ouverts = new Set<number>();

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.messageService.getMessagesEnvoyesAnim().subscribe({
      next: (data) => { this.messages = data; this.cdr.detectChanges(); },
      error: (err) => console.error('Erreur chargement historique animateur', err),
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

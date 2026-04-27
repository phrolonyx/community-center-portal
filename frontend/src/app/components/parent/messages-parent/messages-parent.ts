import { Component, OnInit } from '@angular/core';

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
export class MessagesParent implements OnInit {
  messages: Message[] = [];
  messagesOuverts = new Set<number>();

  ngOnInit(): void {
    this.messages = [];
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
    }
  }

  isOpen(msg: Message): boolean {
    return this.messagesOuverts.has(msg.id);
  }

  markAsRead(msg: Message): void {
    msg.isRead = true;
  }
}

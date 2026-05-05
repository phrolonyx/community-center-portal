import { Component, OnInit, signal } from '@angular/core';
import { ActivitySession, ActivitySessionService } from '../../../apiServices/activity-session-service';

@Component({
  selector: 'app-sceances-admin',
  standalone: false,
  templateUrl: './sceances-admin.html',
  styleUrl: './sceances-admin.css',
})
export class SceancesAdmin implements OnInit {

  sessions = signal<ActivitySession[]>([]);

  constructor(private sessionService: ActivitySessionService) {}

  ngOnInit() {
    this.loadSessions();
  }

  // Charger toutes les séances depuis l'API
  loadSessions() {
    this.sessionService.getAllSessions().subscribe({
      next: data => this.sessions.set(data),
      error: () => this.sessions.set([])
    });
  }

  // Annuler une séance : notifie tous les parents inscrits par courriel
  deleteSession(id: number) {
    if (!confirm('Annuler cette séance ? Tous les parents inscrits seront notifiés par courriel.')) {
      return;
    }
    this.sessionService.cancelSession(id).subscribe({
      next: (res) => {
        alert(`Séance annulée. ${res.notifies} parent(s) notifié(s) par courriel.`);
        this.sessions.set(this.sessions().filter(s => s.id !== id));
      },
      error: () => alert("Erreur lors de l'annulation de la séance"),
    });
  }
}

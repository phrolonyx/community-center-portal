import {ChangeDetectionStrategy, Component, computed, OnInit, signal} from '@angular/core';
import {ActivitySession, ActivitySessionService} from '../../../apiServices/activity-session-service';
import {AuthService} from '../../../apiServices/auth.service';

@Component({
  selector: 'app-sceances-animateur',
  standalone: false,
  templateUrl: './sceances-animateur.html',
  styleUrl: './sceances-animateur.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SceancesAnimateur implements OnInit {

  ngOnInit() {
    this.loadSessions();
  }

  constructor(private sessionService: ActivitySessionService, private authService : AuthService) {}

  // Signal pour la liste complète des activités
  sessions = signal<ActivitySession[]>([]);

  // sessions filtrées par animateur actuelle
  sessionsParAnimateur = computed(() => {
    const animateurId = this.authService.currentUser()?.id;
    return this.sessions().filter(s => s.hostId === animateurId);
  });

  private loadSessions() {
    this.sessionService.getAllSessions().subscribe({
      next: data => this.sessions.set(data),
      error: error => this.sessions.set([]),
    });
  }
}

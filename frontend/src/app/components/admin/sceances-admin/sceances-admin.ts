import {Component, signal} from '@angular/core';
import {ActivitySession, ActivitySessionService} from '../../../apiServices/activity-session-service';

@Component({
  selector: 'app-sceances-admin',
  standalone: false,
  templateUrl: './sceances-admin.html',
  styleUrl: './sceances-admin.css',
})
export class SceancesAdmin {
  constructor(private sessionService: ActivitySessionService) {}

  ngOnInit() {
    this.loadSessions();
  }

  // Liste brute venant de l’API
  sessions = signal<ActivitySession[]>([]);

  // Charger toutes les séances
  loadSessions() {
    this.sessionService.getAllSessions().subscribe({
      next: data => this.sessions.set(data),
      error: () => this.sessions.set([])
    });
  }

  // Supprimer séance
  deleteSession(id: number) {
    this.sessionService.deleteSession(id).subscribe({
      next: () => {
        const updated = this.sessions().filter(s => s.id !== id);
        this.sessions.set(updated);
      },
      error: err => console.error("Erreur suppression séance :", err)
    });
  }

    //  Liste des séances (à remplacer par API)
  // sessions = [
    // Exemple :
    // {
    //   id: 1,
    //   activityName: "Danse",
    //   hostName: "Sophie Martin",
    //   locationName: "Salle A",
    //   capacity: 20,
    //   dateTimeStart: new Date(),
    //   dateTimeEnd: new Date()
    // }
  // ];

  // Supprimer séance
  // deleteSession(id: number) {
    // console.log("Supprimer séance ID:", id);
    // //
  // }
}

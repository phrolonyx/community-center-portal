import {Component, computed, signal} from '@angular/core';
import {Activity} from '../../../models/activity.model';
import {ActivityService} from '../../../apiServices/activity-service';

@Component({
  selector: 'app-activites-admin',
  standalone: false,
  templateUrl: './activites-admin.html',
  styleUrl: './activites-admin.css',
})
export class ActivitesAdmin {
  constructor(private activityService: ActivityService) {}

  ngOnInit() {
    this.loadActivities();
  }

  // Liste brute venant de l’API
  activities = signal<Activity[]>([]);

  // Charger toutes les activités
  loadActivities() {
    this.activityService.getActivities().subscribe({
      next: data => this.activities.set(data),
      error: () => this.activities.set([])
    });
  }

  // Supprimer activité
  deleteActivity(id: number) {
    this.activityService.deleteActivities(id).subscribe({
      next: () => {
        const updated = this.activities().filter(a => a.id !== id);
        this.activities.set(updated);
      },
      error: err => console.error("Erreur suppression activité :", err)
    });
  }

  }


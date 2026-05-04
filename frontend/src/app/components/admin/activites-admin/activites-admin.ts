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

  // Valeur de recherche
  searchValue = signal('');

  // Charger toutes les activités
  loadActivities() {
    this.activityService.getActivities().subscribe({
      next: data => this.activities.set(data),
      error: () => this.activities.set([])
    });
  }

  // 🔍 Recherche locale (nom + description)
  // filteredActivities = computed(() => {
  //   const search = this.searchValue().toLowerCase().trim();
  //   if (!search) return this.activities();
  //
  //   return this.activities().filter(activity =>
  //     activity.name.toLowerCase().includes(search) ||
  //     activity.description.toLowerCase().includes(search)
  //   );
  // });

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
      // Liste des activités (à remplacer par API)
  // activities = [
    // Exemple :
    // {
    //   id: 1,
    //   name: "Danse",
    //   description: "Cours de danse pour enfants"
    // }
  // ];

  // Supprimer activité
  // deleteActivity(id: number) {
  //   console.log("Supprimer activité ID:", id);
  //
  }


import { Component, OnInit, signal, computed, ChangeDetectionStrategy } from '@angular/core';
import { Activity } from '../../models/activity.model';
import { ActivityService } from '../../apiServices/activity-service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-view-activities',
  standalone: false,
  templateUrl: './view-activities.html',
  styleUrl: './view-activities.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ViewActivities implements OnInit {

  // Signal pour la liste complète des activités
  activites = signal<Activity[]>([]);

  // Signal pour le filtre de recherche
  recherche = signal('');

  // Activités filtrées calculées automatiquement depuis les deux signals
  activitesFiltrees = computed(() => {
    const q = this.recherche().toLowerCase().trim();
    if (!q) return this.activites();
    return this.activites().filter(a => a.name.toLowerCase().includes(q));
  });

  // Vrai si une recherche est active mais aucun résultat trouvé
  aucunResultat = computed(() =>
    this.recherche().trim().length > 0 &&
    this.activitesFiltrees().length === 0
  );

  constructor(private activityService: ActivityService) {}

  ngOnInit(): void {
    // Charger toutes les activités publiques au démarrage
    this.activityService.getActivities().subscribe({
      next: data => this.activites.set(data),
      error: () => this.activites.set([])
    });
  }

  // Mise à jour du signal de recherche depuis le template
  onRecherche(valeur: string): void {
    this.recherche.set(valeur);
  }

  // Construire l'URL de l'image via le proxy au lieu de localhost en dur
  getImage(a: Activity): string {
    return a.activityImagePath
      ? `${environment.apiUrl}${a.activityImagePath}`
      : 'assets/images/default-activity.jpg';
  }
}

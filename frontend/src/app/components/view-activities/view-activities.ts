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
  activites = signal<Activity[]>([]);
  recherche = signal('');

  activitesFiltrees = computed(() => {
    const q = this.recherche().toLowerCase().trim();
    if (!q) return this.activites();
    return this.activites().filter(a => a.name.toLowerCase().includes(q));
  });

  aucunResultat = computed(() =>
    this.recherche().trim().length > 0 && this.activitesFiltrees().length === 0
  );

  constructor(private activityService: ActivityService) {}

  ngOnInit(): void {
    this.activityService.getActivities().subscribe({
      next: data => this.activites.set(data),
      error: () => this.activites.set([])
    });
  }

  onRecherche(v: string): void {
    this.recherche.set(v);
  }

  getImage(a: Activity): string {
    return a.activityImagePath
      ? `${environment.apiUrl}${a.activityImagePath}`
      : 'assets/images/default-activity.jpg';
  }

  // Intervalle d'age toutes seances confondues
  intervalleAge(a: Activity): string {
    if (!a.sessions || a.sessions.length === 0) return 'Tous ages';
    const mins = a.sessions.map(s => s.minAge).filter((v): v is number => v != null);
    const maxs = a.sessions.map(s => s.maxAge).filter((v): v is number => v != null);
    if (mins.length === 0 && maxs.length === 0) return 'Tous ages';
    const min = mins.length ? Math.min(...mins) : null;
    const max = maxs.length ? Math.max(...maxs) : null;
    if (min !== null && max !== null) return `${min} - ${max} ans`;
    if (min !== null) return `${min}+ ans`;
    if (max !== null) return `Jusqu'a ${max} ans`;
    return 'Tous ages';
  }
}

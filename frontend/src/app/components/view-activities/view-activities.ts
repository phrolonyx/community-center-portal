import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { Activity } from '../../models/activity.model';
import {ActivityService} from '../../apiServices/activity-service';

// Composant Angular dynamique - transformation depuis vue Thymeleaf
@Component({
  selector: 'app-view-activities',
  standalone: false,
  templateUrl: './view-activities.html',
  styleUrl: './view-activities.css',
})
export class ViewActivities implements OnInit {
  activites: Activity[] = [];
  recherche = '';
  aucunResultat = false;

  constructor(private activityService: ActivityService, private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getActivities();
  }

  getActivities() {
    this.activityService.getActivities().subscribe(data => {
      this.activites = data;
      this.cd.detectChanges();
    })
  }

  filtrer(): void {
    const q = this.recherche.toLowerCase();
    const visibles = this.activites.filter(a => a.name.toLowerCase().includes(q)).length;
    this.aucunResultat = visibles === 0 && this.activites.length > 0;
  }

  correspond(a: Activity): boolean {
    if (!this.recherche || this.recherche.trim() === '') return true;
    return a.name.toLowerCase().includes(this.recherche.toLowerCase().trim());
  }


  getImage(a: Activity): string {
    return a.activityImagePath
      ? 'http://localhost:8080/api' + a.activityImagePath
      : 'assets/images/default-activity.jpg';
  }
}

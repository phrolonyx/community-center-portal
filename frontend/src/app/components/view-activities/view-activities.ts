import { Component, OnInit } from '@angular/core';

interface Activite {
  id: number;
  name: string;
  description: string | null;
  activityImagePath: string | null;
}

// Composant Angular dynamique - transformation depuis vue Thymeleaf
@Component({
  selector: 'app-view-activities',
  standalone: false,
  templateUrl: './view-activities.html',
  styleUrl: './view-activities.css',
})
export class ViewActivities implements OnInit {
  activites: Activite[] = [];
  recherche = '';
  aucunResultat = false;

  ngOnInit(): void {
    // Communication avec Spring Boot via API REST (à brancher)
    this.activites = [
      { id: 1, name: 'Soccer Junior', description: 'Initiation au soccer pour 6-12 ans.', activityImagePath: null },
      { id: 2, name: 'Cours de musique', description: 'Découverte des instruments.', activityImagePath: null },
      { id: 3, name: 'Atelier peinture', description: 'Exprimer sa créativité.', activityImagePath: null },
    ];
  }

  filtrer(): void {
    const q = this.recherche.toLowerCase();
    const visibles = this.activites.filter(a => a.name.toLowerCase().includes(q)).length;
    this.aucunResultat = visibles === 0 && this.activites.length > 0;
  }

  correspond(a: Activite): boolean {
    return a.name.toLowerCase().includes(this.recherche.toLowerCase());
  }

  getImage(a: Activite): string {
    return a.activityImagePath
      ? 'http://localhost:8080' + a.activityImagePath
      : 'assets/images/default-activity.jpg';
  }
}

import { Component, OnInit } from '@angular/core';

interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string | null;
}

interface Utilisateur {
  firstName: string;
  lastName: string;
}

// Composant Angular dynamique
@Component({
  selector: 'app-dashboard-parent',
  standalone: false,
  templateUrl: './dashboard-parent.html',
  styleUrl: './dashboard-parent.css',
})
export class DashboardParent implements OnInit {
  utilisateur: Utilisateur = { firstName: 'Parent', lastName: '' };
  enfants: Enfant[] = [];

  mois = ['Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre'];
  moisCourant = new Date().getMonth();
  anneeCourante = new Date().getFullYear();
  semaines: (number | null)[][] = [];

  ngOnInit(): void {
    this.utilisateur = { firstName: 'Ayman', lastName: 'Echaki' };
    this.enfants = [];
    this.genererCalendrier();
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  changeMonth(direction: number): void {
    this.moisCourant += direction;
    if (this.moisCourant > 11) { this.moisCourant = 0; this.anneeCourante++; }
    if (this.moisCourant < 0)  { this.moisCourant = 11; this.anneeCourante--; }
    this.genererCalendrier();
  }

  get titreCalendrier(): string {
    return `Calendrier — ${this.mois[this.moisCourant]} ${this.anneeCourante}`;
  }

  genererCalendrier(): void {
    const debut = (new Date(this.anneeCourante, this.moisCourant, 1).getDay() + 6) % 7;
    const nbJours = new Date(this.anneeCourante, this.moisCourant + 1, 0).getDate();
    const cases: (number | null)[] = Array(debut).fill(null);
    for (let j = 1; j <= nbJours; j++) cases.push(j);
    while (cases.length % 7 !== 0) cases.push(null);
    this.semaines = [];
    for (let i = 0; i < cases.length; i += 7) {
      this.semaines.push(cases.slice(i, i + 7));
    }
  }

  formatNaissance(enfant: Enfant): string {
    return enfant.dateOfBirth ? `Né(e) le ${enfant.dateOfBirth}` : 'Né(e) le N/A';
  }
}

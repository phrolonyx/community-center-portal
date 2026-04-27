import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

interface Seance {
  id: number;
  dateTimeStart: string;
  dateTimeEnd: string;
  minAge: number | null;
  maxAge: number | null;
  activity: { id: number; name: string; description: string | null; activityImagePath: string | null; };
  location?: { name: string };
  host?: { firstName: string; lastName: string };
}
interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  age: number;
}

// Composant Angular dynamique
@Component({
  selector: 'app-activites-parent',
  standalone: false,
  templateUrl: './activites-parent.html',
  styleUrl: './activites-parent.css',
})
export class ActivitesParent implements OnInit {
  seances: Seance[] = [];
  enfants: Enfant[] = [];
  inscriptions = new Set<string>();

  recherche = '';
  aucunResultat = false;

  modalOuvert = false;
  seanceChoisie: Seance | null = null;

  doublonParam = false;
  erreurAgeParam = false;
  inscritParam = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.doublonParam = p['doublon'] !== undefined;
      this.erreurAgeParam = p['erreurAge'] !== undefined;
      this.inscritParam = p['inscrit'] !== undefined;
    });
    // Communication avec Spring Boot via API REST
    this.seances = [];
    this.enfants = [];
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  matchRecherche(s: Seance): boolean {
    return s.activity.name.toLowerCase().includes(this.recherche.toLowerCase());
  }

  onRecherche(): void {
    const visibles = this.seances.filter(s => this.matchRecherche(s)).length;
    this.aucunResultat = this.seances.length > 0 && visibles === 0;
  }

  getImage(s: Seance): string {
    return s.activity.activityImagePath
      ? 'http://localhost:8080' + s.activity.activityImagePath
      : 'assets/images/default-activity.jpg';
  }

  ouvrirModal(s: Seance): void {
    this.seanceChoisie = s;
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  fermerModal(event?: MouseEvent): void {
    if (event && event.target !== event.currentTarget) return;
    this.modalOuvert = false;
    this.seanceChoisie = null;
    document.body.style.overflow = '';
  }

  estEligible(enfant: Enfant): { ok: boolean; raison: string } {
    if (!this.seanceChoisie) return { ok: false, raison: '' };
    const sId = this.seanceChoisie.id;
    if (this.inscriptions.has(`${enfant.id}-${sId}`)) {
      return { ok: false, raison: 'Ce profil est déjà inscrit à cette activité' };
    }
    const min = this.seanceChoisie.minAge;
    const max = this.seanceChoisie.maxAge;
    if ((min !== null && enfant.age < min) || (max !== null && enfant.age > max)) {
      return { ok: false, raison: `Âge requis : ${min}-${max} ans` };
    }
    return { ok: true, raison: '' };
  }

  inscrire(enfant: Enfant): void {
    if (!this.seanceChoisie) return;
    // Consommation API REST via HttpClient
    console.log('Inscrire', enfant.id, 'à séance', this.seanceChoisie.id);
  }

  initiale(nom: string): string {
    return nom.charAt(0).toUpperCase();
  }
}

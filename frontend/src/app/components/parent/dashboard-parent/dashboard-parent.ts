import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ChildService, Child } from '../../../apiServices/child-service';
import { AuthService } from '../../../apiServices/auth.service';
import { RegistrationService, ActivityRegistration } from '../../../apiServices/registration-service';

interface InscJour {
  id: number;
  activityName: string;
  childId: number;
  childFullName: string;
  status: string;
  sessionStart: string;
  locationName: string;
  hostFullName: string | null;
}

interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
}

interface Utilisateur {
  firstName: string;
  lastName: string;
}

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

  inscriptionsParJour = new Map<number, InscJour[]>();

  modalOuvert = false;
  jourSelectionne: number | null = null;

  // Une couleur par enfant
  private palette = ['#3B82F6', '#EC4899', '#10B981', '#F59E0B', '#8B5CF6', '#EF4444', '#14B8A6', '#F97316'];

  constructor(
    private childService: ChildService,
    private authService: AuthService,
    private registrationService: RegistrationService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chargerUtilisateur();
    this.chargerEnfants();
    this.genererCalendrier();
    this.chargerInscriptionsCalendrier();
  }

  // Inscriptions du mois affiche
  chargerInscriptionsCalendrier(): void {
    this.registrationService.getMyRegistrations().subscribe({
      next: (data: ActivityRegistration[]) => {
        this.inscriptionsParJour.clear();
        for (const r of data) {
          if (this.estAnnule(r.status)) continue;
          const d = new Date(r.sessionStart);
          if (d.getMonth() !== this.moisCourant || d.getFullYear() !== this.anneeCourante) continue;
          const j = d.getDate();
          if (!this.inscriptionsParJour.has(j)) this.inscriptionsParJour.set(j, []);
          this.inscriptionsParJour.get(j)!.push({
            id: r.id,
            activityName: r.activityName,
            childId: r.childId,
            childFullName: r.childFullName,
            status: r.status,
            sessionStart: r.sessionStart,
            locationName: r.locationName,
            hostFullName: r.hostFullName
          });
        }
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement calendrier', err)
    });
  }

  inscriptionsDuJour(j: number | null): InscJour[] {
    if (j == null) return [];
    return this.inscriptionsParJour.get(j) || [];
  }

  couleurEnfant(childId: number): string {
    const idx = this.enfants.findIndex(e => e.id === childId);
    if (idx === -1) return '#9CA3AF';
    return this.palette[idx % this.palette.length];
  }

  estAujourdhui(j: number | null): boolean {
    if (j == null) return false;
    const t = new Date();
    return j === t.getDate() && this.moisCourant === t.getMonth() && this.anneeCourante === t.getFullYear();
  }

  estAnnule(s: string): boolean {
    return s === 'ANNULÉ' || s === 'Annulé' || s === 'AnnulÃ©';
  }

  ouvrirJour(j: number | null): void {
    if (j == null) return;
    if (this.inscriptionsDuJour(j).length === 0) return;
    this.jourSelectionne = j;
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  fermerModal(event?: MouseEvent): void {
    if (event && event.target !== event.currentTarget) return;
    this.modalOuvert = false;
    this.jourSelectionne = null;
    document.body.style.overflow = '';
  }

  jourModalLibelle(): string {
    if (this.jourSelectionne == null) return '';
    return `${this.jourSelectionne} ${this.mois[this.moisCourant]} ${this.anneeCourante}`;
  }

  chargerUtilisateur(): void {
    const u = this.authService.currentUser();
    if (u) this.utilisateur = { firstName: u.firstName, lastName: u.lastName };
  }

  chargerEnfants(): void {
    this.childService.getChildren().subscribe({
      next: (data: Child[]) => {
        this.enfants = data.map(c => ({
          id: c.id,
          firstName: c.firstName,
          lastName: c.lastName,
          dateOfBirth: c.dateOfBirth
        }));
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement enfants', err)
    });
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  changeMonth(dir: number): void {
    this.moisCourant += dir;
    if (this.moisCourant > 11) { this.moisCourant = 0; this.anneeCourante++; }
    if (this.moisCourant < 0)  { this.moisCourant = 11; this.anneeCourante--; }
    this.genererCalendrier();
    this.chargerInscriptionsCalendrier();
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

  formatNaissance(e: Enfant): string {
    if (!e.dateOfBirth) return 'Né(e) le N/A';
    return `Né(e) le ${e.dateOfBirth.split('T')[0]}`;
  }
}

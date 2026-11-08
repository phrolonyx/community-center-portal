import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Activity, SessionInfo } from '../../../models/activity.model';
import { ActivityService } from '../../../apiServices/activity-service';
import { ChildService, Child } from '../../../apiServices/child-service';
import { RegistrationService, ActivityRegistration } from '../../../apiServices/registration-service';
import { environment } from '../../../../environments/environment';

interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  age: number;
}

@Component({
  selector: 'app-activites-parent',
  standalone: false,
  templateUrl: './activites-parent.html',
  styleUrl: './activites-parent.css',
})
export class ActivitesParent implements OnInit {
  activites: Activity[] = [];
  enfants: Enfant[] = [];
  inscriptions = new Set<string>();

  recherche = '';
  aucunResultat = false;

  modalOuvert = false;
  activiteChoisie: Activity | null = null;
  seanceChoisie: SessionInfo | null = null;

  doublonParam = false;
  inscritParam = false;

  constructor(
    private route: ActivatedRoute,
    private activityService: ActivityService,
    private childService: ChildService,
    private registrationService: RegistrationService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.doublonParam = p['doublon'] !== undefined;
      this.inscritParam = p['inscrit'] !== undefined;
    });
    this.chargerDonnees();
  }

  chargerDonnees(): void {
    this.activityService.getActivities().subscribe({
      next: (data: Activity[]) => {
        this.activites = data;
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement activites', err)
    });

    this.childService.getChildren().subscribe({
      next: (data: Child[]) => {
        this.enfants = data.map(c => ({ id: c.id, firstName: c.firstName, lastName: c.lastName, age: c.age }));
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement enfants', err)
    });

    // Couples (enfant, seance) deja inscrits pour bloquer doublons
    this.registrationService.getMyRegistrations().subscribe({
      next: (data: ActivityRegistration[]) => {
        this.inscriptions = new Set(
          data.filter(r => !this.estAnnule(r.status))
              .map(r => `${r.childId}-${r.activitySessionId}`)
        );
        this.cd.detectChanges();
      }
    });
  }


  matchRecherche(a: Activity): boolean {
    return a.name.toLowerCase().includes(this.recherche.toLowerCase());
  }

  onRecherche(): void {
    const visibles = this.activites.filter(a => this.matchRecherche(a)).length;
    this.aucunResultat = this.activites.length > 0 && visibles === 0;
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

  ouvrirModal(a: Activity): void {
    this.activiteChoisie = a;
    this.seanceChoisie = null;
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  fermerModal(event?: MouseEvent): void {
    if (event && event.target !== event.currentTarget) return;
    this.modalOuvert = false;
    this.activiteChoisie = null;
    this.seanceChoisie = null;
    document.body.style.overflow = '';
  }

  estDejaInscritSeance(e: Enfant, s: SessionInfo): boolean {
    return this.inscriptions.has(`${e.id}-${s.id}`);
  }

  ageOk(e: Enfant, s: SessionInfo): boolean {
    if (s.minAge != null && e.age < s.minAge) return false;
    if (s.maxAge != null && e.age > s.maxAge) return false;
    return true;
  }

  raisonAgeKo(s: SessionInfo): string {
    return `Age requis : ${s.minAge ?? '?'}-${s.maxAge ?? '?'} ans`;
  }

  estAnnule(status: string): boolean {
    return status === 'ANNULÉ' || status === 'Annulé' || status === 'AnnulÃ©';
  }

  // Inscription directe enfant + seance
  inscrireDirect(e: Enfant, s: SessionInfo): void {
    if (!this.ageOk(e, s)) {
      alert(this.raisonAgeKo(s));
      return;
    }
    this.registrationService.register({ childId: e.id, activitySessionId: s.id }).subscribe({
      next: () => {
        this.inscriptions.add(`${e.id}-${s.id}`);
        this.inscritParam = true;
        this.fermerModal();
        this.cd.detectChanges();
      },
      error: (err) => alert('Erreur inscription : ' + (err.error?.message || err.message))
    });
  }
}

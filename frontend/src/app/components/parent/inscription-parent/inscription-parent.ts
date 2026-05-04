import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChildService, Child } from '../../../apiServices/child-service';
import { ActivitySessionService, ActivitySession as SessionDto } from '../../../apiServices/activity-session-service';
import { RegistrationService, ActivityRegistration } from '../../../apiServices/registration-service';

interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
}

interface Inscription {
  id: number;
  status: string;
  child: Enfant;
  activitySession: {
    dateTimeStart: string;
    location?: { name: string };
    activity: { name: string };
    hostFullName?: string | null;
  };
}

@Component({
  selector: 'app-inscription-parent',
  standalone: false,
  templateUrl: './inscription-parent.html',
  styleUrl: './inscription-parent.css',
})
export class InscriptionParent implements OnInit {
  enfants: Enfant[] = [];
  inscriptions: Inscription[] = [];

  seancesDisponibles: SessionDto[] = [];
  enfantSelectionne: number | null = null;
  seanceSelectionnee: number | null = null;

  messageInscrit = false;
  messageAnnule = false;

  constructor(
    private route: ActivatedRoute,
    private childService: ChildService,
    private sessionService: ActivitySessionService,
    private registrationService: RegistrationService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.messageInscrit = p['inscrit'] !== undefined;
      this.messageAnnule = p['annule'] !== undefined;
    });
    this.chargerDonnees();
  }

  chargerDonnees(): void {
    this.childService.getChildren().subscribe(data => {
      this.enfants = data.map(c => ({ id: c.id, firstName: c.firstName, lastName: c.lastName }));
      this.cd.detectChanges();
    });

    this.sessionService.getAllSessions().subscribe(data => {
      this.seancesDisponibles = data;
      this.cd.detectChanges();
    });

    this.chargerInscriptions();
  }

  // Liaison Enfant-Seance
  chargerInscriptions(): void {
    this.registrationService.getMyRegistrations().subscribe((data: ActivityRegistration[]) => {
      this.inscriptions = data.map(r => ({
        id: r.id,
        status: r.status,
        child: {
          id: r.childId,
          firstName: r.childFullName.split(' ')[0] || '',
          lastName: r.childFullName.split(' ').slice(1).join(' ') || ''
        },
        activitySession: {
          dateTimeStart: r.sessionStart,
          location: { name: r.locationName },
          activity: { name: r.activityName },
          hostFullName: r.hostFullName
        }
      }));
      this.cd.detectChanges();
    });
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  inscriptionsParEnfant(e: Enfant): Inscription[] {
    return this.inscriptions.filter(i => i.child && i.child.id === e.id && !this.estAnnule(i.status));
  }

  estConfirme(s: string): boolean { return s === 'CONFIRMÉ' || s === 'Confirmé' || s === 'CONFIRMÃ‰'; }
  estEnAttente(s: string): boolean { return s === 'EN_ATTENTE' || s === 'En attente'; }
  estAnnule(s: string): boolean { return s === 'ANNULÉ' || s === 'Annulé' || s === 'AnnulÃ©'; }

  formatStatus(s: string): string {
    if (this.estConfirme(s)) return 'CONFIRMÉ';
    if (this.estEnAttente(s)) return 'EN ATTENTE';
    if (this.estAnnule(s)) return 'ANNULÉ';
    return s;
  }

  // Inscription seance
  inscrire(): void {
    if (!this.enfantSelectionne || !this.seanceSelectionnee) {
      alert('Veuillez choisir un enfant et une seance');
      return;
    }
    this.registrationService.register({ childId: this.enfantSelectionne, activitySessionId: this.seanceSelectionnee }).subscribe({
      next: () => {
        this.messageInscrit = true;
        this.enfantSelectionne = null;
        this.seanceSelectionnee = null;
        this.chargerInscriptions();
      },
      error: (err) => alert('Erreur inscription : ' + (err.error?.message || err.message))
    });
  }

  // Annulation seance
  annulerInscription(id: number): void {
    if (!confirm('Voulez-vous vraiment annuler cette inscription ?')) return;
    this.registrationService.cancel(id).subscribe({
      next: () => {
        this.messageAnnule = true;
        this.chargerInscriptions();
      },
      error: (err) => alert('Erreur annulation : ' + (err.error?.message || err.message))
    });
  }
}

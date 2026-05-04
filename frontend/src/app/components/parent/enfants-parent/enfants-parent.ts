import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChildService, Child } from '../../../apiServices/child-service';
import { RegistrationService, ActivityRegistration } from '../../../apiServices/registration-service';

interface MedCondition { condition: string; }
interface Activity { name: string; }
interface Seance { dateTimeStart: string; activity: Activity; }
interface Inscription { activitySession: Seance; status: string; }

interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  age: number;
  medicalConditions: MedCondition[];
  registrations: Inscription[];
}

interface Formulaire {
  childId: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  relationship: string;
  medical: string;
}

@Component({
  selector: 'app-enfants-parent',
  standalone: false,
  templateUrl: './enfants-parent.html',
  styleUrl: './enfants-parent.css',
})
export class EnfantsParent implements OnInit {
  enfants: Enfant[] = [];
  ajoutParam = false;
  modifParam = false;
  supprParam = false;

  modalOuvert = false;
  modeAjout: 'add' | 'edit' = 'add';
  dateMax = new Date().toISOString().split('T')[0];
  formulaire: Formulaire = { childId: '', firstName: '', lastName: '', dateOfBirth: '', relationship: '', medical: '' };

  constructor(
    private route: ActivatedRoute,
    private childService: ChildService,
    private registrationService: RegistrationService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.ajoutParam = p['ajout'] !== undefined;
      this.modifParam = p['modif'] !== undefined;
      this.supprParam = p['suppr'] !== undefined;
    });
    this.chargerEnfants();
  }

  // Liaison Parent-Enfant
  chargerEnfants(): void {
    this.childService.getChildren().subscribe({
      next: (data: Child[]) => {
        this.enfants = data.map(c => ({
          id: c.id,
          firstName: c.firstName,
          lastName: c.lastName,
          dateOfBirth: c.dateOfBirth || '',
          age: c.age,
          medicalConditions: (c.medicalConditions || []).map(m => ({ condition: m.condition || '' })),
          registrations: []
        }));
        this.chargerInscriptions();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement enfants', err)
    });
  }

  // Inscriptions actives par enfant
  chargerInscriptions(): void {
    this.registrationService.getMyRegistrations().subscribe({
      next: (data: ActivityRegistration[]) => {
        for (const e of this.enfants) {
          e.registrations = data
            .filter(r => r.childId === e.id && !this.estAnnule(r.status))
            .map(r => ({
              activitySession: {
                dateTimeStart: r.sessionStart,
                activity: { name: r.activityName }
              },
              status: r.status
            }));
        }
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement inscriptions', err)
    });
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  ouvrirAjout(): void {
    this.modeAjout = 'add';
    this.formulaire = { childId: '', firstName: '', lastName: '', dateOfBirth: '', relationship: '', medical: '' };
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  ouvrirModif(e: Enfant): void {
    this.modeAjout = 'edit';
    this.formulaire = {
      childId: String(e.id),
      firstName: e.firstName,
      lastName: e.lastName,
      dateOfBirth: e.dateOfBirth ? e.dateOfBirth.split('T')[0] : '',
      relationship: 'Père',
      medical: e.medicalConditions[0]?.condition || '',
    };
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  fermerModal(event?: MouseEvent): void {
    if (event && event.target !== event.currentTarget) return;
    this.modalOuvert = false;
    document.body.style.overflow = '';
  }

  // Ajout / modification enfant
  envoyer(): void {
    const dateSeule = this.formulaire.dateOfBirth ? this.formulaire.dateOfBirth.split('T')[0] : null;
    const dateIso = dateSeule ? `${dateSeule}T00:00:00` : null;

    const data: any = {
      firstName: this.formulaire.firstName,
      lastName: this.formulaire.lastName,
      dateOfBirth: dateIso,
      medical: this.formulaire.medical || undefined,
      relationship: this.formulaire.relationship || 'Père',
      parentId: 0
    };

    if (this.modeAjout === 'add') {
      this.childService.createChild(data).subscribe({
        next: () => {
          this.fermerModal();
          this.ajoutParam = true;
          this.chargerEnfants();
        },
        error: (err) => alert('Erreur lors de l\'ajout : ' + (err.error?.message || err.message))
      });
    } else {
      this.childService.updateChild(Number(this.formulaire.childId), data).subscribe({
        next: () => {
          this.fermerModal();
          this.modifParam = true;
          this.chargerEnfants();
        },
        error: (err) => alert('Erreur lors de la modification : ' + (err.error?.message || err.message))
      });
    }
  }

  supprimer(id: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce profil ?')) return;
    this.childService.deleteChild(id).subscribe({
      next: () => {
        this.supprParam = true;
        this.chargerEnfants();
      },
      error: (err) => alert('Erreur lors de la suppression : ' + (err.error?.message || err.message))
    });
  }

  estConfirme(s: string): boolean { return s === 'CONFIRMÉ' || s === 'Confirmé'; }
  estEnAttente(s: string): boolean { return s === 'EN_ATTENTE' || s === 'En attente'; }
  estAnnule(s: string): boolean { return s === 'ANNULÉ' || s === 'Annulé' || s === 'AnnulÃ©'; }

  formatStatus(s: string): string {
    if (this.estConfirme(s)) return 'CONFIRMÉ';
    if (this.estEnAttente(s)) return 'EN ATTENTE';
    if (this.estAnnule(s)) return 'ANNULÉ';
    return s;
  }
}

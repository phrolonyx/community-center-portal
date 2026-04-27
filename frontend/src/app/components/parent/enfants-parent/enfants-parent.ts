import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

interface MedicalCondition { condition: string; }
interface Activity { name: string; }
interface ActivitySession { dateTimeStart: string; activity: Activity; }
interface Registration { activitySession: ActivitySession; status: string; }
interface Enfant {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  age: number;
  medicalConditions: MedicalCondition[];
  registrations: Registration[];
}

interface Formulaire {
  childId: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  relationship: string;
  medical: string;
}

// Composant Angular dynamique
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

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.ajoutParam = p['ajout'] !== undefined;
      this.modifParam = p['modif'] !== undefined;
      this.supprParam = p['suppr'] !== undefined;
    });
    // Consommation API REST via HttpClient
    this.enfants = [];
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

  ouvrirModif(enfant: Enfant): void {
    this.modeAjout = 'edit';
    this.formulaire = {
      childId: String(enfant.id),
      firstName: enfant.firstName,
      lastName: enfant.lastName,
      dateOfBirth: enfant.dateOfBirth,
      relationship: '',
      medical: enfant.medicalConditions[0]?.condition || '',
    };
    this.modalOuvert = true;
    document.body.style.overflow = 'hidden';
  }

  fermerModal(event?: MouseEvent): void {
    if (event && event.target !== event.currentTarget) return;
    this.modalOuvert = false;
    document.body.style.overflow = '';
  }

  // Communication avec Spring Boot via API REST
  envoyer(): void {
    console.log(this.modeAjout, this.formulaire);
  }

  supprimer(childId: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce profil ?')) return;
    console.log('Supprimer enfant', childId);
  }

  estConfirme(s: string): boolean { return s === 'CONFIRMÉ' || s === 'Confirmé'; }
  estEnAttente(s: string): boolean { return s === 'EN_ATTENTE' || s === 'En attente'; }
  estAnnule(s: string): boolean { return s === 'ANNULÉ' || s === 'Annulé'; }
}

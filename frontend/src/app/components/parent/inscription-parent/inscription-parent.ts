import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
  };
}

// Composant Angular dynamique
@Component({
  selector: 'app-inscription-parent',
  standalone: false,
  templateUrl: './inscription-parent.html',
  styleUrl: './inscription-parent.css',
})
export class InscriptionParent implements OnInit {
  enfants: Enfant[] = [];
  inscriptions: Inscription[] = [];
  messageInscrit = false;
  messageAnnule = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.messageInscrit = p['inscrit'] !== undefined;
      this.messageAnnule = p['annule'] !== undefined;
    });
    // Communication avec Spring Boot via API REST
    this.enfants = [];
    this.inscriptions = [];
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  inscriptionsParEnfant(enfant: Enfant): Inscription[] {
    return this.inscriptions.filter(i =>
      i.child && i.child.id === enfant.id &&
      i.status !== 'Annulé' && i.status !== 'ANNULÉ'
    );
  }

  estConfirme(status: string): boolean {
    return status === 'CONFIRMÉ' || status === 'Confirmé';
  }

  estEnAttente(status: string): boolean {
    return status === 'EN_ATTENTE' || status === 'En attente';
  }

  estAnnule(status: string): boolean {
    return status === 'ANNULÉ' || status === 'Annulé';
  }

  annulerInscription(id: number): void {
    if (!confirm('Voulez-vous vraiment annuler cette inscription ?')) return;
    console.log('Annuler inscription', id);
  }
}

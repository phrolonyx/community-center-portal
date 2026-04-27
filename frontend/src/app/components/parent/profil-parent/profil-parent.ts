import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

interface Profil {
  prenom: string;
  nom: string;
  courriel: string;
  telephone: string;
  adresse: string;
  ville: string;
  province: string;
  urgenceNom: string;
  urgenceRelation: string;
  urgenceTelephone: string;
}

// Composant Angular dynamique pour le profil du parent
@Component({
  selector: 'app-profil-parent',
  standalone: false,
  templateUrl: './profil-parent.html',
  styleUrl: './profil-parent.css',
})
export class ProfilParent implements OnInit {
  profil: Profil = {
    prenom: '',
    nom: '',
    courriel: '',
    telephone: '',
    adresse: '',
    ville: '',
    province: '',
    urgenceNom: '',
    urgenceRelation: '',
    urgenceTelephone: '',
  };

  sauvegarde: Profil | null = null;
  enModeEdition = false;
  modifReussie = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((p) => {
      this.modifReussie = p['modif'] !== undefined;
    });

    this.profil = {
      prenom: 'Aymane',
      nom: 'Chaki',
      courriel: 'aymanechaki10@gmail.com',
      telephone: '',
      adresse: '',
      ville: '',
      province: '',
      urgenceNom: '',
      urgenceRelation: '',
      urgenceTelephone: '',
    };
  }

  toggleSidebar(): void {
    document.querySelector('.sidebar')?.classList.toggle('open');
    document.querySelector('.sidebar-overlay')?.classList.toggle('open');
  }

  afficher(valeur: string): string {
    return valeur && valeur.length > 0 ? valeur : 'Non renseigné';
  }

  modifier(): void {
    this.sauvegarde = { ...this.profil };
    this.enModeEdition = true;
  }

  annuler(): void {
    if (this.sauvegarde) {
      this.profil = { ...this.sauvegarde };
    }
    this.enModeEdition = false;
  }

  // Communication avec Spring Boot via API REST
  enregistrer(): void {
    console.log('Profil enregistré', this.profil);
    this.enModeEdition = false;
  }
}

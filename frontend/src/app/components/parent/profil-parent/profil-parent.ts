import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ParentService, ParentProfile } from '../../../apiServices/parent-service';
import { AuthService } from '../../../apiServices/auth.service';

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

@Component({
  selector: 'app-profil-parent',
  standalone: false,
  templateUrl: './profil-parent.html',
  styleUrl: './profil-parent.css',
})
export class ProfilParent implements OnInit {
  profil: Profil = {
    prenom: '', nom: '', courriel: '',
    telephone: '', adresse: '', ville: '', province: '',
    urgenceNom: '', urgenceRelation: '', urgenceTelephone: ''
  };

  private profilId: number | null = null;
  sauvegarde: Profil | null = null;
  enModeEdition = false;
  modifReussie = false;

  constructor(
    private route: ActivatedRoute,
    private parentService: ParentService,
    private authService: AuthService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(p => {
      this.modifReussie = p['modif'] !== undefined;
    });
    this.chargerProfil();
  }

  // Profil parent
  chargerProfil(): void {
    const u = this.authService.currentUser();
    if (u) {
      this.profil.prenom = u.firstName;
      this.profil.nom = u.lastName;
      this.profil.courriel = u.email;
    }

    this.parentService.getProfile().subscribe({
      next: (data: ParentProfile) => {
        this.profilId = data.id;
        this.profil.telephone = data.phoneNumber || '';
        this.profil.adresse = data.addressLine1 || '';
        this.profil.ville = data.city || '';
        this.profil.province = data.province || '';
        this.profil.urgenceNom = data.emergencyName || '';
        this.profil.urgenceRelation = data.emergencyRelationship || '';
        this.profil.urgenceTelephone = data.emergencyPhone || '';
        this.cd.detectChanges();
      },
      error: (err) => console.error('Erreur chargement profil', err)
    });
  }


  afficher(v: string): string {
    return v && v.length > 0 ? v : 'Non renseigné';
  }

  modifier(): void {
    this.sauvegarde = { ...this.profil };
    this.enModeEdition = true;
  }

  annuler(): void {
    if (this.sauvegarde) this.profil = { ...this.sauvegarde };
    this.enModeEdition = false;
  }

  enregistrer(): void {
    const data = {
      firstName: this.profil.prenom,
      lastName: this.profil.nom,
      email: this.profil.courriel,
      phoneNumber: this.profil.telephone,
      addressLine1: this.profil.adresse,
      city: this.profil.ville,
      province: this.profil.province,
      emergencyName: this.profil.urgenceNom,
      emergencyPhone: this.profil.urgenceTelephone,
      emergencyRelationship: this.profil.urgenceRelation
    };

    this.parentService.updateProfile(data).subscribe({
      next: () => {
        this.modifReussie = true;
        this.enModeEdition = false;
        this.chargerProfil();
        this.cd.detectChanges();
      },
      error: (err) => alert('Erreur lors de la modification : ' + (err.error?.message || err.message))
    });
  }
}

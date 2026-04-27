import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

interface Formulaire {
  prenom: string;
  nom: string;
  email: string;
  mdp: string;
  confirm: string;
  tel: string;
  adresse: string;
  ville: string;
  province: string;
  codePostal: string;
  role: string;
  urgenceNom: string;
  urgenceRelation: string;
  urgenceTel: string;
}

// Composant Angular dynamique
@Component({
  selector: 'app-inscription',
  standalone: false,
  templateUrl: './inscription.html',
  styleUrl: './inscription.css',
})
export class Inscription {
  form: Formulaire = {
    prenom: '', nom: '', email: '', mdp: '', confirm: '',
    tel: '', adresse: '', ville: '', province: '', codePostal: '',
    role: '', urgenceNom: '', urgenceRelation: '', urgenceTel: '',
  };
  showMdp = false;
  showConfirm = false;
  serverError = '';
  clientError = false;

  constructor(private router: Router, private route: ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      if (params['erreur']) {
        this.serverError = params['erreur'];
      }
    });
  }

  toggleMdp(champ: string) {
    if (champ === 'mdp') this.showMdp = !this.showMdp;
    else this.showConfirm = !this.showConfirm;
  }

  emailOk(v: string) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.trim());
  }

  telOk(v: string) {
    return /^\d{3}-\d{3}-\d{4}$/.test(v.trim());
  }

  postalOk(v: string) {
    return /^[A-Za-z]\d[A-Za-z][\s-]?\d[A-Za-z]\d$/.test(v.trim());
  }

  formulaireValide() {
    return this.form.prenom.trim().length >= 2
      && this.form.nom.trim().length >= 2
      && this.emailOk(this.form.email)
      && this.form.mdp.length >= 8
      && this.form.confirm === this.form.mdp
      && this.telOk(this.form.tel)
      && this.form.adresse.trim().length >= 2
      && this.form.ville.trim().length >= 2
      && this.form.province.trim().length >= 2
      && this.postalOk(this.form.codePostal)
      && this.form.role.length > 0;
  }

  // Communication avec Spring Boot via API REST
  onSubmit() {
    if (!this.formulaireValide()) {
      this.clientError = true;
      return;
    }
    this.router.navigate(['/connexion'], { queryParams: { inscrit: '1' } });
  }
}

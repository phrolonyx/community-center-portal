import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';
 
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
  isLoading = false;
 
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}
 
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
 
  onSubmit() {
    if (!this.formulaireValide()) {
      this.clientError = true;
      return;
    }
 
    this.clientError = false;
    this.serverError = '';
    this.isLoading = true;
 
    // Payload aligné exactement sur les champs de RegisterRequest.java
    const payload = {
      prenom: this.form.prenom.trim(),
      nom: this.form.nom.trim(),
      email: this.form.email.trim(),
      password: this.form.mdp,
      confirmPassword: this.form.confirm,
      telephone: this.form.tel.trim(),
      adresse: this.form.adresse.trim(),
      ville: this.form.ville.trim(),
      province: this.form.province.trim(),
      postalCode: this.form.codePostal.trim(),
      role: this.form.role,
      urgenceNom: this.form.urgenceNom.trim() || null,
      urgenceRelation: this.form.urgenceRelation.trim() || null,
      urgenceTel: this.form.urgenceTel.trim() || null,
    };
 
    this.authService.register(payload).subscribe({
      next: () => {
        // Inscription réussie — rediriger vers connexion avec message de confirmation
        this.isLoading = false;
        this.router.navigate(['/connexion'], { queryParams: { inscrit: '1' } });
      },
      error: (err) => {
        // Afficher le message d'erreur retourné par le backend
        this.isLoading = false;
        this.serverError = err?.error?.message || 'Une erreur est survenue. Veuillez réessayer.';
      }
    });
  }
}
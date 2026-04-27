import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// Composant Angular dynamique - Transformation depuis vue Thymeleaf
@Component({
  selector: 'app-connexion',
  standalone: false,
  templateUrl: './connexion.html',
  styleUrl: './connexion.css',
})
export class Connexion implements OnInit {
  email = '';
  mdp = '';
  voirMdp = false;
  erreur = false;
  inscrit = false;
  deconnecte = false;
  erreurForm = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.erreur = params['error'] !== undefined;
      this.inscrit = params['inscrit'] !== undefined;
      this.deconnecte = params['logout'] !== undefined;
    });
  }

  toggleMdp(): void {
    this.voirMdp = !this.voirMdp;
  }

  formValide(): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(this.email.trim()) && this.mdp.length >= 8;
  }

  onSubmit(): void {
    if (!this.formValide()) {
      this.erreurForm = true;
      return;
    }
    this.erreurForm = false;
    console.log('Login:', this.email);
  }
}

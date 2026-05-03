import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';
 
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
  isLoading = false;
 
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}
 
  ngOnInit(): void {
    // Lire les paramètres de l'URL pour afficher les messages
    this.route.queryParams.subscribe(params => {
      this.inscrit = params['inscrit'] !== undefined;
      this.deconnecte = params['logout'] !== undefined;
    });
 
    // Rediriger si l'utilisateur est déjà connecté
    if (this.authService.isAuthenticated()) {
      this.redirigerSelonRole();
    }
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
    this.erreur = false;
    this.isLoading = true;
 
    // Appel à l'API de connexion via AuthService
    this.authService.login(this.email.trim(), this.mdp).subscribe({
      next: () => {
        // Connexion réussie — rediriger selon le rôle de l'utilisateur
        this.isLoading = false;
        this.redirigerSelonRole();
      },
      error: () => {
        // Échec — mauvais identifiants ou compte non approuvé
        this.isLoading = false;
        this.erreur = true;
      }
    });
  }
 
  // Redirection vers le dashboard correspondant au rôle de l'utilisateur connecté
  private redirigerSelonRole(): void {
    if (this.authService.hasRole('ADMIN')) {
      this.router.navigate(['/admin/dashboard']);
    } else if (this.authService.hasRole('PARENT')) {
      this.router.navigate(['/dashboard-parent']);
    } else if (this.authService.hasRole('ANIMATEUR')) {
      this.router.navigate(['/animateur/sceances']);
    } else {
      this.router.navigate(['/']);
    }
  }
}
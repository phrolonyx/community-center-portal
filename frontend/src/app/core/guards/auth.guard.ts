import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';

// Vérifie que l'utilisateur est connecté avant d'accéder à une route protégée.
// Si non connecté, redirige vers la page de connexion.
export const AuthGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  // Redirection vers connexion si aucune session active
  return router.createUrlTree(['/connexion']);
};

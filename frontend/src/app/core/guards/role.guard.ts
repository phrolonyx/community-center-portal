import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../../apiServices/auth.service';

// Vérifie que l'utilisateur connecté possède le rôle requis par la route.
// Le rôle attendu est défini dans data: { role: 'PARENT' } dans app-routing-module.ts.
// Si le rôle ne correspond pas, redirige vers l'accueil.
export const RoleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Récupérer le rôle requis depuis les données de la route
  const requiredRole = route.data['role'] as string;

  if (authService.hasRole(requiredRole)) {
    return true;
  }

  // Redirection vers l'accueil si le rôle ne correspond pas
  return router.createUrlTree(['/']);
};

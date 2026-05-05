import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';

// Composants publics (aucun guard)
import { Accueil } from './components/accueil/accueil';
import { Connexion } from './components/connexion/connexion';
import { Inscription } from './components/inscription/inscription';
import { ViewActivities } from './components/view-activities/view-activities';

// Composants parent
import { DashboardParent } from './components/parent/dashboard-parent/dashboard-parent';
import { ActivitesParent } from './components/parent/activites-parent/activites-parent';
import { EnfantsParent } from './components/parent/enfants-parent/enfants-parent';
import { InscriptionParent } from './components/parent/inscription-parent/inscription-parent';
import { MessagesParent } from './components/parent/messages-parent/messages-parent';
import { ProfilParent } from './components/parent/profil-parent/profil-parent';

// Composants admin
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';
import { EnfantsAdmin } from './components/admin/enfants-admin/enfants-admin';
import { ActivitesAdmin } from './components/admin/activites-admin/activites-admin';
import { SceancesAdmin } from './components/admin/sceances-admin/sceances-admin';
import { FormActiviteAdmin } from './components/admin/form-activite-admin/form-activite-admin';
import { FormSceanceAdmin } from './components/admin/form-sceance-admin/form-sceance-admin';
import { MessagesAdmin } from './components/admin/messages-admin/messages-admin';
import { InboxAdmin } from './components/admin/inbox-admin/inbox-admin';
import { MessagesEnvoyesAdmin } from './components/admin/messages-envoyes-admin/messages-envoyes-admin';

// Composants animateur
import { EnfantsAnimateur } from './components/animateur/enfants-animateur/enfants-animateur';
import { SceancesAnimateur } from './components/animateur/sceances-animateur/sceances-animateur';
import { SceancesDetailsAnimateur } from './components/animateur/sceances-details-animateur/sceances-details-animateur';
import { MessagesAnimateur } from './components/animateur/messages-animateur/messages-animateur';
import { InboxAnimateur } from './components/animateur/inbox-animateur/inbox-animateur';
import { MessagesEnvoyesAnimateur } from './components/animateur/messages-envoyes-animateur/messages-envoyes-animateur';

const routes: Routes = [
  // Pages publiques — accessibles sans connexion
  { path: '', component: Accueil },
  { path: 'connexion', component: Connexion },
  { path: 'inscription', component: Inscription },
  { path: 'view-activities', component: ViewActivities },

  // Pages parent — AuthGuard vérifie la connexion, RoleGuard vérifie le rôle PARENT
  {
    path: 'dashboard-parent',
    component: DashboardParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },
  {
    path: 'dashboard-parent/activites',
    component: ActivitesParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },
  {
    path: 'dashboard-parent/enfants',
    component: EnfantsParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },
  {
    path: 'dashboard-parent/inscriptions',
    component: InscriptionParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },
  {
    path: 'dashboard-parent/messages',
    component: MessagesParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },
  {
    path: 'dashboard-parent/profil',
    component: ProfilParent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PARENT' }
  },

  // Pages admin — AuthGuard vérifie la connexion, RoleGuard vérifie le rôle ADMIN
  {
    path: 'admin/dashboard',
    component: DashboardAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/users',
    component: UsersAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/enfants',
    component: EnfantsAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/activites',
    component: ActivitesAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/form-activite-admin/:id',
    component: FormActiviteAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },

  {
    path: 'admin/sceances',
    component: SceancesAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/form-sceances-admin/:id',
    component: FormSceanceAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/form-activite-admin',
    component: FormActiviteAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/form-sceances-admin',
    component: FormSceanceAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/messages',
    component: MessagesAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/inbox',
    component: InboxAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'admin/messages-envoyes',
    component: MessagesEnvoyesAdmin,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' }
  },

  // Pages animateur — AuthGuard vérifie la connexion, RoleGuard vérifie le rôle ANIMATEUR
  {
    path: 'animateur/enfants',
    component: EnfantsAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },
  {
    path: 'animateur/sceances',
    component: SceancesAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },
  {
    path: 'animateur/sceances/details/:id',
    component: SceancesDetailsAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },
  {
    path: 'animateur/messages',
    component: MessagesAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },
  {
    path: 'animateur/inbox',
    component: InboxAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },
  {
    path: 'animateur/messages-envoyes',
    component: MessagesEnvoyesAnimateur,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ANIMATEUR' }
  },

  // Redirection par défaut si la route n'existe pas
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

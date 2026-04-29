import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Accueil } from './components/accueil/accueil';
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';
import { Connexion } from './components/connexion/connexion';
import { Inscription } from './components/inscription/inscription';
import { ViewActivities } from './components/view-activities/view-activities';
import { DashboardParent } from './components/parent/dashboard-parent/dashboard-parent';
import { InscriptionParent } from './components/parent/inscription-parent/inscription-parent';
import { ActivitesParent } from './components/parent/activites-parent/activites-parent';
import { EnfantsParent } from './components/parent/enfants-parent/enfants-parent';
import { MessagesParent } from './components/parent/messages-parent/messages-parent';
import { ProfilParent } from './components/parent/profil-parent/profil-parent';
import { EnfantsAdmin } from './components/admin/enfants-admin/enfants-admin';
import { ActivitesAdmin } from './components/admin/activites-admin/activites-admin';
import { SceancesAdmin } from './components/admin/sceances-admin/sceances-admin';
import { FormActiviteAdmin } from './components/admin/form-activite-admin/form-activite-admin';
import { FormSceanceAdmin } from './components/admin/form-sceance-admin/form-sceance-admin';
import { EnfantsAnimateur } from './components/animateur/enfants-animateur/enfants-animateur';
import { SceancesAnimateur } from './components/animateur/sceances-animateur/sceances-animateur';
import { SceancesDetailsAnimateur } from './components/animateur/sceances-details-animateur/sceances-details-animateur';


const routes: Routes = [
  { path: '', component: Accueil },
  { path: 'connexion', component: Connexion },
  { path: 'inscription', component: Inscription },
  { path: 'view-activities', component: ViewActivities },
  { path: 'dashboard-parent', component: DashboardParent },
  { path: 'dashboard-parent/activites', component: ActivitesParent },
  { path: 'dashboard-parent/enfants', component: EnfantsParent },
  { path: 'dashboard-parent/inscriptions', component: InscriptionParent },
  { path: 'dashboard-parent/messages', component: MessagesParent },
  { path: 'dashboard-parent/profil', component: ProfilParent },
  { path: 'admin/users', component: UsersAdmin },
  { path: 'admin/dashboard', component: DashboardAdmin },
  { path: 'admin/enfants', component: EnfantsAdmin },
  { path: 'admin/activites', component: ActivitesAdmin },
  { path: 'admin/sceances', component: SceancesAdmin },
  { path: 'admin/form-activite-admin', component: FormActiviteAdmin },
  { path: 'admin/form-sceances-admin', component: FormSceanceAdmin },
  { path: 'animateur/enfants', component: EnfantsAnimateur},
  { path: 'animateur/sceances', component: SceancesAnimateur},
  { path: 'animateur/sceances/details', component: SceancesDetailsAnimateur}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

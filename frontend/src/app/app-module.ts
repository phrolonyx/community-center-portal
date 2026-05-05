import { NgModule, provideBrowserGlobalErrorListeners, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
 
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Accueil } from './components/accueil/accueil';
import { Header } from './components/header/header';
import { Footer } from './components/footer/footer';
import { FormActiviteAdmin } from './components/admin/form-activite-admin/form-activite-admin';
import { ActivitesAdmin } from './components/admin/activites-admin/activites-admin';
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { EnfantsAdmin } from './components/admin/enfants-admin/enfants-admin';
import { MessagesAdmin } from './components/admin/messages-admin/messages-admin';
import { InboxAdmin } from './components/admin/inbox-admin/inbox-admin';
import { MessagesEnvoyesAdmin } from './components/admin/messages-envoyes-admin/messages-envoyes-admin';
import { FormSceanceAdmin } from './components/admin/form-sceance-admin/form-sceance-admin';
import { SceancesAdmin } from './components/admin/sceances-admin/sceances-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';
import { Connexion } from './components/connexion/connexion';
import { Inscription } from './components/inscription/inscription';
import { ViewActivities } from './components/view-activities/view-activities';
import { ParentSidebar } from './components/parent/parent-sidebar/parent-sidebar';
import { DashboardParent } from './components/parent/dashboard-parent/dashboard-parent';
import { InscriptionParent } from './components/parent/inscription-parent/inscription-parent';
import { ActivitesParent } from './components/parent/activites-parent/activites-parent';
import { EnfantsParent } from './components/parent/enfants-parent/enfants-parent';
import { MessagesParent } from './components/parent/messages-parent/messages-parent';
import { ProfilParent } from './components/parent/profil-parent/profil-parent';
import { AdminSidebar } from './components/admin/admin-sidebar/admin-sidebar';
import { AdminHeader } from './components/admin/admin-header/admin-header';
import { EnfantsAnimateur } from './components/animateur/enfants-animateur/enfants-animateur';
import { SceancesAnimateur } from './components/animateur/sceances-animateur/sceances-animateur';
import { AnimateurSidebar } from './components/animateur/animateur-sidebar/animateur-sidebar';
import { AnimateurHeader } from './components/animateur/animateur-header/animateur-header';
import { SceancesDetailsAnimateur } from './components/animateur/sceances-details-animateur/sceances-details-animateur';
import { MessagesAnimateur } from './components/animateur/messages-animateur/messages-animateur';
import { InboxAnimateur } from './components/animateur/inbox-animateur/inbox-animateur';
import { MessagesEnvoyesAnimateur } from './components/animateur/messages-envoyes-animateur/messages-envoyes-animateur';
import { AuthService } from './apiServices/auth.service';
import { catchError, of } from 'rxjs';
 
// Fonction exécutée avant le démarrage du routing — attend que me() soit résolu
function initAuth(authService: AuthService) {
  return () => authService.me().pipe(
    // Ignorer l'erreur 401 (pas de session) — l'app démarre quand même
    catchError(() => of(null))
  );
}
 
@NgModule({
  declarations: [
    App,
    Accueil,
    Header,
    Footer,
    FormActiviteAdmin,
    ActivitesAdmin,
    DashboardAdmin,
    EnfantsAdmin,
    MessagesAdmin,
    InboxAdmin,
    MessagesEnvoyesAdmin,
    FormSceanceAdmin,
    SceancesAdmin,
    UsersAdmin,
    Connexion,
    Inscription,
    ViewActivities,
    ParentSidebar,
    DashboardParent,
    InscriptionParent,
    ActivitesParent,
    EnfantsParent,
    MessagesParent,
    ProfilParent,
    AdminSidebar,
    AdminHeader,
    EnfantsAnimateur,
    SceancesAnimateur,
    AnimateurSidebar,
    AnimateurHeader,
    SceancesDetailsAnimateur,
    MessagesAnimateur,
    InboxAnimateur,
    MessagesEnvoyesAnimateur,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(),
    {
      // APP_INITIALIZER bloque le démarrage du routing jusqu'à ce que me() soit résolu.
      // Ainsi les guards ont toujours accès à currentUser au moment de la navigation.
      provide: APP_INITIALIZER,
      useFactory: initAuth,
      deps: [AuthService],
      multi: true
    }
  ],
  bootstrap: [App],
})
export class AppModule {}
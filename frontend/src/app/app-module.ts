import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
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
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners(), provideHttpClient()],
  bootstrap: [App],
})
export class AppModule {}

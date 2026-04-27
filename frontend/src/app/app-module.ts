import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

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
import { AdminSidebar } from './components/admin/admin-sidebar/admin-sidebar';
import { AdminHeader } from './components/admin/admin-header/admin-header';
import { AdminLayout } from './components/admin/admin-layout/admin-layout';

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
    AdminSidebar,
    AdminHeader,
    AdminLayout,
  ],
  imports: [BrowserModule, AppRoutingModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}

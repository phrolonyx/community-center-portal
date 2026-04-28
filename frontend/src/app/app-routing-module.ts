import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Accueil } from './components/accueil/accueil';
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';
import { EnfantsAdmin } from './components/admin/enfants-admin/enfants-admin';
import { ActivitesAdmin } from './components/admin/activites-admin/activites-admin';
import { SceancesAdmin } from './components/admin/sceances-admin/sceances-admin';
import { FormActiviteAdmin } from './components/admin/form-activite-admin/form-activite-admin';
import { FormSceanceAdmin } from './components/admin/form-sceance-admin/form-sceance-admin';


const routes: Routes = [
   {path: '', component: Accueil },
   {path: 'admin/dashboard', component: DashboardAdmin},
   {path: 'admin/users', component: UsersAdmin},
   {path: 'admin/enfants', component: EnfantsAdmin},
   {path: 'admin/activites', component: ActivitesAdmin},
   {path: 'admin/sceances', component: SceancesAdmin},
   {path: 'admin/form-activite-admin', component: FormActiviteAdmin},
   {path: 'admin/form-sceances-admin', component: FormSceanceAdmin},

   
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

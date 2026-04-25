import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Accueil } from './components/accueil/accueil';
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';

const routes: Routes = [
   { path: '', component: Accueil },
   {path: 'admin', component: DashboardAdmin},
   {path: 'admin/users', component: UsersAdmin }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Accueil } from './components/accueil/accueil';
import { DashboardAdmin } from './components/admin/dashboard-admin/dashboard-admin';
import { UsersAdmin } from './components/admin/users-admin/users-admin';
import {AdminLayout} from './components/admin/admin-layout/admin-layout';

const routes: Routes = [
   { path: '', component: Accueil },
   
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { Routes } from '@angular/router';
import { HomeComponent } from './home-component/home-component';
import { FacilityDetailsComponent } from './facility-details-component/facility-details-component';
import { SecretComponent } from './secret-component/secret-component';
import { AthleteComponent } from './athlete-component/athlete-component';
import { EmployeeComponent } from './employee-component/employee-component';
import { AdminComponent } from './admin-component/admin-component';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'facility/:id', component: FacilityDetailsComponent},
    { path: 'secret', component: SecretComponent},
    { path: 'admin-dashboard', component: AdminComponent},
    { path: 'employee-dashboard', component: EmployeeComponent},
    { path: 'athlete-dashboard', component: AthleteComponent},
];

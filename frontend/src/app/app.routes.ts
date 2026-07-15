import { Routes } from '@angular/router';
import { HomeComponent } from './home-component/home-component';
import { FacilityDetailsComponent } from './facility-details-component/facility-details-component';
import { SecretComponent } from './secret-component/secret-component';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'facility/:id', component: FacilityDetailsComponent},
    { path: 'secret', component: SecretComponent}
];

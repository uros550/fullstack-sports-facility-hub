import { Routes } from '@angular/router';
import { HomeComponent } from './home-component/home-component';
import { FacilityDetailsComponent } from './facility-details-component/facility-details-component';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'facility/:id', component: FacilityDetailsComponent}
];

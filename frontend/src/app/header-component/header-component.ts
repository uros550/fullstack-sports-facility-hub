import { Component, inject, signal } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header-component',
  imports: [RouterLink],
  templateUrl: './header-component.html',
  styleUrl: './header-component.css',
})
export class HeaderComponent {

  private authenticationService = inject(AuthenticationService);

  openAuthentication() {
    this.authenticationService.open();
  }

}

import { Component, inject } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-authentication-component',
  imports: [],
  templateUrl: './authentication-component.html',
  styleUrl: './authentication-component.css',
})
export class AuthenticationComponent {

  private authenticationService = inject(AuthenticationService);

  isOpen():boolean {
    return this.authenticationService.isOpen();
  }

  getMode(): string {
    return this.authenticationService.mode();
  }

  close(): void {
    this.authenticationService.close();
  }

  changeMode(newMode: string): void {
    this.authenticationService.setMode(newMode);
  }

}

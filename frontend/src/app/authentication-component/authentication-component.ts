import { Component, inject } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-authentication-component',
  imports: [FormsModule],
  templateUrl: './authentication-component.html',
  styleUrl: './authentication-component.css',
})
export class AuthenticationComponent {

  username = '';
  password = '';
  errorMessage = '';

  private authenticationService = inject(AuthenticationService);
  private router = inject(Router);

  isOpen():boolean {
    return this.authenticationService.isOpen();
  }

  getMode(): string {
    return this.authenticationService.getMode();
  }

  close() {
    this.errorMessage = '';
    this.authenticationService.close();
  }

  changeMode(newMode: string) {
    this.errorMessage = '';
    this.authenticationService.setMode(newMode);
  }

  login() {
    this.errorMessage = '';
    this.authenticationService.login(this.username, this.password).subscribe(data => {
      if (data != null) {
        localStorage.setItem("loggedUser", JSON.stringify(data));
        
        if (data.role === "ATHLETE") {
          this.router.navigate(["/athlete-dashboard"]);
        }
        else if (data.role === "EMPLOYEE") {
          this.router.navigate(["/employee-dashboard"]);
        }
        else {
          localStorage.removeItem("loggedUser");
          this.errorMessage = "Wrong role.";
        }
      }
      else {
        this.errorMessage = "Wrong credentials.";
      }
    })

  }

}

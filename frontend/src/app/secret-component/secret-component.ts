import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../services/authentication-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-secret-component',
  imports: [FormsModule],
  templateUrl: './secret-component.html',
  styleUrl: './secret-component.css',
})
export class SecretComponent {

  username = '';
  password = '';
  errorMessage = '';

  private authenticationService = inject(AuthenticationService);
  private router = inject(Router);
  
  loginAdmin() {
    this.errorMessage = '';
    this.authenticationService.login(this.username, this.password).subscribe(data => {
      if (data != null) {
        localStorage.setItem("loggedUser", JSON.stringify(data));
        
        if (data.role === "ADMIN") {
          this.router.navigate(["/admin-dashboard"]);
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

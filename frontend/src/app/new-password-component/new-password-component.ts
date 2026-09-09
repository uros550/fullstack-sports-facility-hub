import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-new-password-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './new-password-component.html',
  styleUrl: './new-password-component.css',
})
export class NewPasswordComponent implements OnInit {

  token: string | null = null;
  newPassword = '';
  confirmPassword = '';

  errorMessage = '';
  successMessage = '';

  private readonly passwordPattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9])[A-Za-z].{7,11}$/;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authenticationService = inject(AuthenticationService);

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if (!this.token) {
      this.errorMessage = 'Invalid or missing reset link.';
    }
  }

  confirm() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.token) {
      this.errorMessage = 'Invalid or missing reset link.';
      return;
    }
    if (!this.newPassword || !this.confirmPassword) {
      this.errorMessage = 'Please fill in both fields.';
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }
    if (!this.passwordPattern.test(this.newPassword)) {
      this.errorMessage = 'Password must be 8-12 characters, start with a letter, and contain at least one uppercase letter, one digit and one special character.';
      return;
    }

    this.authenticationService.resetPassword(this.token, this.newPassword).subscribe(data => {
      if (data === 'Success') {
        this.successMessage = 'Password changed successfully. You can now log in.';
        setTimeout(() => this.router.navigate(['/']), 2000);
      } else {
        this.errorMessage = data;
      }
    });
  }
}
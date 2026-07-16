import { Component, ElementRef, HostListener, inject, signal } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header-component',
  imports: [RouterLink],
  templateUrl: './header-component.html',
  styleUrl: './header-component.css',
})
export class HeaderComponent {

  private authenticationService = inject(AuthenticationService);
  private elementRef = inject(ElementRef);
  private router = inject(Router);

  currentUser = this.authenticationService.currentUser;
  isDropdownOpen = false;

  openAuthentication() {
    this.authenticationService.open();
  }

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  logout() {
    localStorage.removeItem('loggedUser');
    this.authenticationService.currentUser.set(null);
    this.isDropdownOpen = false;
    this.router.navigate(['/home']);
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isDropdownOpen = false;
    }
  }

}

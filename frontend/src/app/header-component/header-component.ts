import { Component, ElementRef, HostListener, inject } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { Router, RouterLink } from '@angular/router';
import { User } from '../models/User';

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

  //state
  currentUser = this.authenticationService.currentUser;
  isAthlete: boolean = false;
  isEmployee: boolean = false;
  isDropdownOpen = false;

  openAuthentication() {
    this.authenticationService.open();
  }

  getProfilePicture(): string {
    const user = this.currentUser();
    //doesnt have pfp
    if (!user?.profilePicture) {
      return 'default-pfp.jpg';
    }
    if (user.role === 'ATHLETE') this.isAthlete = true;
    else if (user.role === 'EMPLOYEE') this.isEmployee = true;
    //has pfp
    return `http://localhost:8080/${user.profilePicture}`;
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  profileReturn() {
    if (this.isAthlete) {
      this.router.navigate(['/athlete-dashboard']);
    }
    else if (this.isEmployee) {
      this.router.navigate(['/employee-dashboard']);
    }
    else {
      this.router.navigate(['/admin-dashboard']);
    }
  }

  logout() {
    localStorage.removeItem('loggedUser');
    localStorage.removeItem('cart');
    this.authenticationService.currentUser.set(null);
    this.isDropdownOpen = false;
    this.router.navigate(['/home']);
  }

  //close drop down when clicking outside
  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isDropdownOpen = false;
    }
  }

}

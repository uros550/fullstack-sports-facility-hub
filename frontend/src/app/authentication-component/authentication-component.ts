import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SportService } from '../services/sport-service';
import { Sport } from '../models/Sport';

@Component({
  selector: 'app-authentication-component',
  imports: [FormsModule],
  templateUrl: './authentication-component.html',
  styleUrl: './authentication-component.css',
})
export class AuthenticationComponent implements OnInit {

  username = '';
  password = '';
  firstName = '';
  lastName = '';
  phone = '';
  email = '';
  role = 'ATHLETE'; //default role

  //extended register
  facilityName = '';
  facilityAddress = '';
  facilityCity = '';
  facilityMb = '';
  facilityPib = '';

  sports = signal<Sport[]>([]);
  selectedSports: number[] = [];

  //messages
  errorMessage = '';
  successMessage = '';

  private authenticationService = inject(AuthenticationService);
  private sportService = inject(SportService);
  private router = inject(Router);

  ngOnInit(): void {
    this.sportService.getAllSports().subscribe(data => {
      this.sports.set(data);
    })
  }

  //pop-up methods
  isOpen(): boolean {
    return this.authenticationService.isOpen();
  }
  getMode(): string {
    return this.authenticationService.getMode();
  }
  changeMode(newMode: string) {
    this.errorMessage = '';
    this.successMessage = '';
    this.authenticationService.setMode(newMode);
  }
  close() {
    this.resetFields();
    this.authenticationService.close();
  }

  //LOGIN
  login() {
    this.authenticationService.login(this.username, this.password).subscribe(data => {
      if (data != null) {
        localStorage.setItem("loggedUser", JSON.stringify(data));

        if (data.role === "ATHLETE" && data.status === "APPROVED") {
          this.authenticationService.currentUser.set(data);
          this.close();
          this.router.navigate(["/athlete-dashboard"]);
        }
        else if (data.role === "EMPLOYEE" && data.status === "APPROVED") {
          this.authenticationService.currentUser.set(data);
          this.close();
          this.router.navigate(["/employee-dashboard"]);
        }
        else {
          localStorage.removeItem("loggedUser");
          this.errorMessage = "Wrong role.";
          if (data.status === "PENDING") this.errorMessage = "Account verification pending.";
          if (data.status === "REJECTED") this.errorMessage = "Account verification rejected.";
        }
      }
      else {
        this.errorMessage = "Wrong credentials.";
      }
    })
  }

  //REGISTRATION
  register() {
    //validation for employee (athlete can be without fav sport)
    if (this.role === 'EMPLOYEE') {
      if (!this.facilityName || !this.facilityAddress || !this.facilityCity || !this.facilityMb || !this.facilityPib) {
        this.errorMessage = 'Please fill in all facility details.';
        return;
      }
    }
    this.errorMessage = '';

    //checks
    if (this.role === 'EMPLOYEE') {
      if (!/^\d{8}$/.test(this.facilityMb)) {
        this.errorMessage = 'Matični broj mora imati tačno 8 cifara.';
        return;
      }
      if (!/^[1-9]\d{8}$/.test(this.facilityPib)) {
        this.errorMessage = 'PIB mora imati tačno 9 cifara i ne sme početi sa nulom.';
        return;
      }
    }

    //pack the object
    const registrationData = {
      id: 0,
      username: this.username,
      password: this.password,
      firstName: this.firstName,
      lastName: this.lastName,
      phone: this.phone,
      email: this.email,
      role: this.role,
      status: 'PENDING',
      profilePicture: '', 
      facilityName: this.role === 'EMPLOYEE' ? this.facilityName : '',
      facilityAddress: this.role === 'EMPLOYEE' ? this.facilityAddress : '',
      facilityCity: this.role === 'EMPLOYEE' ? this.facilityCity : '',
      facilityMb: this.role === 'EMPLOYEE' ? this.facilityMb : '',
      facilityPib: this.role === 'EMPLOYEE' ? this.facilityPib : '',
      sports: this.role === 'ATHLETE' ? this.selectedSports : []
    };

    this.authenticationService.register(registrationData).subscribe( data => {
      if (data === 'Success') {
        this.successMessage = 'Successfully signed up.';
        this.changeMode('LOGIN')
      }
      else {
        this.errorMessage = data;
      }
    })
  }
  
  goToExtendedRegister() {
    //validation
    if (!this.username || !this.password || !this.firstName || !this.lastName || !this.phone || !this.email) {
      this.errorMessage = 'Please fill in all fields.';
      return;
    }
    this.errorMessage = '';
    this.changeMode('EXTENDED_REGISTER');
  }

  onSportsChange() {
    if (this.selectedSports.length > 5) {
      this.errorMessage = 'Maximum of 5 sports selected';
      this.selectedSports.pop(); //remove sixth
    } else {
      this.errorMessage = '';
    }
  }

  //RESETS
  resetPassword() {

  }

  resetFields() {
    this.username = '';
    this.password = '';
    this.firstName = '';
    this.lastName = '';
    this.phone = '';
    this.email = '';
    this.role = 'ATHLETE';
    this.facilityName = '';
    this.facilityAddress = '';
    this.facilityCity = '';
    this.facilityMb = '';
    this.facilityPib = '';
    this.selectedSports = [];
    this.errorMessage = '';
    this.successMessage = '';
  }

}
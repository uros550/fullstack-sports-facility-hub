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

  //messages
  errorMessage = '';
  successMessage = '';

  sports = signal<Sport[]>([]);
  selectedSports: number[] = [];

  private authenticationService = inject(AuthenticationService);
  private sportService = inject(SportService);
  private router = inject(Router);

  ngOnInit(): void {
    this.sportService.getAllSports().subscribe(data => {
      this.sports.set(data);
    })
  }

  isOpen():boolean {
    return this.authenticationService.isOpen();
  }

  getMode(): string {
    return this.authenticationService.getMode();
  }

  close() {
    this.resetFields();
    this.authenticationService.close();
  }

  changeMode(newMode: string) {
    this.errorMessage = '';
    this.successMessage = '';
    this.authenticationService.setMode(newMode);
  }

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

  goToExtendedRegister() {
    this.errorMessage = '';
    this.changeMode('EXTENDED_REGISTER');
  }

  onSportsChange(event: any) {
    const selected = Array.from(event.target.selectedOptions).map((o: any) => Number(o.value));
    
    if (selected.length > 5) {
      this.errorMessage = 'Možete izabrati najviše 5 sportova.';
      event.target.value = this.selectedSports; // Vraćamo vizuelni prikaz na staro
    } else {
      this.errorMessage = '';
      this.selectedSports = selected;
    }
  }

  register() {
    this.errorMessage = '';

    //checks
    if (this.role === 'EMPLOYEE') {
      if (!this.facilityName || !this.facilityAddress || !this.facilityMb || !this.facilityPib) {
        this.errorMessage = 'All fields are';
        return;
      }
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
    this.facilityMb = '';
    this.facilityPib = '';
    this.selectedSports = [];
    this.errorMessage = '';
    this.successMessage = '';
  }

}

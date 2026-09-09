import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header-component/header-component';
import { FooterComponent } from "./footer-component/footer-component";
import { AuthenticationComponent } from './authentication-component/authentication-component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, FooterComponent, AuthenticationComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {}

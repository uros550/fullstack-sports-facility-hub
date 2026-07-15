import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  
  //like getters but better
  isOpen = signal<boolean>(false);
  mode = signal<string>('LOGIN');

  open() {
    this.mode.set('LOGIN');
    this.isOpen.set(true);
  }

  close() {
    this.isOpen.set(false);
  }

  setMode(newMode: string) {
    this.mode.set(newMode);
  }

}

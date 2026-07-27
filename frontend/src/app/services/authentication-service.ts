import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/users';
  
  //signals other users of var if changed
  currentUser = signal<User | null>(JSON.parse(localStorage.getItem('loggedUser') || 'null'));

  private _isOpen: boolean = false;
  private _mode: string = 'LOGIN';

  //pop-up screen
  isOpen(): boolean {
    return this._isOpen;
  }
  getMode(): string {
    return this._mode;
  }
  open() {
    this._mode = 'LOGIN';
    this._isOpen = true;
  }
  close() {
    this._isOpen = false;
  }
  setMode(newMode: string) {
    this._mode = newMode;
  }


  login(username: string, password: string) {
    return this.http.post<User>(`${this.path}/login`, {username, password});
  }

  register(user: any) {
    return this.http.post(`${this.path}/register`, user, { responseType: 'text' }); //string response not json
  }

  uploadProfilePicture(username: string, file: File) {
    //data struct
    const data = new FormData();
    data.append('username', username);
    data.append('image', file);
    return this.http.put(`${this.path}/upload-image`, data, { responseType: 'text' });
  }

}

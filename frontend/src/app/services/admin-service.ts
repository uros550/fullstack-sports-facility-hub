import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
 
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/admin';
  
  getAllUsers() {
    return this.http.get<User[]>(`${this.path}/users/all`);
  }

  getPendingUsers() {
    return this.http.get<User[]>(`${this.path}/users/pending`);
  }

  changeUsername(userId: number, newUsername: string) {
    return this.http.post<boolean>(`${this.path}/users/update/${userId}`, null, {
    params: {
      newUsername: newUsername
    }});
  }

  acceptRegistration(userId: number) {
    return this.http.post<boolean>(`${this.path}/users/accept/${userId}`, {});
  }

  rejectRegistration(userId: number) {
    return this.http.post<boolean>(`${this.path}/users/reject/${userId}`, {});
  }

  deleteAccount(userId: number) {
    return this.http.post<boolean>(`${this.path}/users/delete/${userId}`, {});
  }
  
}

import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { BrowserStorageService } from './browser-storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  authorization = signal<string | null>(null);

  constructor(
    private httpClient: HttpClient,
    private storage: BrowserStorageService
  ) {
    const token = this.storage.get('Authorization');
    if (token) {
      this.authorization.set(token);
    }
  }

  login(credentials: UserCredential) {
    return this.httpClient.post<AuthToken>('/user-service/api/v1/auth', credentials)
  }

  register(userRequest: UserRequest) {
    return this.httpClient.post('/user-service/api/v1/auth/register', userRequest,{
      observe: 'response'
    });
  }

  logout() {
    this.storage.remove('Authorization');
    this.authorization.set(null);
  }

  setAuthorization(token: string) {
    this.storage.set('Authorization', token);
    this.authorization.set(token);
  }

  getAuthrization() {
    return this.authorization();
  }
}


export interface UserCredential{
  emailOrUsername: string,
  password: string
}

export interface AuthToken{
  token: string
}

export interface UserRequest{
  username: string,
  email: string,
  password: string,
  firstName: string,
  lastName: string,
  gender: string,
  dateOfBirth: string
}
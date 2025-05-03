import { Injectable } from '@angular/core';
import AuthService from './auth.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export default class UserService {
  constructor(
    private authService: AuthService,
    private httpClient: HttpClient
  ) {}

  fetchUser(): Observable<HttpResponse<User>> {
    const authentication = this.authService.getAuthentication();
    console.log("Authentication :-",authentication);
    return this.httpClient.get<User>('/user-service/api/v1/users', {
      headers: {
        Authorization: `Bearer ${authentication?? ''}`,
      },
      observe: 'response',
    });
  }
}

export interface User {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  gender: string;
}

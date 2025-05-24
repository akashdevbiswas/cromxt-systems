import { Injectable } from "@angular/core";
import { AuthService } from "./auth.service";
import { HttpClient } from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private authService: AuthService, private httpClient:HttpClient) { }

  fetchUser() {
    return this.httpClient.get<Users>('/server/api/v1/users',{
      headers:{
        'Authorization': `Bearer ${this.authService.getAuthrization()}`
      },
      observe: 'response',
    });
  }

  isUserProfileComplete() {
    return this.httpClient.get<boolean>('/server/api/v1/users/is-profile-complete',{
      headers:{
        'Authorization': `Bearer ${this.authService.getAuthrization()}`
      }
    });
  }
}

export interface Users{
  id: string,
  username: string,
  profileImage: string,
  firstName: string,
  lastName: string,
  email: string
}
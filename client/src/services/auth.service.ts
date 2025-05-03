import { HttpClient } from "@angular/common/http";
import { Injectable, OnInit, signal, WritableSignal } from "@angular/core";
import { BrowserStorageService } from "./localStorage.service";

@Injectable({
    providedIn: 'root',
})
export default class AuthService implements OnInit{

    private authentication: WritableSignal<string | null> = signal<string | null>(null);

    constructor(private httpClient: HttpClient, private localStorage: BrowserStorageService){}

    ngOnInit(): void {
        const token = this.localStorage.get('authentication');
        
        if(token){
            this.authentication.set(token);
        }else{
            this.authentication.set(null);
        }
    }

    public setAuthentication(token: string){
        this.localStorage.set('authentication', token);
        this.authentication.set(token);
    }

    public getAuthentication(): string | null{
        return this.authentication();
    }

    public registerUser(user: UserRequest){
        return this.httpClient.post<void>('/user-service/api/v1/auth/register', user,{
            observe: 'response'
        });
    }

    public login(UserCredentials: UserCredentials){
        return this.httpClient.post<Authorization>('/user-service/api/v1/auth', UserCredentials, {
            observe: 'response'
        });
    }

    public removeAuthentication(){
        this.localStorage.remove('token');
        this.authentication.set(null);
    }
}

export interface UserRequest{
    username: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    gender: string;
    dateOfBirth: string;
}

export interface UserCredentials{
    emailOrUsername: string;
    password: string;
}
export interface Authorization{
    token: string;
}
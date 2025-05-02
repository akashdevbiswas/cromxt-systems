import { Component } from '@angular/core';
import LoginComponent from "../components/login.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'auth-page',
  template: `
    <div class="min-h-screen h-auto w-full">
        <router-outlet></router-outlet>
    </div>
  `,
  standalone: true,
  imports: [RouterOutlet],
})
export default class AuthPage {}

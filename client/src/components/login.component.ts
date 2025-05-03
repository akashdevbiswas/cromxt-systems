import { Component } from '@angular/core';
import InputComponent from './input.component';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import ButtonComponent from './button.component';
import AuthService, { UserCredentials } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'login-component',
  template: `
    <form
      (submit)="onSubmitForm($event)"
      class="absolute top-1/2 left-1/2 -translate-1/2 w-[90%] md:w-[60%] lg:w-[50%] xl:w-[40%] flex items-center flex-col border-2 border-black rounded-xl gap-10 py-8"
    >
      <h1 class="text-2xl">Login</h1>
      <input-component
        lable="Username Or Email"
        class="w-[90%]"
        [inputControl]="usernameOrEmail"
      ></input-component>
      <input-component
        [type]="'password'"
        lable="Password"
        class="w-[90%]"
        [inputControl]="password"
      ></input-component>
      <button-component
        buttonName="Log in"
        class="min-w-fit w-[20%] rounded-xl btn-primary"
      ></button-component>
    </form>
  `,
  standalone: true,
  imports: [InputComponent, ReactiveFormsModule, ButtonComponent],
})
export default class LoginComponent {
  constructor(private authService: AuthService, private router: Router) {}

  usernameOrEmail = new FormControl('');
  password = new FormControl('');

  onSubmitForm = (eve: SubmitEvent) => {
    eve.preventDefault();

    if (this.usernameOrEmail.value && this.password.value) {
      const userCredentials: UserCredentials = {
        emailOrUsername: this.usernameOrEmail.value,
        password: this.password.value,
      };
      this.authService.login(userCredentials).subscribe({
        next: (res) => {
          const {status,body} = res;
          if(status == 201 && body){
            this.authService.setAuthentication(body.token);
          }
          this.router.navigateByUrl('/home');
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  };
}

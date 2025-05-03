import { Component } from '@angular/core';
import InputComponent from './input.component';
import { FormControl } from '@angular/forms';
import ButtonComponent from './button.component';
import AuthService, { UserRequest } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'signup-component',
  template: `
    <form
      (submit)="onSubmit($event)"
      class="absolute top-1/2 left-1/2 -translate-1/2 gap-4 flex flex-col items-center h-auto w-[40%] py-10  border-2 border-black rounded-4xl"
    >
      <h1 class="text-3xl">Sign Up</h1>
      <div class="w-full input-group">
        <input-component
          lable="Username"
          class="w-full"
          [inputControl]="username"
        ></input-component>
        <input-component
          lable="Email"
          class="w-full"
          [inputControl]="email"
        ></input-component>
      </div>

      <div class="input-group">
        <input-component
          lable="First Name"
          class="w-full"
          [inputControl]="firstName"
        ></input-component>
        <input-component
          lable="Last Name"
          class="w-full"
          [inputControl]="lastName"
        ></input-component>
      </div>

      <div class="input-group">
        <input-component
          lable="Gender"
          class="w-full"
          type="select"
          [selectOptions]="genderOptions"
          [inputControl]="gender"
        ></input-component>
        <input-component
          lable="Date of Birth"
          class="w-full"
          type="date"
          [inputControl]="dateOfBirth"
        ></input-component>
      </div>

      <div class="input-group">
        <input-component
          lable="Password"
          class="w-full"
          [inputControl]="password"
          [type]="'password'"
        ></input-component>
        <input-component
          lable="Confirm Password"
          class="w-full"
          [inputControl]="confirmPassword"
          [type]="'password'"
        ></input-component>
      </div>
      <button-component
        [className]=""
        [buttonName]="'Sign Up'"
        [class]="'w-full'"
      ></button-component>
    </form>
  `,
  standalone: true,
  imports: [InputComponent, ButtonComponent],
})
export default class SignUpComponent {
  username = new FormControl('');
  email = new FormControl('');
  firstName = new FormControl('');
  lastName = new FormControl('');
  gender = new FormControl('');
  dateOfBirth = new FormControl('');
  password = new FormControl('');
  confirmPassword = new FormControl('');

  genderOptions = ['MALE', 'FEMALE', 'OTHERS'];

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(eve: SubmitEvent) {
    eve.preventDefault();
    if (this.password.value !== this.confirmPassword.value) {
      return;
    }
    console.log(
      this.username.value,
      this.email.value,
      this.password.value,
      this.firstName.value,
      this.lastName.value,
      this.gender.value,
      this.dateOfBirth.value
    );
    if (
      this.username.value &&
      this.email.value &&
      this.password.value &&
      this.firstName.value &&
      this.lastName.value &&
      this.gender.value &&
      this.dateOfBirth.value
    ) {
      console.log(this.username.value);
      const userRequest: UserRequest = {
        username: this.username.value,
        email: this.email.value,
        password: this.password.value,
        firstName: this.firstName.value,
        lastName: this.lastName.value,
        gender: this.gender.value,
        dateOfBirth: this.dateOfBirth.value,
      };

      this.authService.registerUser(userRequest).subscribe((res) => {
        console.log(res);
        if (res.status == 204) {
          this.router.navigateByUrl('/auth/login');
        }
      });
    }
  }
}

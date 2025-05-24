import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputComponent } from '../../components/input.component';
import { ButtonComponent } from '../../components/button.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ErrorService } from '../../services/error.service';

@Component({
  selector: 'sign-up-page',
  templateUrl: 'sign-up.page.html',
  imports: [InputComponent, ButtonComponent],
})
export class SignUpPage {
  genderOptions: string[] = ['MALE', 'FEMALE', 'OTHERS'];

  constructor(
    private authService: AuthService,
    private router: Router,
    protected errorService: ErrorService
  ) {}

  username = new FormControl(null, {
    validators: [Validators.required, Validators.minLength(5)],
  });
  email = new FormControl(null, {
    validators: [Validators.email, Validators.required],
  });
  password = new FormControl(null, {
    validators: [Validators.required, Validators.minLength(8)],
  });
  confirmPassword = new FormControl(null, {
    validators: [Validators.required, Validators.minLength(8)],
  });
  firstName = new FormControl(null, {
    validators: [Validators.required],
  });
  lastName = new FormControl(null, {
    validators: [Validators.required],
  });
  dateOfBirth = new FormControl(null, {
    validators: [Validators.required],
  });
  gender = new FormControl('', {
    validators: [Validators.nullValidator],
  });

  onSubmit(eve: SubmitEvent) {
    eve.preventDefault();

    if (this.password.value !== this.confirmPassword.value) {
      this.errorService.setError('Passwords do not match');
      return;
    }

    if (
      this.username.value &&
      this.email.value &&
      this.password.value &&
      this.firstName.value &&
      this.lastName.value &&
      this.gender.value &&
      this.dateOfBirth.value 
    ) {
      this.authService
        .register({
          username: this.username.value,
          password: this.password.value,
          firstName: this.firstName.value,
          lastName: this.lastName.value,
          email: this.email.value,
          gender: this.gender.value,
          dateOfBirth: this.dateOfBirth.value
        })
        .subscribe({
          next: (res) => {
            const { status } = res;
            if (status === 204) {
              this.router.navigateByUrl('/auth/login');
            }
          },
          error: (err) => {
            console.error(err);
            this.errorService.setError(
              'Some error occurred while saving the user'
            );
          },
          complete: () => {
            console.log('User saved successfully');
          },
        });
    }
  }
}

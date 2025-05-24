import { Component } from '@angular/core';
import { InputComponent } from '../../components/input.component';
import { ButtonComponent } from '../../components/button.component';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'sign-in-page',
  templateUrl: 'sign-in.page.html',
  standalone: true,
  imports: [InputComponent, ButtonComponent],
})
export class SignInPage {
  constructor(private authService: AuthService, private router: Router) {}

  username = new FormControl<string | null>(null);
  password = new FormControl<string | null>(null);

  onSubmit(eve: SubmitEvent) {
    eve.preventDefault();

    if (this.username.value && this.password.value) {
      this.authService
        .login({
          emailOrUsername: this.username.value,
          password: this.password.value,
        })
        .subscribe({
          next: (authToken) => {
            const { token } = authToken;
            if (token) {
              this.authService.setAuthorization(token);
              this.router.navigateByUrl('/');
            }
          },
        });
    }
  }
}

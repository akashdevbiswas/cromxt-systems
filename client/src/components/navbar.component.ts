import { Component, Inject, input, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonComponent } from './button.component';
import { AuthService } from '../services/auth.service';
import { isPlatformBrowser, NgOptimizedImage } from '@angular/common';
import { Users, UserService } from '../services/users.service';

@Component({
  selector: 'profile-component',
  template: `
    <div class="w-[100px]">
      <img
        [ngSrc]="imageUrl"
        [alt]="username()"
        [height]="50"
        [width]="50"
        class="rounded-full object-contain"
      />
    </div>
  `,
  imports: [NgOptimizedImage],
  standalone: true,
})
export class ProfileComponent {
  imageUrl = input<string>('');
  username = input.required<string>();
}

@Component({
  selector: 'navbar-compoenent',
  template: `
    <nav class="flex justify-between items-center font-bold px-2 py-2">
      <p
        (click)="router.navigateByUrl('/home')"
        class="uppercase text-2xl md:text-3xl lg:text-4xl cursor-pointer"
      >
        Cromxt
      </p>
      @if(router.url.includes('/auth')) { @if(router.url === '/auth/login') {
      <button-component
        [buttonName]="'Sign Up'"
        [navigationUrl]="'/auth/register'"
        [buttonType]="'navigation'"
      ></button-component>
      }@else{
      <button-component
        [buttonName]="'Sign In'"
        [navigationUrl]="'/auth/login'"
        [buttonType]="'navigation'"
      ></button-component>
      } }@else { @if(user) {

      <profile-component
        [username]="user.username"
        [imageUrl]="user.profileImage"
      ></profile-component>

      }@else{
      <button-component
        [buttonName]="'Sign In'"
        [navigationUrl]="'/auth'"
        [buttonType]="'navigation'"
      ></button-component>
      } }
    </nav>
  `,
  standalone: true,
  imports: [ButtonComponent, ProfileComponent],
})
export class NavbarComponent {
  protected user: Users | null = null;
  constructor(
    protected router: Router,
    protected authService: AuthService,
    userService: UserService,
    
    @Inject(PLATFORM_ID) platFormId: Object
  ) {
    if (isPlatformBrowser(platFormId)) {
      userService.fetchUser().subscribe({
        next: (res) => {
          const { status, body } = res;
          if (status === 200 && body) {
            this.user = body;
          }
        },
        error: (err) => {
          console.error(err);
          router.navigateByUrl('/auth');
        },
      });
    }
  }
}

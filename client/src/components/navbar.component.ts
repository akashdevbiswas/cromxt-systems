import { Component, effect, OnInit } from '@angular/core';
import ButtonComponent from './button.component';
import UserService, { User } from '../services/user.service';
import AuthService from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'navbar-component',
  template: `
    <nav class="navbar bg-base-100 shadow-sm">
      <div class="flex-1">
        <a class="btn btn-ghost text-xl">Crombucket</a>
      </div>
      @if (user) {
      <div class="flex-none">
        <div class="dropdown dropdown-end">
          <div
            tabindex="0"
            role="button"
            class="btn btn-ghost btn-circle avatar"
          >
            <div class="avatar avatar-placeholder">
              <div class="bg-neutral text-neutral-content w-12 rounded-full">
                <span>{{user.firstName.charAt(0) + user.lastName.charAt(0)}}</span>
              </div>
            </div>
            <p>{{ user.username }}</p>
          </div>
          <ul
            tabindex="0"
            class="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
          >
            <li>
              <a class="justify-between"> Profile </a>
            </li>
            <li><a>Settings</a></li>
            <li><a>Logout</a></li>
          </ul>
        </div>
      </div>
      } @else {
      <button-component
        (onClick)="onClick()"
        [buttonName]="router.url == '/auth/login' ? 'Sign-Up' : 'Login'"
      >
      </button-component>
      }
    </nav>
  `,
  imports: [ButtonComponent],
})
export default class NavbarComponent implements OnInit {
  user: User | null = null;
  currentUrl: string = 'Sign-Up';
  constructor(
    private userService: UserService,
    private authService: AuthService,
    public router: Router
  ) {}

  ngOnInit(): void {
    try {
      this.userService.fetchUser().subscribe((res) => {
        if (res.status == 200 && res.body) {
          this.user = res.body;
        } else if (res.status == 401) {
          this.authService.removeAuthentication();
        }
      });
    } catch (e) {
      console.error(e);
    }
  }

  onClick() {
    if (this.router.url == '/auth/login')
      this.router.navigateByUrl('/auth/sign-up');
    else this.router.navigateByUrl('/auth/login');
  }
}

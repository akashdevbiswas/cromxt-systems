import { Routes } from '@angular/router';
import { HomePage } from '../pages/home/home.page';
import { AuthPage } from '../pages/auth/auth.page';
import { SignUpPage } from '../pages/sign-up/sign-up.page';
import { SignInPage } from '../pages/sign-in/sign-in.page';

export const routes: Routes = [
  {
    path:'home',
    component: HomePage
  },
  {
    path:'auth',
    component: AuthPage,
    children: [
      {
        path: 'login',
        component: SignInPage
      },
      {
        path: 'register',
        component: SignUpPage
      },
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },

];

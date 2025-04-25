import { Routes } from '@angular/router';
import LoginPage from '../pages/login.page';
import HomePage from '../pages/home.page';

export const routes: Routes = [
    {
        path:'home',
        component: HomePage
    },
    {
        path: 'login',
        component: LoginPage
    },
    {
        path:'',
        redirectTo:'/home',
        pathMatch: 'full'
    }
];

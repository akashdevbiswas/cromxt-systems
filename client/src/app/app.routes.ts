import { Routes } from '@angular/router';
import AuthPage from '../pages/auth.page';
import HomePage from '../pages/home.page';
import LoginComponent from '../components/login.component';
import SignUpComponent from '../components/signup.component';

export const routes: Routes = [
    {
        path:'home',
        component: HomePage
    },
    {
        path: 'auth',
        component: AuthPage,
        children:[
            {   
                path: 'login',
                component: LoginComponent
            },
            {
                path: 'sign-up',
                component: SignUpComponent
            },
            {
                path: '',
                redirectTo: '/auth/login',
                pathMatch: 'full'
            } 
        ]
    },
    {
        path:'',
        redirectTo:'/home',
        pathMatch: 'full'
    }
];

import { Routes } from '@angular/router';
import { ROUTE_CONFIG } from './config/routes.config';
import { LayoutComponent } from './layout/layout.component';
import { HistorialComponent } from './pages/historial/historial.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { authGuard } from './auth.guard';
import { loginGuard } from './login.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: ROUTE_CONFIG.register,
  },
  {
    path: ROUTE_CONFIG.register,
    component: RegisterComponent,
    canActivate: [loginGuard],
    title: 'Register',
  },
  {
    path: ROUTE_CONFIG.app,
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: ROUTE_CONFIG.home,
      },
      {
        path: ROUTE_CONFIG.home,
        component: HomeComponent,
        title: 'Home',
      },
      {
        path: ROUTE_CONFIG.historial,
        component: HistorialComponent,
        title: 'Historial',
      },
    ],
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: ROUTE_CONFIG.register,
  },
];

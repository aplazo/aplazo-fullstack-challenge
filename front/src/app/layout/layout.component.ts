import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoDashboardComponents } from '@apz/shared-ui/dashboard';
import { AplazoSidenavLinkComponent } from '@apz/shared-ui/sidenav';
import { ROUTE_CONFIG } from '../config/routes.config';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: true,
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  imports: [
    AplazoDashboardComponents,
    AplazoButtonComponent,
    AplazoSidenavLinkComponent,
    RouterOutlet,
    RouterLink,
  ],
})
export class LayoutComponent {
  constructor(private authService: AuthService) {}

  readonly #router = inject(Router);

  readonly appRoutes = ROUTE_CONFIG;

  clickLogo(): void {
    this.#router.navigate([ROUTE_CONFIG.home]);
  }

  onLogout(): void {
    this.authService.deleteUserDetails();
    this.#router.navigate([ROUTE_CONFIG.home]);
  }
}

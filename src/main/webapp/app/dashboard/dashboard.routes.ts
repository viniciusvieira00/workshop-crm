import { Routes } from '@angular/router';

import { DashboardComponent } from './dashboard.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

export const DASHBOARD_ROUTE: Routes = [
  {
    path: '',
    component: DashboardComponent,
    canActivate: [UserRouteAccessService],
    data: {
      pageTitle: 'Dashboard Analítico',
    },
  },
];

import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServiceOrderResolve from './route/service-order-routing-resolve.service';

const serviceOrderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-order.component').then(m => m.ServiceOrderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-order-detail.component').then(m => m.ServiceOrderDetailComponent),
    resolve: {
      serviceOrder: ServiceOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-order-update.component').then(m => m.ServiceOrderUpdateComponent),
    resolve: {
      serviceOrder: ServiceOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-order-update.component').then(m => m.ServiceOrderUpdateComponent),
    resolve: {
      serviceOrder: ServiceOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceOrderRoute;

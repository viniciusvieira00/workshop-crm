import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServiceOrderTypeResolve from './route/service-order-type-routing-resolve.service';

const serviceOrderTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-order-type.component').then(m => m.ServiceOrderTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-order-type-detail.component').then(m => m.ServiceOrderTypeDetailComponent),
    resolve: {
      serviceOrderType: ServiceOrderTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-order-type-update.component').then(m => m.ServiceOrderTypeUpdateComponent),
    resolve: {
      serviceOrderType: ServiceOrderTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-order-type-update.component').then(m => m.ServiceOrderTypeUpdateComponent),
    resolve: {
      serviceOrderType: ServiceOrderTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceOrderTypeRoute;

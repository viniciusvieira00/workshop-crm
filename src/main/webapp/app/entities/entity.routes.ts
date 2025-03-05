import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'Clients' },
    loadChildren: () => import('./crm/client/client.routes'),
  },
  {
    path: 'vehicle',
    data: { pageTitle: 'Vehicles' },
    loadChildren: () => import('./crm/vehicle/vehicle.routes'),
  },
  {
    path: 'service-order',
    data: { pageTitle: 'Ordens de Serviço' },
    loadChildren: () => import('./crm/service-order/service-order.routes'),
  },
  {
    path: 'service-order-type',
    data: { pageTitle: 'ServiceOrderTypes' },
    loadChildren: () => import('./crm/service-order-type/service-order-type.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'Products' },
    loadChildren: () => import('./crm/product/product.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

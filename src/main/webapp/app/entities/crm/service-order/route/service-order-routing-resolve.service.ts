import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceOrder } from '../service-order.model';
import { ServiceOrderService } from '../service/service-order.service';

const serviceOrderResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceOrder> => {
  const id = route.params.id;
  if (id) {
    return inject(ServiceOrderService)
      .find(id)
      .pipe(
        mergeMap((serviceOrder: HttpResponse<IServiceOrder>) => {
          if (serviceOrder.body) {
            return of(serviceOrder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default serviceOrderResolve;

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceOrderType } from '../service-order-type.model';
import { ServiceOrderTypeService } from '../service/service-order-type.service';

const serviceOrderTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceOrderType> => {
  const id = route.params.id;
  if (id) {
    return inject(ServiceOrderTypeService)
      .find(id)
      .pipe(
        mergeMap((serviceOrderType: HttpResponse<IServiceOrderType>) => {
          if (serviceOrderType.body) {
            return of(serviceOrderType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default serviceOrderTypeResolve;

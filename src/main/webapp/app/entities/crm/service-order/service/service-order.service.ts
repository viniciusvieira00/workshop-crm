import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceOrder, NewServiceOrder } from '../service-order.model';

export type PartialUpdateServiceOrder = Partial<IServiceOrder> & Pick<IServiceOrder, 'id'>;

type RestOf<T extends IServiceOrder | NewServiceOrder> = Omit<
  T,
  'creationDate' | 'startDate' | 'completionDate' | 'createdDate' | 'lastModifiedDate'
> & {
  creationDate?: string | null;
  startDate?: string | null;
  completionDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestServiceOrder = RestOf<IServiceOrder>;

export type NewRestServiceOrder = RestOf<NewServiceOrder>;

export type PartialUpdateRestServiceOrder = RestOf<PartialUpdateServiceOrder>;

export type EntityResponseType = HttpResponse<IServiceOrder>;
export type EntityArrayResponseType = HttpResponse<IServiceOrder[]>;

@Injectable({ providedIn: 'root' })
export class ServiceOrderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-orders');

  create(serviceOrder: NewServiceOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrder);
    return this.http
      .post<RestServiceOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceOrder: IServiceOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrder);
    return this.http
      .put<RestServiceOrder>(`${this.resourceUrl}/${this.getServiceOrderIdentifier(serviceOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceOrder: PartialUpdateServiceOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrder);
    return this.http
      .patch<RestServiceOrder>(`${this.resourceUrl}/${this.getServiceOrderIdentifier(serviceOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceOrderIdentifier(serviceOrder: Pick<IServiceOrder, 'id'>): number {
    return serviceOrder.id;
  }

  compareServiceOrder(o1: Pick<IServiceOrder, 'id'> | null, o2: Pick<IServiceOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceOrderIdentifier(o1) === this.getServiceOrderIdentifier(o2) : o1 === o2;
  }

  addServiceOrderToCollectionIfMissing<Type extends Pick<IServiceOrder, 'id'>>(
    serviceOrderCollection: Type[],
    ...serviceOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceOrders: Type[] = serviceOrdersToCheck.filter(isPresent);
    if (serviceOrders.length > 0) {
      const serviceOrderCollectionIdentifiers = serviceOrderCollection.map(serviceOrderItem =>
        this.getServiceOrderIdentifier(serviceOrderItem),
      );
      const serviceOrdersToAdd = serviceOrders.filter(serviceOrderItem => {
        const serviceOrderIdentifier = this.getServiceOrderIdentifier(serviceOrderItem);
        if (serviceOrderCollectionIdentifiers.includes(serviceOrderIdentifier)) {
          return false;
        }
        serviceOrderCollectionIdentifiers.push(serviceOrderIdentifier);
        return true;
      });
      return [...serviceOrdersToAdd, ...serviceOrderCollection];
    }
    return serviceOrderCollection;
  }

  protected convertDateFromClient<T extends IServiceOrder | NewServiceOrder | PartialUpdateServiceOrder>(serviceOrder: T): RestOf<T> {
    return {
      ...serviceOrder,
      creationDate: serviceOrder.creationDate?.toJSON() ?? null,
      startDate: serviceOrder.startDate?.toJSON() ?? null,
      completionDate: serviceOrder.completionDate?.toJSON() ?? null,
      createdDate: serviceOrder.createdDate?.toJSON() ?? null,
      lastModifiedDate: serviceOrder.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceOrder: RestServiceOrder): IServiceOrder {
    return {
      ...restServiceOrder,
      creationDate: restServiceOrder.creationDate ? dayjs(restServiceOrder.creationDate) : undefined,
      startDate: restServiceOrder.startDate ? dayjs(restServiceOrder.startDate) : undefined,
      completionDate: restServiceOrder.completionDate ? dayjs(restServiceOrder.completionDate) : undefined,
      createdDate: restServiceOrder.createdDate ? dayjs(restServiceOrder.createdDate) : undefined,
      lastModifiedDate: restServiceOrder.lastModifiedDate ? dayjs(restServiceOrder.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceOrder>): HttpResponse<IServiceOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceOrder[]>): HttpResponse<IServiceOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

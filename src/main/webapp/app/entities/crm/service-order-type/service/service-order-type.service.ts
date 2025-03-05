import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceOrderType, NewServiceOrderType } from '../service-order-type.model';

export type PartialUpdateServiceOrderType = Partial<IServiceOrderType> & Pick<IServiceOrderType, 'id'>;

type RestOf<T extends IServiceOrderType | NewServiceOrderType> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestServiceOrderType = RestOf<IServiceOrderType>;

export type NewRestServiceOrderType = RestOf<NewServiceOrderType>;

export type PartialUpdateRestServiceOrderType = RestOf<PartialUpdateServiceOrderType>;

export type EntityResponseType = HttpResponse<IServiceOrderType>;
export type EntityArrayResponseType = HttpResponse<IServiceOrderType[]>;

@Injectable({ providedIn: 'root' })
export class ServiceOrderTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-order-types');

  create(serviceOrderType: NewServiceOrderType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrderType);
    return this.http
      .post<RestServiceOrderType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceOrderType: IServiceOrderType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrderType);
    return this.http
      .put<RestServiceOrderType>(`${this.resourceUrl}/${this.getServiceOrderTypeIdentifier(serviceOrderType)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceOrderType: PartialUpdateServiceOrderType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOrderType);
    return this.http
      .patch<RestServiceOrderType>(`${this.resourceUrl}/${this.getServiceOrderTypeIdentifier(serviceOrderType)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceOrderType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceOrderType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceOrderTypeIdentifier(serviceOrderType: Pick<IServiceOrderType, 'id'>): number {
    return serviceOrderType.id;
  }

  compareServiceOrderType(o1: Pick<IServiceOrderType, 'id'> | null, o2: Pick<IServiceOrderType, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceOrderTypeIdentifier(o1) === this.getServiceOrderTypeIdentifier(o2) : o1 === o2;
  }

  addServiceOrderTypeToCollectionIfMissing<Type extends Pick<IServiceOrderType, 'id'>>(
    serviceOrderTypeCollection: Type[],
    ...serviceOrderTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceOrderTypes: Type[] = serviceOrderTypesToCheck.filter(isPresent);
    if (serviceOrderTypes.length > 0) {
      const serviceOrderTypeCollectionIdentifiers = serviceOrderTypeCollection.map(serviceOrderTypeItem =>
        this.getServiceOrderTypeIdentifier(serviceOrderTypeItem),
      );
      const serviceOrderTypesToAdd = serviceOrderTypes.filter(serviceOrderTypeItem => {
        const serviceOrderTypeIdentifier = this.getServiceOrderTypeIdentifier(serviceOrderTypeItem);
        if (serviceOrderTypeCollectionIdentifiers.includes(serviceOrderTypeIdentifier)) {
          return false;
        }
        serviceOrderTypeCollectionIdentifiers.push(serviceOrderTypeIdentifier);
        return true;
      });
      return [...serviceOrderTypesToAdd, ...serviceOrderTypeCollection];
    }
    return serviceOrderTypeCollection;
  }

  protected convertDateFromClient<T extends IServiceOrderType | NewServiceOrderType | PartialUpdateServiceOrderType>(
    serviceOrderType: T,
  ): RestOf<T> {
    return {
      ...serviceOrderType,
      createdDate: serviceOrderType.createdDate?.toJSON() ?? null,
      lastModifiedDate: serviceOrderType.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceOrderType: RestServiceOrderType): IServiceOrderType {
    return {
      ...restServiceOrderType,
      createdDate: restServiceOrderType.createdDate ? dayjs(restServiceOrderType.createdDate) : undefined,
      lastModifiedDate: restServiceOrderType.lastModifiedDate ? dayjs(restServiceOrderType.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceOrderType>): HttpResponse<IServiceOrderType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceOrderType[]>): HttpResponse<IServiceOrderType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

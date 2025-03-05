import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicle, NewVehicle } from '../vehicle.model';

export type PartialUpdateVehicle = Partial<IVehicle> & Pick<IVehicle, 'id'>;

type RestOf<T extends IVehicle | NewVehicle> = Omit<
  T,
  'fabricationDate' | 'lastMaintenanceDate' | 'nextMaintenanceDate' | 'createdDate' | 'lastModifiedDate'
> & {
  fabricationDate?: string | null;
  lastMaintenanceDate?: string | null;
  nextMaintenanceDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestVehicle = RestOf<IVehicle>;

export type NewRestVehicle = RestOf<NewVehicle>;

export type PartialUpdateRestVehicle = RestOf<PartialUpdateVehicle>;

export type EntityResponseType = HttpResponse<IVehicle>;
export type EntityArrayResponseType = HttpResponse<IVehicle[]>;

@Injectable({ providedIn: 'root' })
export class VehicleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicles');

  create(vehicle: NewVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .post<RestVehicle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vehicle: IVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .put<RestVehicle>(`${this.resourceUrl}/${this.getVehicleIdentifier(vehicle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vehicle: PartialUpdateVehicle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicle);
    return this.http
      .patch<RestVehicle>(`${this.resourceUrl}/${this.getVehicleIdentifier(vehicle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVehicle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVehicle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleIdentifier(vehicle: Pick<IVehicle, 'id'>): number {
    return vehicle.id;
  }

  compareVehicle(o1: Pick<IVehicle, 'id'> | null, o2: Pick<IVehicle, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleIdentifier(o1) === this.getVehicleIdentifier(o2) : o1 === o2;
  }

  addVehicleToCollectionIfMissing<Type extends Pick<IVehicle, 'id'>>(
    vehicleCollection: Type[],
    ...vehiclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicles: Type[] = vehiclesToCheck.filter(isPresent);
    if (vehicles.length > 0) {
      const vehicleCollectionIdentifiers = vehicleCollection.map(vehicleItem => this.getVehicleIdentifier(vehicleItem));
      const vehiclesToAdd = vehicles.filter(vehicleItem => {
        const vehicleIdentifier = this.getVehicleIdentifier(vehicleItem);
        if (vehicleCollectionIdentifiers.includes(vehicleIdentifier)) {
          return false;
        }
        vehicleCollectionIdentifiers.push(vehicleIdentifier);
        return true;
      });
      return [...vehiclesToAdd, ...vehicleCollection];
    }
    return vehicleCollection;
  }

  protected convertDateFromClient<T extends IVehicle | NewVehicle | PartialUpdateVehicle>(vehicle: T): RestOf<T> {
    return {
      ...vehicle,
      fabricationDate: vehicle.fabricationDate?.format(DATE_FORMAT) ?? null,
      lastMaintenanceDate: vehicle.lastMaintenanceDate?.format(DATE_FORMAT) ?? null,
      nextMaintenanceDate: vehicle.nextMaintenanceDate?.format(DATE_FORMAT) ?? null,
      createdDate: vehicle.createdDate?.toJSON() ?? null,
      lastModifiedDate: vehicle.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVehicle: RestVehicle): IVehicle {
    return {
      ...restVehicle,
      fabricationDate: restVehicle.fabricationDate ? dayjs(restVehicle.fabricationDate) : undefined,
      lastMaintenanceDate: restVehicle.lastMaintenanceDate ? dayjs(restVehicle.lastMaintenanceDate) : undefined,
      nextMaintenanceDate: restVehicle.nextMaintenanceDate ? dayjs(restVehicle.nextMaintenanceDate) : undefined,
      createdDate: restVehicle.createdDate ? dayjs(restVehicle.createdDate) : undefined,
      lastModifiedDate: restVehicle.lastModifiedDate ? dayjs(restVehicle.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVehicle>): HttpResponse<IVehicle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVehicle[]>): HttpResponse<IVehicle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

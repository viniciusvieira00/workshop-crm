import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceOrder, NewServiceOrder } from '../service-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceOrder for edit and NewServiceOrderFormGroupInput for create.
 */
type ServiceOrderFormGroupInput = IServiceOrder | PartialWithRequiredKeyOf<NewServiceOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceOrder | NewServiceOrder> = Omit<
  T,
  'creationDate' | 'startDate' | 'completionDate' | 'createdDate' | 'lastModifiedDate'
> & {
  creationDate?: string | null;
  startDate?: string | null;
  completionDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ServiceOrderFormRawValue = FormValueOf<IServiceOrder>;

type NewServiceOrderFormRawValue = FormValueOf<NewServiceOrder>;

type ServiceOrderFormDefaults = Pick<
  NewServiceOrder,
  'id' | 'creationDate' | 'startDate' | 'completionDate' | 'createdDate' | 'lastModifiedDate' | 'products'
>;

type ServiceOrderFormGroupContent = {
  id: FormControl<ServiceOrderFormRawValue['id'] | NewServiceOrder['id']>;
  creationDate: FormControl<ServiceOrderFormRawValue['creationDate']>;
  startDate: FormControl<ServiceOrderFormRawValue['startDate']>;
  completionDate: FormControl<ServiceOrderFormRawValue['completionDate']>;
  initialCost: FormControl<ServiceOrderFormRawValue['initialCost']>;
  additionalCost: FormControl<ServiceOrderFormRawValue['additionalCost']>;
  totalCost: FormControl<ServiceOrderFormRawValue['totalCost']>;
  notes: FormControl<ServiceOrderFormRawValue['notes']>;
  status: FormControl<ServiceOrderFormRawValue['status']>;
  createdBy: FormControl<ServiceOrderFormRawValue['createdBy']>;
  createdDate: FormControl<ServiceOrderFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ServiceOrderFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ServiceOrderFormRawValue['lastModifiedDate']>;
  type: FormControl<ServiceOrderFormRawValue['type']>;
  vehicle: FormControl<ServiceOrderFormRawValue['vehicle']>;
  products: FormControl<ServiceOrderFormRawValue['products']>;
};

export type ServiceOrderFormGroup = FormGroup<ServiceOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceOrderFormService {
  createServiceOrderFormGroup(serviceOrder: ServiceOrderFormGroupInput = { id: null }): ServiceOrderFormGroup {
    const serviceOrderRawValue = this.convertServiceOrderToServiceOrderRawValue({
      ...this.getFormDefaults(),
      ...serviceOrder,
    });
    return new FormGroup<ServiceOrderFormGroupContent>({
      id: new FormControl(
        { value: serviceOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      creationDate: new FormControl(serviceOrderRawValue.creationDate),
      startDate: new FormControl(serviceOrderRawValue.startDate),
      completionDate: new FormControl(serviceOrderRawValue.completionDate),
      initialCost: new FormControl(serviceOrderRawValue.initialCost, {
        validators: [Validators.min(0)],
      }),
      additionalCost: new FormControl(serviceOrderRawValue.additionalCost, {
        validators: [Validators.min(0)],
      }),
      totalCost: new FormControl(serviceOrderRawValue.totalCost, {
        validators: [Validators.min(0)],
      }),
      notes: new FormControl(serviceOrderRawValue.notes, {
        validators: [Validators.maxLength(2000)],
      }),
      status: new FormControl(serviceOrderRawValue.status, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(serviceOrderRawValue.createdBy),
      createdDate: new FormControl(serviceOrderRawValue.createdDate),
      lastModifiedBy: new FormControl(serviceOrderRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(serviceOrderRawValue.lastModifiedDate),
      type: new FormControl(serviceOrderRawValue.type, {
        validators: [Validators.required],
      }),
      vehicle: new FormControl(serviceOrderRawValue.vehicle, {
        validators: [Validators.required],
      }),
      products: new FormControl(serviceOrderRawValue.products ?? []),
    });
  }

  getServiceOrder(form: ServiceOrderFormGroup): IServiceOrder | NewServiceOrder {
    return this.convertServiceOrderRawValueToServiceOrder(form.getRawValue() as ServiceOrderFormRawValue | NewServiceOrderFormRawValue);
  }

  resetForm(form: ServiceOrderFormGroup, serviceOrder: ServiceOrderFormGroupInput): void {
    const serviceOrderRawValue = this.convertServiceOrderToServiceOrderRawValue({ ...this.getFormDefaults(), ...serviceOrder });
    form.reset(
      {
        ...serviceOrderRawValue,
        id: { value: serviceOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      startDate: currentTime,
      completionDate: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      products: [],
    };
  }

  private convertServiceOrderRawValueToServiceOrder(
    rawServiceOrder: ServiceOrderFormRawValue | NewServiceOrderFormRawValue,
  ): IServiceOrder | NewServiceOrder {
    return {
      ...rawServiceOrder,
      creationDate: dayjs(rawServiceOrder.creationDate, DATE_TIME_FORMAT),
      startDate: dayjs(rawServiceOrder.startDate, DATE_TIME_FORMAT),
      completionDate: dayjs(rawServiceOrder.completionDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawServiceOrder.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawServiceOrder.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertServiceOrderToServiceOrderRawValue(
    serviceOrder: IServiceOrder | (Partial<NewServiceOrder> & ServiceOrderFormDefaults),
  ): ServiceOrderFormRawValue | PartialWithRequiredKeyOf<NewServiceOrderFormRawValue> {
    return {
      ...serviceOrder,
      creationDate: serviceOrder.creationDate ? serviceOrder.creationDate.format(DATE_TIME_FORMAT) : undefined,
      startDate: serviceOrder.startDate ? serviceOrder.startDate.format(DATE_TIME_FORMAT) : undefined,
      completionDate: serviceOrder.completionDate ? serviceOrder.completionDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: serviceOrder.createdDate ? serviceOrder.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: serviceOrder.lastModifiedDate ? serviceOrder.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      products: serviceOrder.products ?? [],
    };
  }
}

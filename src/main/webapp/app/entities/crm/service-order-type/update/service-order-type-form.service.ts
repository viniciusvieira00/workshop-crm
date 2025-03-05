import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceOrderType, NewServiceOrderType } from '../service-order-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceOrderType for edit and NewServiceOrderTypeFormGroupInput for create.
 */
type ServiceOrderTypeFormGroupInput = IServiceOrderType | PartialWithRequiredKeyOf<NewServiceOrderType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceOrderType | NewServiceOrderType> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ServiceOrderTypeFormRawValue = FormValueOf<IServiceOrderType>;

type NewServiceOrderTypeFormRawValue = FormValueOf<NewServiceOrderType>;

type ServiceOrderTypeFormDefaults = Pick<NewServiceOrderType, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ServiceOrderTypeFormGroupContent = {
  id: FormControl<ServiceOrderTypeFormRawValue['id'] | NewServiceOrderType['id']>;
  name: FormControl<ServiceOrderTypeFormRawValue['name']>;
  description: FormControl<ServiceOrderTypeFormRawValue['description']>;
  price: FormControl<ServiceOrderTypeFormRawValue['price']>;
  createdBy: FormControl<ServiceOrderTypeFormRawValue['createdBy']>;
  createdDate: FormControl<ServiceOrderTypeFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ServiceOrderTypeFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ServiceOrderTypeFormRawValue['lastModifiedDate']>;
};

export type ServiceOrderTypeFormGroup = FormGroup<ServiceOrderTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceOrderTypeFormService {
  createServiceOrderTypeFormGroup(serviceOrderType: ServiceOrderTypeFormGroupInput = { id: null }): ServiceOrderTypeFormGroup {
    const serviceOrderTypeRawValue = this.convertServiceOrderTypeToServiceOrderTypeRawValue({
      ...this.getFormDefaults(),
      ...serviceOrderType,
    });
    return new FormGroup<ServiceOrderTypeFormGroupContent>({
      id: new FormControl(
        { value: serviceOrderTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(serviceOrderTypeRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(100)],
      }),
      description: new FormControl(serviceOrderTypeRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      price: new FormControl(serviceOrderTypeRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      createdBy: new FormControl(serviceOrderTypeRawValue.createdBy),
      createdDate: new FormControl(serviceOrderTypeRawValue.createdDate),
      lastModifiedBy: new FormControl(serviceOrderTypeRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(serviceOrderTypeRawValue.lastModifiedDate),
    });
  }

  getServiceOrderType(form: ServiceOrderTypeFormGroup): IServiceOrderType | NewServiceOrderType {
    return this.convertServiceOrderTypeRawValueToServiceOrderType(
      form.getRawValue() as ServiceOrderTypeFormRawValue | NewServiceOrderTypeFormRawValue,
    );
  }

  resetForm(form: ServiceOrderTypeFormGroup, serviceOrderType: ServiceOrderTypeFormGroupInput): void {
    const serviceOrderTypeRawValue = this.convertServiceOrderTypeToServiceOrderTypeRawValue({
      ...this.getFormDefaults(),
      ...serviceOrderType,
    });
    form.reset(
      {
        ...serviceOrderTypeRawValue,
        id: { value: serviceOrderTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceOrderTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertServiceOrderTypeRawValueToServiceOrderType(
    rawServiceOrderType: ServiceOrderTypeFormRawValue | NewServiceOrderTypeFormRawValue,
  ): IServiceOrderType | NewServiceOrderType {
    return {
      ...rawServiceOrderType,
      createdDate: dayjs(rawServiceOrderType.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawServiceOrderType.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertServiceOrderTypeToServiceOrderTypeRawValue(
    serviceOrderType: IServiceOrderType | (Partial<NewServiceOrderType> & ServiceOrderTypeFormDefaults),
  ): ServiceOrderTypeFormRawValue | PartialWithRequiredKeyOf<NewServiceOrderTypeFormRawValue> {
    return {
      ...serviceOrderType,
      createdDate: serviceOrderType.createdDate ? serviceOrderType.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: serviceOrderType.lastModifiedDate ? serviceOrderType.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

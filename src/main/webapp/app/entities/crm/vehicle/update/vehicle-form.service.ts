import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVehicle, NewVehicle } from '../vehicle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicle for edit and NewVehicleFormGroupInput for create.
 */
type VehicleFormGroupInput = IVehicle | PartialWithRequiredKeyOf<NewVehicle>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVehicle | NewVehicle> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type VehicleFormRawValue = FormValueOf<IVehicle>;

type NewVehicleFormRawValue = FormValueOf<NewVehicle>;

type VehicleFormDefaults = Pick<NewVehicle, 'id' | 'createdDate' | 'lastModifiedDate'>;

type VehicleFormGroupContent = {
  id: FormControl<VehicleFormRawValue['id'] | NewVehicle['id']>;
  licensePlate: FormControl<VehicleFormRawValue['licensePlate']>;
  model: FormControl<VehicleFormRawValue['model']>;
  brand: FormControl<VehicleFormRawValue['brand']>;
  fabricationDate: FormControl<VehicleFormRawValue['fabricationDate']>;
  modelYear: FormControl<VehicleFormRawValue['modelYear']>;
  color: FormControl<VehicleFormRawValue['color']>;
  renavam: FormControl<VehicleFormRawValue['renavam']>;
  fuelType: FormControl<VehicleFormRawValue['fuelType']>;
  chassiNumber: FormControl<VehicleFormRawValue['chassiNumber']>;
  currentMileage: FormControl<VehicleFormRawValue['currentMileage']>;
  lastMaintenanceDate: FormControl<VehicleFormRawValue['lastMaintenanceDate']>;
  lastMaintenanceMileage: FormControl<VehicleFormRawValue['lastMaintenanceMileage']>;
  nextMaintenanceDate: FormControl<VehicleFormRawValue['nextMaintenanceDate']>;
  nextMaintenanceMileage: FormControl<VehicleFormRawValue['nextMaintenanceMileage']>;
  description: FormControl<VehicleFormRawValue['description']>;
  status: FormControl<VehicleFormRawValue['status']>;
  createdBy: FormControl<VehicleFormRawValue['createdBy']>;
  createdDate: FormControl<VehicleFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<VehicleFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<VehicleFormRawValue['lastModifiedDate']>;
  client: FormControl<VehicleFormRawValue['client']>;
};

export type VehicleFormGroup = FormGroup<VehicleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleFormService {
  createVehicleFormGroup(vehicle: VehicleFormGroupInput = { id: null }): VehicleFormGroup {
    const vehicleRawValue = this.convertVehicleToVehicleRawValue({
      ...this.getFormDefaults(),
      ...vehicle,
    });
    return new FormGroup<VehicleFormGroupContent>({
      id: new FormControl(
        { value: vehicleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      licensePlate: new FormControl(vehicleRawValue.licensePlate, {
        validators: [Validators.required, Validators.pattern('^[A-Z0-9]{7}$')],
      }),
      model: new FormControl(vehicleRawValue.model, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(50)],
      }),
      brand: new FormControl(vehicleRawValue.brand, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(50)],
      }),
      fabricationDate: new FormControl(vehicleRawValue.fabricationDate, {
        validators: [Validators.required],
      }),
      modelYear: new FormControl(vehicleRawValue.modelYear, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      color: new FormControl(vehicleRawValue.color, {
        validators: [Validators.required],
      }),
      renavam: new FormControl(vehicleRawValue.renavam, {
        validators: [Validators.pattern('^[0-9]{9,11}$')],
      }),
      fuelType: new FormControl(vehicleRawValue.fuelType, {
        validators: [Validators.required],
      }),
      chassiNumber: new FormControl(vehicleRawValue.chassiNumber, {
        validators: [Validators.pattern('^[A-Z0-9]{17}$')],
      }),
      currentMileage: new FormControl(vehicleRawValue.currentMileage, {
        validators: [Validators.required, Validators.min(0)],
      }),
      lastMaintenanceDate: new FormControl(vehicleRawValue.lastMaintenanceDate),
      lastMaintenanceMileage: new FormControl(vehicleRawValue.lastMaintenanceMileage, {
        validators: [Validators.min(0)],
      }),
      nextMaintenanceDate: new FormControl(vehicleRawValue.nextMaintenanceDate),
      nextMaintenanceMileage: new FormControl(vehicleRawValue.nextMaintenanceMileage, {
        validators: [Validators.min(0)],
      }),
      description: new FormControl(vehicleRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      status: new FormControl(vehicleRawValue.status, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(vehicleRawValue.createdBy),
      createdDate: new FormControl(vehicleRawValue.createdDate),
      lastModifiedBy: new FormControl(vehicleRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(vehicleRawValue.lastModifiedDate),
      client: new FormControl(vehicleRawValue.client, {
        validators: [Validators.required],
      }),
    });
  }

  getVehicle(form: VehicleFormGroup): IVehicle | NewVehicle {
    return this.convertVehicleRawValueToVehicle(form.getRawValue() as VehicleFormRawValue | NewVehicleFormRawValue);
  }

  resetForm(form: VehicleFormGroup, vehicle: VehicleFormGroupInput): void {
    const vehicleRawValue = this.convertVehicleToVehicleRawValue({ ...this.getFormDefaults(), ...vehicle });
    form.reset(
      {
        ...vehicleRawValue,
        id: { value: vehicleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertVehicleRawValueToVehicle(rawVehicle: VehicleFormRawValue | NewVehicleFormRawValue): IVehicle | NewVehicle {
    return {
      ...rawVehicle,
      createdDate: dayjs(rawVehicle.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawVehicle.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertVehicleToVehicleRawValue(
    vehicle: IVehicle | (Partial<NewVehicle> & VehicleFormDefaults),
  ): VehicleFormRawValue | PartialWithRequiredKeyOf<NewVehicleFormRawValue> {
    return {
      ...vehicle,
      createdDate: vehicle.createdDate ? vehicle.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: vehicle.lastModifiedDate ? vehicle.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

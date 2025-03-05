import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IClient | NewClient> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ClientFormRawValue = FormValueOf<IClient>;

type NewClientFormRawValue = FormValueOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ClientFormGroupContent = {
  id: FormControl<ClientFormRawValue['id'] | NewClient['id']>;
  documentNumber: FormControl<ClientFormRawValue['documentNumber']>;
  name: FormControl<ClientFormRawValue['name']>;
  email: FormControl<ClientFormRawValue['email']>;
  phoneNumber: FormControl<ClientFormRawValue['phoneNumber']>;
  alternativePhoneNumber: FormControl<ClientFormRawValue['alternativePhoneNumber']>;
  address: FormControl<ClientFormRawValue['address']>;
  city: FormControl<ClientFormRawValue['city']>;
  state: FormControl<ClientFormRawValue['state']>;
  zipCode: FormControl<ClientFormRawValue['zipCode']>;
  clientType: FormControl<ClientFormRawValue['clientType']>;
  notes: FormControl<ClientFormRawValue['notes']>;
  createdBy: FormControl<ClientFormRawValue['createdBy']>;
  createdDate: FormControl<ClientFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ClientFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ClientFormRawValue['lastModifiedDate']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = this.convertClientToClientRawValue({
      ...this.getFormDefaults(),
      ...client,
    });
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentNumber: new FormControl(clientRawValue.documentNumber, {
        validators: [Validators.required, Validators.pattern('^[0-9]{11,14}$')],
      }),
      name: new FormControl(clientRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      email: new FormControl(clientRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      phoneNumber: new FormControl(clientRawValue.phoneNumber, {
        validators: [Validators.required, Validators.pattern('^[0-9]{10,11}$')],
      }),
      alternativePhoneNumber: new FormControl(clientRawValue.alternativePhoneNumber, {
        validators: [Validators.pattern('^[0-9]{10,11}$')],
      }),
      address: new FormControl(clientRawValue.address, {
        validators: [Validators.required],
      }),
      city: new FormControl(clientRawValue.city, {
        validators: [Validators.required],
      }),
      state: new FormControl(clientRawValue.state, {
        validators: [Validators.required],
      }),
      zipCode: new FormControl(clientRawValue.zipCode, {
        validators: [Validators.required, Validators.pattern('^[0-9]{8}$')],
      }),
      clientType: new FormControl(clientRawValue.clientType, {
        validators: [Validators.required],
      }),
      notes: new FormControl(clientRawValue.notes, {
        validators: [Validators.maxLength(1000)],
      }),
      createdBy: new FormControl(clientRawValue.createdBy),
      createdDate: new FormControl(clientRawValue.createdDate),
      lastModifiedBy: new FormControl(clientRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(clientRawValue.lastModifiedDate),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return this.convertClientRawValueToClient(form.getRawValue() as ClientFormRawValue | NewClientFormRawValue);
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertClientRawValueToClient(rawClient: ClientFormRawValue | NewClientFormRawValue): IClient | NewClient {
    return {
      ...rawClient,
      createdDate: dayjs(rawClient.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawClient.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertClientToClientRawValue(
    client: IClient | (Partial<NewClient> & ClientFormDefaults),
  ): ClientFormRawValue | PartialWithRequiredKeyOf<NewClientFormRawValue> {
    return {
      ...client,
      createdDate: client.createdDate ? client.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: client.lastModifiedDate ? client.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

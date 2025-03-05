import dayjs from 'dayjs/esm';
import { ClientType } from 'app/entities/enumerations/client-type.model';

export interface IClient {
  id: number;
  documentNumber?: string | null;
  name?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  alternativePhoneNumber?: string | null;
  address?: string | null;
  city?: string | null;
  state?: string | null;
  zipCode?: string | null;
  clientType?: keyof typeof ClientType | null;
  notes?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };

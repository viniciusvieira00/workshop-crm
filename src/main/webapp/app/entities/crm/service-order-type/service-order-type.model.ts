import dayjs from 'dayjs/esm';

export interface IServiceOrderType {
  id: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewServiceOrderType = Omit<IServiceOrderType, 'id'> & { id: null };

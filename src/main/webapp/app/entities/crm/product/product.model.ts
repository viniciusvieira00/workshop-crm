import dayjs from 'dayjs/esm';
import { IServiceOrder } from 'app/entities/crm/service-order/service-order.model';

export interface IProduct {
  id: number;
  name?: string | null;
  description?: string | null;
  brand?: string | null;
  costPrice?: number | null;
  sellPrice?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  serviceOrders?: Pick<IServiceOrder, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };

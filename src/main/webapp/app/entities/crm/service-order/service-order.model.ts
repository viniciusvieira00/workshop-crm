import dayjs from 'dayjs/esm';
import { IServiceOrderType } from 'app/entities/crm/service-order-type/service-order-type.model';
import { IVehicle } from 'app/entities/crm/vehicle/vehicle.model';
import { IProduct } from 'app/entities/crm/product/product.model';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';

export interface IServiceOrder {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  completionDate?: dayjs.Dayjs | null;
  initialCost?: number | null;
  additionalCost?: number | null;
  totalCost?: number | null;
  notes?: string | null;
  status?: keyof typeof ServiceOrderStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  type?: Pick<IServiceOrderType, 'id' | 'name'> | null;
  vehicle?: Pick<IVehicle, 'id' | 'licensePlate' | 'client'> | null;
  products?: Pick<IProduct, 'id' | 'name' | 'brand' | 'sellPrice'>[] | null;
}

export type NewServiceOrder = Omit<IServiceOrder, 'id'> & { id: null };

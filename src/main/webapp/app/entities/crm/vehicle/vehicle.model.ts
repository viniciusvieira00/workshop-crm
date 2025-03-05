import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/crm/client/client.model';
import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';

export interface IVehicle {
  id: number;
  licensePlate?: string | null;
  model?: string | null;
  brand?: string | null;
  fabricationDate?: dayjs.Dayjs | null;
  modelYear?: number | null;
  color?: string | null;
  renavam?: string | null;
  fuelType?: string | null;
  chassiNumber?: string | null;
  currentMileage?: number | null;
  lastMaintenanceDate?: dayjs.Dayjs | null;
  lastMaintenanceMileage?: number | null;
  nextMaintenanceDate?: dayjs.Dayjs | null;
  nextMaintenanceMileage?: number | null;
  description?: string | null;
  status?: keyof typeof VehicleStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  client?: Pick<IClient, 'id' | 'name'> | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };

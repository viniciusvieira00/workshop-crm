import dayjs from 'dayjs/esm';

import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 7486,
  licensePlate: 'J0ENQ97',
  model: 'even yuck astride',
  brand: 'faint neaten roger',
  fabricationDate: dayjs('2025-03-05'),
  modelYear: 1958,
  color: 'turquesa',
  fuelType: 'gratefully uselessly',
  currentMileage: 27466,
  status: 'AVAILABLE',
};

export const sampleWithPartialData: IVehicle = {
  id: 16142,
  licensePlate: '5RALX8T',
  model: 'boo carefully',
  brand: 'whenever freight impressive',
  fabricationDate: dayjs('2025-03-04'),
  modelYear: 2073,
  color: 'coral',
  fuelType: 'pro behold ack',
  chassiNumber: 'Y4M1G93K8UEM6O7KG',
  currentMileage: 3950,
  lastMaintenanceDate: dayjs('2025-03-04'),
  description: 'sedately',
  status: 'WAITING_MAINTENANCE',
  createdBy: 'brr outfox decide',
  lastModifiedBy: 'athletic psst over',
};

export const sampleWithFullData: IVehicle = {
  id: 12947,
  licensePlate: '212JIX5',
  model: 'boo broken',
  brand: 'busily',
  fabricationDate: dayjs('2025-03-04'),
  modelYear: 1994,
  color: 'castanho',
  renavam: '2828100705',
  fuelType: 'extroverted gadzooks astride',
  chassiNumber: '4BGAVXLC7BRAHTH74',
  currentMileage: 785,
  lastMaintenanceDate: dayjs('2025-03-04'),
  lastMaintenanceMileage: 2316,
  nextMaintenanceDate: dayjs('2025-03-04'),
  nextMaintenanceMileage: 15236,
  description: 'giving homely redress',
  status: 'UNDER_MAINTENANCE',
  createdBy: 'save amend',
  createdDate: dayjs('2025-03-04T12:41'),
  lastModifiedBy: 'yum yahoo',
  lastModifiedDate: dayjs('2025-03-04T21:22'),
};

export const sampleWithNewData: NewVehicle = {
  licensePlate: 'MB8C23J',
  model: 'glisten transcend yippee',
  brand: 'reproach',
  fabricationDate: dayjs('2025-03-04'),
  modelYear: 2058,
  color: 'bronze',
  fuelType: 'stylish',
  currentMileage: 2221,
  status: 'OUT_OF_SERVICE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

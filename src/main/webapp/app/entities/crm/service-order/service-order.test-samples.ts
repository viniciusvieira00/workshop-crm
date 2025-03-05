import dayjs from 'dayjs/esm';

import { IServiceOrder, NewServiceOrder } from './service-order.model';

export const sampleWithRequiredData: IServiceOrder = {
  id: 20247,
  creationDate: dayjs('2025-03-04T14:49'),
  status: 'CREATED',
};

export const sampleWithPartialData: IServiceOrder = {
  id: 9237,
  creationDate: dayjs('2025-03-05T05:08'),
  additionalCost: 14859.15,
  status: 'CREATED',
  createdBy: 'hydrolyse until phooey',
};

export const sampleWithFullData: IServiceOrder = {
  id: 5482,
  creationDate: dayjs('2025-03-04T09:01'),
  startDate: dayjs('2025-03-05T02:49'),
  completionDate: dayjs('2025-03-05T05:26'),
  initialCost: 1296.44,
  additionalCost: 12317.37,
  totalCost: 14885.2,
  notes: 'elver',
  status: 'CREATED',
  createdBy: 'distant sadly',
  createdDate: dayjs('2025-03-04T16:13'),
  lastModifiedBy: 'eventually meh daughter',
  lastModifiedDate: dayjs('2025-03-04T14:32'),
};

export const sampleWithNewData: NewServiceOrder = {
  creationDate: dayjs('2025-03-04T06:16'),
  status: 'CANCELED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

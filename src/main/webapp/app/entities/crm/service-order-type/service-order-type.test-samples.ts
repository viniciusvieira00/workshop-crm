import dayjs from 'dayjs/esm';

import { IServiceOrderType, NewServiceOrderType } from './service-order-type.model';

export const sampleWithRequiredData: IServiceOrderType = {
  id: 26987,
  name: 'acknowledge inside ugh',
  price: 8661.27,
};

export const sampleWithPartialData: IServiceOrderType = {
  id: 25583,
  name: 'yum kissingly under',
  price: 27317.14,
  createdDate: dayjs('2025-03-05T01:13'),
  lastModifiedDate: dayjs('2025-03-04T10:23'),
};

export const sampleWithFullData: IServiceOrderType = {
  id: 1907,
  name: 'fly accompany inside',
  description: 'whoever majestic apud',
  price: 14353.39,
  createdBy: 'valley whereas rigidly',
  createdDate: dayjs('2025-03-04T10:49'),
  lastModifiedBy: 'upliftingly',
  lastModifiedDate: dayjs('2025-03-04T21:45'),
};

export const sampleWithNewData: NewServiceOrderType = {
  name: 'consequently than',
  price: 21506.05,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

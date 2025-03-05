import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
  sellPrice: 17510.56,
};

export const sampleWithPartialData: IProduct = {
  id: 16118,
  name: 'now till',
  brand: 'um incinerate yowza',
  sellPrice: 319.09,
  lastModifiedDate: dayjs('2025-03-04T18:30'),
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  description: 'along amongst',
  brand: 'hm ugh',
  costPrice: 2989.07,
  sellPrice: 1604.77,
  createdBy: 'inasmuch a partially',
  createdDate: dayjs('2025-03-05T03:22'),
  lastModifiedBy: 'trust drowse',
  lastModifiedDate: dayjs('2025-03-04T11:40'),
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  sellPrice: 26562.23,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import dayjs from 'dayjs/esm';

import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 16289,
  documentNumber: '15860674081521',
  name: 'whoever including seriously',
  email: '{UrA$2@rb^|&.4X',
  phoneNumber: '2909474285',
  address: 'softly mallard',
  city: 'Xavier de Nossa Senhora',
  state: 'mouser',
  zipCode: '96624580',
  clientType: 'INDIVIDUAL',
};

export const sampleWithPartialData: IClient = {
  id: 27864,
  documentNumber: '295205846804',
  name: 'harvest from concerning',
  email: '2^@8.J}',
  phoneNumber: '09472402032',
  alternativePhoneNumber: '00938824369',
  address: 'colorfully coaxingly pish',
  city: 'Braga do Sul',
  state: 'boo drat',
  zipCode: '14588613',
  clientType: 'COMPANY',
  notes: 'now how',
  createdBy: 'yet mathematics huzzah',
  createdDate: dayjs('2025-03-04T18:04'),
  lastModifiedBy: 'ew openly ack',
  lastModifiedDate: dayjs('2025-03-04T11:10'),
};

export const sampleWithFullData: IClient = {
  id: 31496,
  documentNumber: '03849217904758',
  name: 'mean',
  email: '?{>-V^@<\\z8s.!',
  phoneNumber: '83893065958',
  alternativePhoneNumber: '68824607095',
  address: 'clinch whose',
  city: 'Kléber do Descoberto',
  state: 'adrenalin brr outrageous',
  zipCode: '98657005',
  clientType: 'INDIVIDUAL',
  notes: 'aha',
  createdBy: 'qualified',
  createdDate: dayjs('2025-03-04T05:06'),
  lastModifiedBy: 'crossly aw',
  lastModifiedDate: dayjs('2025-03-04T18:32'),
};

export const sampleWithNewData: NewClient = {
  documentNumber: '781122743905',
  name: 'monumental',
  email: 'J;f=]@zxsy.&i',
  phoneNumber: '03876855561',
  address: 'for',
  city: 'Marcos do Sul',
  state: 'powerless',
  zipCode: '87516875',
  clientType: 'INDIVIDUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

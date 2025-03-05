import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IServiceOrder } from '../service-order.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-order.test-samples';

import { RestServiceOrder, ServiceOrderService } from './service-order.service';

const requireRestSample: RestServiceOrder = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  completionDate: sampleWithRequiredData.completionDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ServiceOrder Service', () => {
  let service: ServiceOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceOrder | IServiceOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceOrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ServiceOrder', () => {
      const serviceOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceOrder', () => {
      const serviceOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceOrderToCollectionIfMissing', () => {
      it('should add a ServiceOrder to an empty array', () => {
        const serviceOrder: IServiceOrder = sampleWithRequiredData;
        expectedResult = service.addServiceOrderToCollectionIfMissing([], serviceOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOrder);
      });

      it('should not add a ServiceOrder to an array that contains it', () => {
        const serviceOrder: IServiceOrder = sampleWithRequiredData;
        const serviceOrderCollection: IServiceOrder[] = [
          {
            ...serviceOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceOrderToCollectionIfMissing(serviceOrderCollection, serviceOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceOrder to an array that doesn't contain it", () => {
        const serviceOrder: IServiceOrder = sampleWithRequiredData;
        const serviceOrderCollection: IServiceOrder[] = [sampleWithPartialData];
        expectedResult = service.addServiceOrderToCollectionIfMissing(serviceOrderCollection, serviceOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOrder);
      });

      it('should add only unique ServiceOrder to an array', () => {
        const serviceOrderArray: IServiceOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceOrderCollection: IServiceOrder[] = [sampleWithRequiredData];
        expectedResult = service.addServiceOrderToCollectionIfMissing(serviceOrderCollection, ...serviceOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceOrder: IServiceOrder = sampleWithRequiredData;
        const serviceOrder2: IServiceOrder = sampleWithPartialData;
        expectedResult = service.addServiceOrderToCollectionIfMissing([], serviceOrder, serviceOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOrder);
        expect(expectedResult).toContain(serviceOrder2);
      });

      it('should accept null and undefined values', () => {
        const serviceOrder: IServiceOrder = sampleWithRequiredData;
        expectedResult = service.addServiceOrderToCollectionIfMissing([], null, serviceOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOrder);
      });

      it('should return initial array if no ServiceOrder is added', () => {
        const serviceOrderCollection: IServiceOrder[] = [sampleWithRequiredData];
        expectedResult = service.addServiceOrderToCollectionIfMissing(serviceOrderCollection, undefined, null);
        expect(expectedResult).toEqual(serviceOrderCollection);
      });
    });

    describe('compareServiceOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 9582 };
        const entity2 = null;

        const compareResult1 = service.compareServiceOrder(entity1, entity2);
        const compareResult2 = service.compareServiceOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 9582 };
        const entity2 = { id: 32224 };

        const compareResult1 = service.compareServiceOrder(entity1, entity2);
        const compareResult2 = service.compareServiceOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 9582 };
        const entity2 = { id: 9582 };

        const compareResult1 = service.compareServiceOrder(entity1, entity2);
        const compareResult2 = service.compareServiceOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

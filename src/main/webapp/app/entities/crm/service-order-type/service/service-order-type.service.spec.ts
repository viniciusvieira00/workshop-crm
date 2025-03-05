import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IServiceOrderType } from '../service-order-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-order-type.test-samples';

import { RestServiceOrderType, ServiceOrderTypeService } from './service-order-type.service';

const requireRestSample: RestServiceOrderType = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ServiceOrderType Service', () => {
  let service: ServiceOrderTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceOrderType | IServiceOrderType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceOrderTypeService);
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

    it('should create a ServiceOrderType', () => {
      const serviceOrderType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceOrderType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceOrderType', () => {
      const serviceOrderType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceOrderType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceOrderType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceOrderType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceOrderType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceOrderTypeToCollectionIfMissing', () => {
      it('should add a ServiceOrderType to an empty array', () => {
        const serviceOrderType: IServiceOrderType = sampleWithRequiredData;
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing([], serviceOrderType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOrderType);
      });

      it('should not add a ServiceOrderType to an array that contains it', () => {
        const serviceOrderType: IServiceOrderType = sampleWithRequiredData;
        const serviceOrderTypeCollection: IServiceOrderType[] = [
          {
            ...serviceOrderType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing(serviceOrderTypeCollection, serviceOrderType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceOrderType to an array that doesn't contain it", () => {
        const serviceOrderType: IServiceOrderType = sampleWithRequiredData;
        const serviceOrderTypeCollection: IServiceOrderType[] = [sampleWithPartialData];
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing(serviceOrderTypeCollection, serviceOrderType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOrderType);
      });

      it('should add only unique ServiceOrderType to an array', () => {
        const serviceOrderTypeArray: IServiceOrderType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceOrderTypeCollection: IServiceOrderType[] = [sampleWithRequiredData];
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing(serviceOrderTypeCollection, ...serviceOrderTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceOrderType: IServiceOrderType = sampleWithRequiredData;
        const serviceOrderType2: IServiceOrderType = sampleWithPartialData;
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing([], serviceOrderType, serviceOrderType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOrderType);
        expect(expectedResult).toContain(serviceOrderType2);
      });

      it('should accept null and undefined values', () => {
        const serviceOrderType: IServiceOrderType = sampleWithRequiredData;
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing([], null, serviceOrderType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOrderType);
      });

      it('should return initial array if no ServiceOrderType is added', () => {
        const serviceOrderTypeCollection: IServiceOrderType[] = [sampleWithRequiredData];
        expectedResult = service.addServiceOrderTypeToCollectionIfMissing(serviceOrderTypeCollection, undefined, null);
        expect(expectedResult).toEqual(serviceOrderTypeCollection);
      });
    });

    describe('compareServiceOrderType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceOrderType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 2331 };
        const entity2 = null;

        const compareResult1 = service.compareServiceOrderType(entity1, entity2);
        const compareResult2 = service.compareServiceOrderType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 2331 };
        const entity2 = { id: 8305 };

        const compareResult1 = service.compareServiceOrderType(entity1, entity2);
        const compareResult2 = service.compareServiceOrderType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 2331 };
        const entity2 = { id: 2331 };

        const compareResult1 = service.compareServiceOrderType(entity1, entity2);
        const compareResult2 = service.compareServiceOrderType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

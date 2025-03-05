import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-order.test-samples';

import { ServiceOrderFormService } from './service-order-form.service';

describe('ServiceOrder Form Service', () => {
  let service: ServiceOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceOrderFormService);
  });

  describe('Service methods', () => {
    describe('createServiceOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            startDate: expect.any(Object),
            completionDate: expect.any(Object),
            initialCost: expect.any(Object),
            additionalCost: expect.any(Object),
            totalCost: expect.any(Object),
            notes: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            type: expect.any(Object),
            vehicle: expect.any(Object),
            products: expect.any(Object),
          }),
        );
      });

      it('passing IServiceOrder should create a new form with FormGroup', () => {
        const formGroup = service.createServiceOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            startDate: expect.any(Object),
            completionDate: expect.any(Object),
            initialCost: expect.any(Object),
            additionalCost: expect.any(Object),
            totalCost: expect.any(Object),
            notes: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            type: expect.any(Object),
            vehicle: expect.any(Object),
            products: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceOrder', () => {
      it('should return NewServiceOrder for default ServiceOrder initial value', () => {
        const formGroup = service.createServiceOrderFormGroup(sampleWithNewData);

        const serviceOrder = service.getServiceOrder(formGroup) as any;

        expect(serviceOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceOrder for empty ServiceOrder initial value', () => {
        const formGroup = service.createServiceOrderFormGroup();

        const serviceOrder = service.getServiceOrder(formGroup) as any;

        expect(serviceOrder).toMatchObject({});
      });

      it('should return IServiceOrder', () => {
        const formGroup = service.createServiceOrderFormGroup(sampleWithRequiredData);

        const serviceOrder = service.getServiceOrder(formGroup) as any;

        expect(serviceOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceOrder should not enable id FormControl', () => {
        const formGroup = service.createServiceOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceOrder should disable id FormControl', () => {
        const formGroup = service.createServiceOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-order-type.test-samples';

import { ServiceOrderTypeFormService } from './service-order-type-form.service';

describe('ServiceOrderType Form Service', () => {
  let service: ServiceOrderTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceOrderTypeFormService);
  });

  describe('Service methods', () => {
    describe('createServiceOrderTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceOrderTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IServiceOrderType should create a new form with FormGroup', () => {
        const formGroup = service.createServiceOrderTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            price: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceOrderType', () => {
      it('should return NewServiceOrderType for default ServiceOrderType initial value', () => {
        const formGroup = service.createServiceOrderTypeFormGroup(sampleWithNewData);

        const serviceOrderType = service.getServiceOrderType(formGroup) as any;

        expect(serviceOrderType).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceOrderType for empty ServiceOrderType initial value', () => {
        const formGroup = service.createServiceOrderTypeFormGroup();

        const serviceOrderType = service.getServiceOrderType(formGroup) as any;

        expect(serviceOrderType).toMatchObject({});
      });

      it('should return IServiceOrderType', () => {
        const formGroup = service.createServiceOrderTypeFormGroup(sampleWithRequiredData);

        const serviceOrderType = service.getServiceOrderType(formGroup) as any;

        expect(serviceOrderType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceOrderType should not enable id FormControl', () => {
        const formGroup = service.createServiceOrderTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceOrderType should disable id FormControl', () => {
        const formGroup = service.createServiceOrderTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

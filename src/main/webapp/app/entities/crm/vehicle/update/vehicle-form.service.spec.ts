import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle.test-samples';

import { VehicleFormService } from './vehicle-form.service';

describe('Vehicle Form Service', () => {
  let service: VehicleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licensePlate: expect.any(Object),
            model: expect.any(Object),
            brand: expect.any(Object),
            fabricationDate: expect.any(Object),
            modelYear: expect.any(Object),
            color: expect.any(Object),
            renavam: expect.any(Object),
            fuelType: expect.any(Object),
            chassiNumber: expect.any(Object),
            currentMileage: expect.any(Object),
            lastMaintenanceDate: expect.any(Object),
            lastMaintenanceMileage: expect.any(Object),
            nextMaintenanceDate: expect.any(Object),
            nextMaintenanceMileage: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            client: expect.any(Object),
          }),
        );
      });

      it('passing IVehicle should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licensePlate: expect.any(Object),
            model: expect.any(Object),
            brand: expect.any(Object),
            fabricationDate: expect.any(Object),
            modelYear: expect.any(Object),
            color: expect.any(Object),
            renavam: expect.any(Object),
            fuelType: expect.any(Object),
            chassiNumber: expect.any(Object),
            currentMileage: expect.any(Object),
            lastMaintenanceDate: expect.any(Object),
            lastMaintenanceMileage: expect.any(Object),
            nextMaintenanceDate: expect.any(Object),
            nextMaintenanceMileage: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            client: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicle', () => {
      it('should return NewVehicle for default Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithNewData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicle for empty Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup();

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject({});
      });

      it('should return IVehicle', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicle should not enable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicle should disable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

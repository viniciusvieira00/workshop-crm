import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IServiceOrderType } from 'app/entities/crm/service-order-type/service-order-type.model';
import { ServiceOrderTypeService } from 'app/entities/crm/service-order-type/service/service-order-type.service';
import { IVehicle } from 'app/entities/crm/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/crm/vehicle/service/vehicle.service';
import { IProduct } from 'app/entities/crm/product/product.model';
import { ProductService } from 'app/entities/crm/product/service/product.service';
import { IServiceOrder } from '../service-order.model';
import { ServiceOrderService } from '../service/service-order.service';
import { ServiceOrderFormService } from './service-order-form.service';

import { ServiceOrderUpdateComponent } from './service-order-update.component';

describe('ServiceOrder Management Update Component', () => {
  let comp: ServiceOrderUpdateComponent;
  let fixture: ComponentFixture<ServiceOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceOrderFormService: ServiceOrderFormService;
  let serviceOrderService: ServiceOrderService;
  let serviceOrderTypeService: ServiceOrderTypeService;
  let vehicleService: VehicleService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServiceOrderUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ServiceOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceOrderFormService = TestBed.inject(ServiceOrderFormService);
    serviceOrderService = TestBed.inject(ServiceOrderService);
    serviceOrderTypeService = TestBed.inject(ServiceOrderTypeService);
    vehicleService = TestBed.inject(VehicleService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceOrderType query and add missing value', () => {
      const serviceOrder: IServiceOrder = { id: 32224 };
      const type: IServiceOrderType = { id: 2331 };
      serviceOrder.type = type;

      const serviceOrderTypeCollection: IServiceOrderType[] = [{ id: 2331 }];
      jest.spyOn(serviceOrderTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOrderTypeCollection })));
      const additionalServiceOrderTypes = [type];
      const expectedCollection: IServiceOrderType[] = [...additionalServiceOrderTypes, ...serviceOrderTypeCollection];
      jest.spyOn(serviceOrderTypeService, 'addServiceOrderTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      expect(serviceOrderTypeService.query).toHaveBeenCalled();
      expect(serviceOrderTypeService.addServiceOrderTypeToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOrderTypeCollection,
        ...additionalServiceOrderTypes.map(expect.objectContaining),
      );
      expect(comp.serviceOrderTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vehicle query and add missing value', () => {
      const serviceOrder: IServiceOrder = { id: 32224 };
      const vehicle: IVehicle = { id: 18638 };
      serviceOrder.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: 18638 }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const additionalVehicles = [vehicle];
      const expectedCollection: IVehicle[] = [...additionalVehicles, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleCollection,
        ...additionalVehicles.map(expect.objectContaining),
      );
      expect(comp.vehiclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const serviceOrder: IServiceOrder = { id: 32224 };
      const products: IProduct[] = [{ id: 21536 }];
      serviceOrder.products = products;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const serviceOrder: IServiceOrder = { id: 32224 };
      const type: IServiceOrderType = { id: 2331 };
      serviceOrder.type = type;
      const vehicle: IVehicle = { id: 18638 };
      serviceOrder.vehicle = vehicle;
      const products: IProduct = { id: 21536 };
      serviceOrder.products = [products];

      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      expect(comp.serviceOrderTypesSharedCollection).toContainEqual(type);
      expect(comp.vehiclesSharedCollection).toContainEqual(vehicle);
      expect(comp.productsSharedCollection).toContainEqual(products);
      expect(comp.serviceOrder).toEqual(serviceOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrder>>();
      const serviceOrder = { id: 9582 };
      jest.spyOn(serviceOrderFormService, 'getServiceOrder').mockReturnValue(serviceOrder);
      jest.spyOn(serviceOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOrder }));
      saveSubject.complete();

      // THEN
      expect(serviceOrderFormService.getServiceOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceOrderService.update).toHaveBeenCalledWith(expect.objectContaining(serviceOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrder>>();
      const serviceOrder = { id: 9582 };
      jest.spyOn(serviceOrderFormService, 'getServiceOrder').mockReturnValue({ id: null });
      jest.spyOn(serviceOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOrder }));
      saveSubject.complete();

      // THEN
      expect(serviceOrderFormService.getServiceOrder).toHaveBeenCalled();
      expect(serviceOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrder>>();
      const serviceOrder = { id: 9582 };
      jest.spyOn(serviceOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceOrderType', () => {
      it('Should forward to serviceOrderTypeService', () => {
        const entity = { id: 2331 };
        const entity2 = { id: 8305 };
        jest.spyOn(serviceOrderTypeService, 'compareServiceOrderType');
        comp.compareServiceOrderType(entity, entity2);
        expect(serviceOrderTypeService.compareServiceOrderType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVehicle', () => {
      it('Should forward to vehicleService', () => {
        const entity = { id: 18638 };
        const entity2 = { id: 22559 };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

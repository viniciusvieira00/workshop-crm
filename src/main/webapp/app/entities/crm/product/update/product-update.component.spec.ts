import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IServiceOrder } from 'app/entities/crm/service-order/service-order.model';
import { ServiceOrderService } from 'app/entities/crm/service-order/service/service-order.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormService } from './product-form.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let serviceOrderService: ServiceOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    serviceOrderService = TestBed.inject(ServiceOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceOrder query and add missing value', () => {
      const product: IProduct = { id: 11926 };
      const serviceOrders: IServiceOrder[] = [{ id: 9582 }];
      product.serviceOrders = serviceOrders;

      const serviceOrderCollection: IServiceOrder[] = [{ id: 9582 }];
      jest.spyOn(serviceOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOrderCollection })));
      const additionalServiceOrders = [...serviceOrders];
      const expectedCollection: IServiceOrder[] = [...additionalServiceOrders, ...serviceOrderCollection];
      jest.spyOn(serviceOrderService, 'addServiceOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(serviceOrderService.query).toHaveBeenCalled();
      expect(serviceOrderService.addServiceOrderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOrderCollection,
        ...additionalServiceOrders.map(expect.objectContaining),
      );
      expect(comp.serviceOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 11926 };
      const serviceOrders: IServiceOrder = { id: 9582 };
      product.serviceOrders = [serviceOrders];

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.serviceOrdersSharedCollection).toContainEqual(serviceOrders);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 21536 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 21536 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 21536 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceOrder', () => {
      it('Should forward to serviceOrderService', () => {
        const entity = { id: 9582 };
        const entity2 = { id: 32224 };
        jest.spyOn(serviceOrderService, 'compareServiceOrder');
        comp.compareServiceOrder(entity, entity2);
        expect(serviceOrderService.compareServiceOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

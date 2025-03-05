import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ServiceOrderTypeService } from '../service/service-order-type.service';
import { IServiceOrderType } from '../service-order-type.model';
import { ServiceOrderTypeFormService } from './service-order-type-form.service';

import { ServiceOrderTypeUpdateComponent } from './service-order-type-update.component';

describe('ServiceOrderType Management Update Component', () => {
  let comp: ServiceOrderTypeUpdateComponent;
  let fixture: ComponentFixture<ServiceOrderTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceOrderTypeFormService: ServiceOrderTypeFormService;
  let serviceOrderTypeService: ServiceOrderTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServiceOrderTypeUpdateComponent],
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
      .overrideTemplate(ServiceOrderTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceOrderTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceOrderTypeFormService = TestBed.inject(ServiceOrderTypeFormService);
    serviceOrderTypeService = TestBed.inject(ServiceOrderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serviceOrderType: IServiceOrderType = { id: 8305 };

      activatedRoute.data = of({ serviceOrderType });
      comp.ngOnInit();

      expect(comp.serviceOrderType).toEqual(serviceOrderType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrderType>>();
      const serviceOrderType = { id: 2331 };
      jest.spyOn(serviceOrderTypeFormService, 'getServiceOrderType').mockReturnValue(serviceOrderType);
      jest.spyOn(serviceOrderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOrderType }));
      saveSubject.complete();

      // THEN
      expect(serviceOrderTypeFormService.getServiceOrderType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceOrderTypeService.update).toHaveBeenCalledWith(expect.objectContaining(serviceOrderType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrderType>>();
      const serviceOrderType = { id: 2331 };
      jest.spyOn(serviceOrderTypeFormService, 'getServiceOrderType').mockReturnValue({ id: null });
      jest.spyOn(serviceOrderTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrderType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceOrderType }));
      saveSubject.complete();

      // THEN
      expect(serviceOrderTypeFormService.getServiceOrderType).toHaveBeenCalled();
      expect(serviceOrderTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceOrderType>>();
      const serviceOrderType = { id: 2331 };
      jest.spyOn(serviceOrderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceOrderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceOrderTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

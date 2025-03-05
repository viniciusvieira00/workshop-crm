import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';

import { IServiceOrderType } from 'app/entities/crm/service-order-type/service-order-type.model';
import { ServiceOrderTypeService } from 'app/entities/crm/service-order-type/service/service-order-type.service';
import { IVehicle } from 'app/entities/crm/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/crm/vehicle/service/vehicle.service';
import { IProduct } from 'app/entities/crm/product/product.model';
import { ProductService } from 'app/entities/crm/product/service/product.service';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';
import { ServiceOrderService } from '../service/service-order.service';
import { IServiceOrder } from '../service-order.model';
import { ServiceOrderFormGroup, ServiceOrderFormService } from './service-order-form.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-service-order-update',
  templateUrl: './service-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgSelectModule],
})
export class ServiceOrderUpdateComponent implements OnInit {
  isSaving = false;
  serviceOrder: IServiceOrder | null = null;
  serviceOrderStatusValues = Object.keys(ServiceOrderStatus);

  // Add translated labels for service order status
  serviceOrderStatusLabels: Record<string, string> = {
    CREATED: 'Criado',
    IN_PROGRESS: 'Em Andamento',
    COMPLETED: 'Concluído',
    CANCELED: 'Cancelado',
  };

  vehiclesSharedCollection: IVehicle[] = [];
  productsSharedCollection: IProduct[] = [];
  serviceOrderTypesSharedCollection: IServiceOrderType[] = [];

  editForm: ServiceOrderFormGroup = this.serviceOrderFormService.createServiceOrderFormGroup();

  constructor(
    protected serviceOrderService: ServiceOrderService,
    protected serviceOrderFormService: ServiceOrderFormService,
    protected vehicleService: VehicleService,
    protected productService: ProductService,
    protected serviceOrderTypeService: ServiceOrderTypeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareServiceOrderType = (o1: IServiceOrderType | null, o2: IServiceOrderType | null): boolean =>
    this.serviceOrderTypeService.compareServiceOrderType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceOrder }) => {
      this.serviceOrder = serviceOrder;
      if (serviceOrder) {
        this.updateForm(serviceOrder);
      } else {
        // Set default completion date to 20 days from now for new service orders
        const completionDate = dayjs().add(20, 'day');
        this.editForm.patchValue({
          completionDate: completionDate.format('YYYY-MM-DDTHH:mm'),
          status: ServiceOrderStatus.CREATED,
        });
      }

      this.loadRelationshipsOptions();

      // Check for vehicleId in query params
      this.activatedRoute.queryParams.subscribe(params => {
        const vehicleId = params['vehicleId'];
        if (vehicleId && !this.serviceOrder) {
          this.vehicleService.find(vehicleId).subscribe((vehicleResponse: HttpResponse<IVehicle>) => {
            if (vehicleResponse.body) {
              this.editForm.patchValue({
                vehicle: vehicleResponse.body,
              });
            }
          });
        }
      });
    });

    // Listen for changes to the service order type to update the initial cost
    this.editForm.get('type')?.valueChanges.subscribe(type => {
      if (type) {
        const serviceOrderType = type as IServiceOrderType;
        if (serviceOrderType.price !== undefined) {
          this.editForm.patchValue({
            initialCost: serviceOrderType.price,
          });
          this.calculateTotalCost();
        }
      }
    });

    // Listen for changes to the products to update the total cost
    this.editForm.get('products')?.valueChanges.subscribe(() => {
      this.calculateTotalCost();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceOrder = this.serviceOrderFormService.getServiceOrder(this.editForm);

    // Ensure status is CREATED for new service orders
    if (serviceOrder.id === null) {
      serviceOrder.status = ServiceOrderStatus.CREATED;
    }

    if (serviceOrder.id !== null) {
      this.subscribeToSaveResponse(this.serviceOrderService.update(serviceOrder));
    } else {
      this.subscribeToSaveResponse(this.serviceOrderService.create(serviceOrder));
    }
  }

  // Format currency values for display
  formatCurrency(value: number | null | undefined): string {
    if (value === null || value === undefined) {
      return 'R$ 0,00';
    }
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  // Calculate the total cost based on initial and additional costs
  calculateTotalCost(): void {
    const initialCost = this.editForm.get('initialCost')?.value || 0;
    const additionalCost = this.editForm.get('additionalCost')?.value || 0;

    // Calcular o custo dos produtos selecionados
    let productsCost = 0;
    const selectedProducts = this.editForm.get('products')?.value || [];
    if (selectedProducts.length > 0) {
      productsCost = selectedProducts.reduce((total, product) => {
        return total + (product.sellPrice || 0);
      }, 0);
    }

    const totalCost = initialCost + additionalCost + productsCost;

    this.editForm.patchValue({
      totalCost: totalCost,
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(serviceOrder: IServiceOrder): void {
    this.serviceOrder = serviceOrder;
    this.serviceOrderFormService.resetForm(this.editForm, serviceOrder);

    this.vehiclesSharedCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(
      this.vehiclesSharedCollection,
      serviceOrder.vehicle,
    );
    this.serviceOrderTypesSharedCollection = this.serviceOrderTypeService.addServiceOrderTypeToCollectionIfMissing<IServiceOrderType>(
      this.serviceOrderTypesSharedCollection,
      serviceOrder.type,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(serviceOrder.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vehicleService
      .query()
      .pipe(map((res: HttpResponse<IVehicle[]>) => (res.body ? res.body : [])))
      .pipe(
        map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.serviceOrder?.vehicle)),
      )
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesSharedCollection = vehicles));

    this.serviceOrderTypeService
      .query()
      .pipe(map((res: HttpResponse<IServiceOrderType[]>) => (res.body ? res.body : [])))
      .pipe(
        map((serviceOrderTypes: IServiceOrderType[]) =>
          this.serviceOrderTypeService.addServiceOrderTypeToCollectionIfMissing<IServiceOrderType>(
            serviceOrderTypes,
            this.serviceOrder?.type,
          ),
        ),
      )
      .subscribe((serviceOrderTypes: IServiceOrderType[]) => (this.serviceOrderTypesSharedCollection = serviceOrderTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => (res.body ? res.body : [])))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.serviceOrder?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}

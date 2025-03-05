import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { IServiceOrder } from '../service-order.model';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';
import { IVehicle } from '../../vehicle/vehicle.model';
import { IClient } from '../../client/client.model';
import { ClientService } from '../../client/service/client.service';
import { VehicleService } from '../../vehicle/service/vehicle.service';

import { EntityArrayResponseType, ServiceOrderService } from '../service/service-order.service';
import { ServiceOrderDeleteDialogComponent } from '../delete/service-order-delete-dialog.component';

@Component({
  selector: 'jhi-service-order',
  templateUrl: './service-order.component.html',
  styleUrls: ['./service-order.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    FilterComponent,
    ItemCountComponent,
  ],
})
export class ServiceOrderComponent implements OnInit {
  subscription: Subscription | null = null;
  serviceOrders = signal<IServiceOrder[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Status labels
  statusLabels: Record<string, string> = {
    CREATED: 'Criado',
    IN_PROGRESS: 'Em Andamento',
    COMPLETED: 'Concluído',
    CANCELED: 'Cancelado',
  };

  public readonly router = inject(Router);
  protected readonly serviceOrderService = inject(ServiceOrderService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly vehicleService = inject(VehicleService);
  protected readonly clientService = inject(ClientService);

  vehicleMap: Map<number, IVehicle> = new Map();
  clientMap: Map<number, IClient> = new Map();

  trackId = (item: IServiceOrder): number => this.serviceOrderService.getServiceOrderIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));

    // Carregar todos os veículos
    this.vehicleService.query().subscribe(res => {
      if (res.body) {
        res.body.forEach(vehicle => {
          if (vehicle.id) {
            this.vehicleMap.set(vehicle.id, vehicle);
          }
        });
      }
    });

    // Carregar todos os clientes
    this.clientService.query().subscribe(res => {
      if (res.body) {
        res.body.forEach(client => {
          if (client.id) {
            this.clientMap.set(client.id, client);
          }
        });
      }
    });
  }

  delete(serviceOrder: IServiceOrder): void {
    const modalRef = this.modalService.open(ServiceOrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.serviceOrder = serviceOrder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  // Format currency values for display
  formatCurrency(value: number | null | undefined): string {
    if (value === null || value === undefined) {
      return 'R$ 0,00';
    }
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  // Get translated status label
  getStatusLabel(status: string): string {
    return this.statusLabels[status] || status;
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.serviceOrders.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IServiceOrder[] | null): IServiceOrder[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.serviceOrderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  getClientName(vehicleId: number | null | undefined): string {
    if (!vehicleId) return 'Não informado';

    const vehicle = this.vehicleMap.get(vehicleId);
    if (!vehicle || !vehicle.client || !vehicle.client.id) return 'Não informado';

    const client = this.clientMap.get(vehicle.client.id);
    return client?.name || 'Não informado';
  }
}

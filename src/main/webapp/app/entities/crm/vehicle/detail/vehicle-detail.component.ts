import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVehicle } from '../vehicle.model';
import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { IServiceOrder } from 'app/entities/crm/service-order/service-order.model';
import { ServiceOrderService } from 'app/entities/crm/service-order/service/service-order.service';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-detail',
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgbTooltipModule],
})
export class VehicleDetailComponent implements OnInit {
  vehicle = signal<IVehicle | null>(null);

  activeTab = 'details';
  serviceOrders: IServiceOrder[] = [];
  isLoadingServiceOrders = false;

  protected serviceOrderService = inject(ServiceOrderService);
  protected activatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    // Get vehicle from route resolver
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.vehicle.set(vehicle);

      // Load service orders when component initializes
      if (this.vehicle()) {
        this.loadServiceOrders();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;

    // Load service orders when switching to that tab
    if (tab === 'serviceOrders' && this.vehicle() && this.serviceOrders.length === 0) {
      this.loadServiceOrders();
    }
  }

  loadServiceOrders(): void {
    if (!this.vehicle()) {
      return;
    }

    this.isLoadingServiceOrders = true;

    // Call service to get service orders for this vehicle
    this.serviceOrderService.query({ 'vehicleId.equals': this.vehicle()!.id }).subscribe({
      next: (res: HttpResponse<IServiceOrder[]>) => {
        this.serviceOrders = res.body || [];
        this.isLoadingServiceOrders = false;
      },
      error: () => {
        this.isLoadingServiceOrders = false;
      },
    });
  }

  getVehicleStatusClass(status: VehicleStatus | string | null): string {
    if (!status) {
      return '';
    }

    const statusClasses: Record<string, string> = {
      [VehicleStatus.AVAILABLE]: 'bg-success',
      [VehicleStatus.UNDER_MAINTENANCE]: 'bg-info',
      [VehicleStatus.WAITING_MAINTENANCE]: 'bg-warning',
      [VehicleStatus.OUT_OF_SERVICE]: 'bg-danger',
      [VehicleStatus.DELIVERED_TO_CUSTOMER]: 'bg-primary',
    };

    return statusClasses[status] || '';
  }

  getVehicleStatusLabel(status: VehicleStatus | string | null): string {
    if (!status) {
      return 'Não definido';
    }

    const statusLabels: Record<string, string> = {
      [VehicleStatus.AVAILABLE]: 'Disponível',
      [VehicleStatus.UNDER_MAINTENANCE]: 'Em Manutenção',
      [VehicleStatus.WAITING_MAINTENANCE]: 'Aguardando Manutenção',
      [VehicleStatus.OUT_OF_SERVICE]: 'Fora de Serviço',
      [VehicleStatus.DELIVERED_TO_CUSTOMER]: 'Entregue ao Cliente',
    };

    return statusLabels[status] || status.toString();
  }

  getServiceOrderStatusClass(status: string | null): string {
    if (!status) {
      return '';
    }

    const statusClasses: Record<string, string> = {
      CREATED: 'bg-primary',
      IN_PROGRESS: 'bg-info',
      COMPLETED: 'bg-success',
      CANCELED: 'bg-danger',
      WAITING_PARTS: 'bg-warning',
    };

    return statusClasses[status] || '';
  }

  getServiceOrderStatusLabel(status: string | null): string {
    if (!status) {
      return 'Não definido';
    }

    const statusLabels: Record<string, string> = {
      CREATED: 'Aberta',
      IN_PROGRESS: 'Em Andamento',
      COMPLETED: 'Concluída',
      CANCELED: 'Cancelada',
      WAITING_PARTS: 'Aguardando Peças',
    };

    return statusLabels[status] || status;
  }
}

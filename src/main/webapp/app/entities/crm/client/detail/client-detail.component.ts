import { Component, OnInit, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IClient } from '../client.model';
import { IVehicle } from '../../vehicle/vehicle.model';
import { VehicleService } from '../../vehicle/service/vehicle.service';
import { IServiceOrder } from '../../service-order/service-order.model';
import { ServiceOrderService } from '../../service-order/service/service-order.service';
import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-client-detail',
  templateUrl: './client-detail.component.html',
  styleUrls: ['./client-detail.component.scss'],
  standalone: true,
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, NgbTooltipModule],
})
export class ClientDetailComponent implements OnInit {
  client = input<IClient | null>(null);

  // Tab management
  activeTab = 'details';

  // Vehicles tab
  vehicles: IVehicle[] = [];
  isLoadingVehicles = false;

  // Service Orders tab
  serviceOrders: IServiceOrder[] = [];
  isLoadingServiceOrders = false;

  protected vehicleService = inject(VehicleService);
  protected serviceOrderService = inject(ServiceOrderService);

  ngOnInit(): void {
    if (this.client()) {
      this.loadVehicles();
    }
  }

  previousState(): void {
    window.history.back();
  }

  // Tab management
  setActiveTab(tab: string): void {
    this.activeTab = tab;

    // Load data for the selected tab if needed
    if (tab === 'vehicles' && this.vehicles.length === 0) {
      this.loadVehicles();
    } else if (tab === 'workOrders' && this.serviceOrders.length === 0) {
      this.loadServiceOrders();
    }
  }

  // Vehicles tab methods
  loadVehicles(): void {
    if (!this.client()) {
      return;
    }

    this.isLoadingVehicles = true;

    // Create filter to get vehicles for this client
    const clientId = this.client()!.id;
    const req = {
      'clientId.equals': clientId,
    };

    this.vehicleService.query(req).subscribe({
      next: (res: HttpResponse<IVehicle[]>) => {
        this.vehicles = res.body || [];
        this.isLoadingVehicles = false;
      },
      error: () => {
        this.isLoadingVehicles = false;
      },
    });
  }

  getVehicleStatusLabel(status: string | null | undefined): string {
    if (!status) {
      return 'Desconhecido';
    }

    const statusMap: Record<string, string> = {
      AVAILABLE: 'Disponível',
      UNDER_MAINTENANCE: 'Em Manutenção',
      WAITING_MAINTENANCE: 'Aguardando Manutenção',
      OUT_OF_SERVICE: 'Fora de Serviço',
      DELIVERED_TO_CUSTOMER: 'Entregue ao Cliente',
    };

    return statusMap[status] || status;
  }

  getVehicleStatusClass(status: string | null | undefined): string {
    if (!status) {
      return 'bg-secondary';
    }

    const statusClassMap: Record<string, string> = {
      AVAILABLE: 'bg-success',
      UNDER_MAINTENANCE: 'bg-warning',
      WAITING_MAINTENANCE: 'bg-info',
      OUT_OF_SERVICE: 'bg-danger',
      DELIVERED_TO_CUSTOMER: 'bg-primary',
    };

    return statusClassMap[status] || 'bg-secondary';
  }

  // Service Orders tab methods
  loadServiceOrders(): void {
    if (!this.client()) {
      return;
    }

    this.isLoadingServiceOrders = true;

    // We need to get all vehicles for this client first
    const clientId = this.client()!.id;
    const vehicleReq = {
      'clientId.equals': clientId,
    };

    this.vehicleService.query(vehicleReq).subscribe({
      next: (vehicleRes: HttpResponse<IVehicle[]>) => {
        const vehicles = vehicleRes.body || [];

        if (vehicles.length === 0) {
          this.serviceOrders = [];
          this.isLoadingServiceOrders = false;
          return;
        }

        // Get all vehicle IDs
        const vehicleIds = vehicles.map(v => v.id);

        // Create filter to get service orders for these vehicles
        const serviceOrderReq = {
          'vehicleId.in': vehicleIds.join(','),
        };

        this.serviceOrderService.query(serviceOrderReq).subscribe({
          next: (serviceOrderRes: HttpResponse<IServiceOrder[]>) => {
            this.serviceOrders = serviceOrderRes.body || [];
            this.isLoadingServiceOrders = false;
          },
          error: () => {
            this.isLoadingServiceOrders = false;
          },
        });
      },
      error: () => {
        this.isLoadingServiceOrders = false;
      },
    });
  }

  getServiceOrderStatusLabel(status: string | null | undefined): string {
    if (!status) {
      return 'Desconhecido';
    }

    const statusMap: Record<string, string> = {
      CREATED: 'Criada',
      IN_PROGRESS: 'Em Andamento',
      COMPLETED: 'Concluída',
      CANCELED: 'Cancelada',
    };

    return statusMap[status] || status;
  }

  getServiceOrderStatusClass(status: string | null | undefined): string {
    if (!status) {
      return 'bg-secondary';
    }

    const statusClassMap: Record<string, string> = {
      CREATED: 'bg-info',
      IN_PROGRESS: 'bg-warning',
      COMPLETED: 'bg-success',
      CANCELED: 'bg-danger',
    };

    return statusClassMap[status] || 'bg-secondary';
  }

  // Utility methods
  formatDocument(doc: string | null | undefined): string {
    if (!doc) {
      return 'Não informado';
    }

    // Format CPF: 123.456.789-10
    if (doc.length === 11) {
      return `${doc.substring(0, 3)}.${doc.substring(3, 6)}.${doc.substring(6, 9)}-${doc.substring(9, 11)}`;
    }

    // Format CNPJ: 12.345.678/0001-90
    if (doc.length === 14) {
      return `${doc.substring(0, 2)}.${doc.substring(2, 5)}.${doc.substring(5, 8)}/${doc.substring(8, 12)}-${doc.substring(12, 14)}`;
    }

    return doc;
  }

  formatPhoneNumber(phone: string | null | undefined): string {
    if (!phone) {
      return 'Não informado';
    }

    // Format 10-digit phone: (12) 3456-7890
    if (phone.length === 10) {
      return `(${phone.substring(0, 2)}) ${phone.substring(2, 6)}-${phone.substring(6, 10)}`;
    }

    // Format 11-digit phone: (12) 9 3456-7890
    if (phone.length === 11) {
      return `(${phone.substring(0, 2)}) ${phone.substring(2, 3)} ${phone.substring(3, 7)}-${phone.substring(7, 11)}`;
    }

    return phone;
  }

  getPhoneLink(phone: string | null | undefined): string {
    if (!phone) {
      return '#';
    }

    return `tel:+55${phone}`;
  }

  formatCep(cep: string | null | undefined): string {
    if (!cep) {
      return 'Não informado';
    }

    // Format CEP: 12345-678
    if (cep.length === 8) {
      return `${cep.substring(0, 5)}-${cep.substring(5, 8)}`;
    }

    return cep;
  }
}

import { Component, input, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IServiceOrder } from '../service-order.model';
import { ServiceOrderStatus } from 'app/entities/enumerations/service-order-status.model';
import { IVehicle } from '../../vehicle/vehicle.model';
import { IClient } from '../../client/client.model';
import { ClientService } from '../../client/service/client.service';
import { VehicleService } from '../../vehicle/service/vehicle.service';
import { IProduct } from '../../product/product.model';

@Component({
  selector: 'jhi-service-order-detail',
  templateUrl: './service-order-detail.component.html',
  styleUrls: ['./service-order-detail.component.scss'],
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ServiceOrderDetailComponent {
  serviceOrder = input<IServiceOrder | null>(null);

  protected readonly vehicleService = inject(VehicleService);
  protected readonly clientService = inject(ClientService);

  vehicleMap: Map<number, IVehicle> = new Map();
  clientMap: Map<number, IClient> = new Map();

  statusLabels: Record<string, string> = {
    CREATED: 'Criada',
    IN_PROGRESS: 'Em Andamento',
    COMPLETED: 'Concluída',
    CANCELED: 'Cancelada',
  };

  constructor() {
    this.loadVehiclesAndClients();
  }

  previousState(): void {
    window.history.back();
  }

  loadVehiclesAndClients(): void {
    this.vehicleService.query().subscribe(response => {
      if (response.body) {
        for (const vehicle of response.body) {
          if (vehicle.id) {
            this.vehicleMap.set(vehicle.id, vehicle);
          }
        }
      }

      // After loading vehicles, load clients
      this.clientService.query().subscribe(clientResponse => {
        if (clientResponse.body) {
          for (const client of clientResponse.body) {
            if (client.id) {
              this.clientMap.set(client.id, client);
            }
          }
        }
      });
    });
  }

  getClientName(vehicleId: number | null | undefined): string {
    if (!vehicleId) {
      return 'Não informado';
    }

    const vehicle = this.vehicleMap.get(vehicleId);
    if (vehicle && vehicle.client && vehicle.client.id) {
      const client = this.clientMap.get(vehicle.client.id);
      return client ? client.name || 'Não informado' : 'Não informado';
    }

    return 'Não informado';
  }

  getStatusLabel(status: string | null | undefined): string {
    if (!status) return 'Não informado';
    return this.statusLabels[status] || status;
  }

  formatCurrency(value: number | null | undefined): string {
    if (value === null || value === undefined) {
      return 'R$ 0,00';
    }
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  calculateProductsTotal(products: Pick<IProduct, 'id' | 'name' | 'brand' | 'sellPrice'>[] | null): string {
    if (!products || products.length === 0) {
      return this.formatCurrency(0);
    }

    const total = products.reduce((sum, product) => {
      return sum + (product.sellPrice || 0);
    }, 0);

    return this.formatCurrency(total);
  }
}

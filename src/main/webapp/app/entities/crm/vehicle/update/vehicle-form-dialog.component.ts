import { Component, Input, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehicle } from '../vehicle.model';
import { VehicleService } from '../service/vehicle.service';
import { IClient } from '../../client/client.model';
import { ClientService } from '../../client/service/client.service';
import { VehicleFormService, VehicleFormGroup } from './vehicle-form.service';
import { VehicleStatus } from 'app/entities/enumerations/vehicle-status.model';
import { NewVehicle } from '../vehicle.model';

@Component({
  standalone: true,
  templateUrl: './vehicle-form-dialog.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleFormDialogComponent implements OnInit {
  @Input() vehicle: IVehicle = { id: 0 };
  @Input() isNew = true;

  isSaving = false;
  vehicleStatusValues = Object.keys(VehicleStatus);

  clientsSharedCollection: IClient[] = [];
  editForm!: VehicleFormGroup;

  protected vehicleService = inject(VehicleService);
  protected vehicleFormService = inject(VehicleFormService);
  protected clientService = inject(ClientService);
  protected activeModal = inject(NgbActiveModal);
  protected activatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.initializeForm();
    this.loadRelationshipsOptions();
  }

  initializeForm(): void {
    this.editForm = this.vehicleFormService.createVehicleFormGroup();
    if (!this.isNew && this.vehicle.id) {
      this.updateForm(this.vehicle);
    } else if (this.vehicle.client?.id) {
      // Se for um novo veículo com cliente já definido
      this.editForm.patchValue({
        client: this.vehicle.client,
        status: VehicleStatus.AVAILABLE,
      });
    }
  }

  updateForm(vehicle: IVehicle): void {
    this.editForm.patchValue({
      id: vehicle.id,
      licensePlate: vehicle.licensePlate,
      model: vehicle.model,
      brand: vehicle.brand,
      fabricationDate: vehicle.fabricationDate,
      modelYear: vehicle.modelYear,
      color: vehicle.color,
      renavam: vehicle.renavam,
      fuelType: vehicle.fuelType,
      chassiNumber: vehicle.chassiNumber,
      currentMileage: vehicle.currentMileage,
      lastMaintenanceDate: vehicle.lastMaintenanceDate,
      lastMaintenanceMileage: vehicle.lastMaintenanceMileage,
      nextMaintenanceDate: vehicle.nextMaintenanceDate,
      nextMaintenanceMileage: vehicle.nextMaintenanceMileage,
      description: vehicle.description,
      status: vehicle.status,
      client: vehicle.client,
    });

    // Garantir que o cliente esteja selecionado no formulário
    if (vehicle.client && vehicle.client.id) {
      this.editForm.get('client')?.setValue(vehicle.client);
    }
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.createFromForm();

    if (this.isNew) {
      const newVehicle: NewVehicle = { ...vehicle, id: null };
      this.subscribeToSaveResponse(this.vehicleService.create(newVehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    }
  }

  createFromForm(): IVehicle {
    return {
      ...this.editForm.getRawValue(),
      id: this.editForm.get('id')?.value || 0,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.activeModal.close('saved');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected loadRelationshipsOptions(): void {
    if (this.vehicle.client?.id) {
      this.clientService
        .find(this.vehicle.client.id)
        .pipe(map((res: HttpResponse<IClient>) => res.body ?? { id: 0 }))
        .subscribe((client: IClient) => {
          if (client.id) {
            this.clientsSharedCollection = [client];

            this.editForm.patchValue({
              client: client,
            });
          }
        });
    } else {
      this.clientService
        .query()
        .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
        .subscribe((clients: IClient[]) => {
          this.clientsSharedCollection = clients;
        });
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  getStatusLabel(status: string | undefined): string {
    const statusMap: Record<string, string> = {
      AVAILABLE: 'Disponível',
      UNDER_MAINTENANCE: 'Em Manutenção',
      WAITING_MAINTENANCE: 'Aguardando Manutenção',
      OUT_OF_SERVICE: 'Fora de Serviço',
      DELIVERED_TO_CUSTOMER: 'Entregue ao Cliente',
    };
    return status ? statusMap[status] || status : '';
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}

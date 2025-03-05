import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceOrderType } from '../service-order-type.model';
import { ServiceOrderTypeService } from '../service/service-order-type.service';
import { ServiceOrderTypeFormGroup, ServiceOrderTypeFormService } from './service-order-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-order-type-update',
  templateUrl: './service-order-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  styleUrls: ['./service-order-type-update.component.scss'],
})
export class ServiceOrderTypeUpdateComponent implements OnInit {
  isSaving = false;
  serviceOrderType: IServiceOrderType | null = null;

  protected serviceOrderTypeService = inject(ServiceOrderTypeService);
  protected serviceOrderTypeFormService = inject(ServiceOrderTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceOrderTypeFormGroup = this.serviceOrderTypeFormService.createServiceOrderTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceOrderType }) => {
      this.serviceOrderType = serviceOrderType;
      if (serviceOrderType) {
        this.updateForm(serviceOrderType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceOrderType = this.serviceOrderTypeFormService.getServiceOrderType(this.editForm);
    if (serviceOrderType.id !== null) {
      this.subscribeToSaveResponse(this.serviceOrderTypeService.update(serviceOrderType));
    } else {
      this.subscribeToSaveResponse(this.serviceOrderTypeService.create(serviceOrderType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceOrderType>>): void {
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

  protected updateForm(serviceOrderType: IServiceOrderType): void {
    this.serviceOrderType = serviceOrderType;
    this.serviceOrderTypeFormService.resetForm(this.editForm, serviceOrderType);
  }
}

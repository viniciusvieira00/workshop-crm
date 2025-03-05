import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceOrderType } from '../service-order-type.model';
import { ServiceOrderTypeService } from '../service/service-order-type.service';

@Component({
  templateUrl: './service-order-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceOrderTypeDeleteDialogComponent {
  serviceOrderType?: IServiceOrderType;

  protected serviceOrderTypeService = inject(ServiceOrderTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceOrderTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

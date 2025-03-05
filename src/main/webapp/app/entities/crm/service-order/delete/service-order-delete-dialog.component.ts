import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceOrder } from '../service-order.model';
import { ServiceOrderService } from '../service/service-order.service';

@Component({
  templateUrl: './service-order-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceOrderDeleteDialogComponent {
  serviceOrder?: IServiceOrder;

  protected serviceOrderService = inject(ServiceOrderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

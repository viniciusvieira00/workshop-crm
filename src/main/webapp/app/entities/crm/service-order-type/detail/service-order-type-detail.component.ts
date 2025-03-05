import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IServiceOrderType } from '../service-order-type.model';
import * as dayjs from 'dayjs';
import { FieldInfoComponent } from 'app/shared/field-info/field-info.component';

@Component({
  standalone: true,
  selector: 'jhi-service-order-type-detail',
  templateUrl: './service-order-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, FieldInfoComponent],
  styleUrls: ['./service-order-type-detail.component.scss'],
})
export class ServiceOrderTypeDetailComponent {
  @Input() serviceOrderType: IServiceOrderType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  formatDate(date: dayjs.Dayjs | null | undefined): string {
    if (!date) {
      return '';
    }
    return date.format('DD/MM/YYYY HH:mm');
  }

  previousState(): void {
    window.history.back();
  }
}

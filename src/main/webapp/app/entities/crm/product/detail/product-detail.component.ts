import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import dayjs from 'dayjs';

import SharedModule from 'app/shared/shared.module';
import { IProduct } from '../product.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
  standalone: true,
  imports: [SharedModule, RouterModule, DatePipe],
})
export class ProductDetailComponent implements OnInit {
  product: IProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
    });
  }

  previousState(): void {
    window.history.back();
  }

  formatCurrency(value: number | null | undefined): string {
    if (value === null || value === undefined) {
      return 'N/A';
    }
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  formatDate(date: dayjs.Dayjs | null | undefined): string {
    if (!date) {
      return 'N/A';
    }
    return date.format('DD/MM/YYYY HH:mm');
  }
}

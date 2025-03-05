import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-field-info',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="field-info">
      <div class="field-label">{{ label }}</div>
      <div class="field-value">{{ value || 'Não informado' }}</div>
    </div>
  `,
  styles: [
    `
      .field-info {
        margin-bottom: 10px;
      }
      .field-label {
        font-weight: bold;
        color: #495057;
        margin-bottom: 5px;
      }
      .field-value {
        color: #212529;
      }
    `,
  ],
})
export class FieldInfoComponent {
  @Input() label: string = '';
  @Input() value: any = null;
}

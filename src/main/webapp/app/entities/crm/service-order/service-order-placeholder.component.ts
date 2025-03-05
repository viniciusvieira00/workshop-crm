import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-service-order-placeholder',
  standalone: true,
  imports: [SharedModule],
  template: `
    <div class="row">
      <div class="col-md-12 text-center mt-5">
        <h1>Ordens de Serviço</h1>
        <p class="lead mt-3">Esta funcionalidade será implementada em breve.</p>
        <div class="alert alert-info mt-4">
          <fa-icon icon="info-circle" class="me-2"></fa-icon>
          <span>O módulo de Ordens de Serviço permitirá gerenciar todos os serviços realizados nos veículos dos clientes.</span>
        </div>
      </div>
    </div>
  `,
})
export default class ServiceOrderPlaceholderComponent {}

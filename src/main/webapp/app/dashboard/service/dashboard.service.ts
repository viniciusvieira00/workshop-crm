import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDashboard } from '../model/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboard');

  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
  ) {}

  /**
   * Obtém os dados do dashboard.
   * @returns Observable com os dados do dashboard.
   */
  getDashboard(): Observable<HttpResponse<IDashboard>> {
    return this.http.get<IDashboard>(this.resourceUrl, { observe: 'response' });
  }
}

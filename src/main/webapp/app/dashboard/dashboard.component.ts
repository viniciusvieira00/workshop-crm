import { Component, OnInit, NO_ERRORS_SCHEMA, AfterViewInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartType } from 'chart.js';
import { finalize } from 'rxjs/operators';

import './chart.config';
import { DashboardService } from './service/dashboard.service';
import { IDashboard, IServiceOrderTypeStats, IClientStats } from './model/dashboard.model';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';

// Definindo tipos para as opções dos gráficos
type IndexAxis = 'x' | 'y';

@Component({
  standalone: true,
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule, BaseChartDirective, FontAwesomeModule, NgbModule, AlertErrorComponent],
  schemas: [NO_ERRORS_SCHEMA],
})
export class DashboardComponent implements OnInit, AfterViewInit {
  dashboard: IDashboard | null = null;
  isLoading = true;
  chartsReady = false;

  // Configurações para os gráficos
  pieChartOptions: any = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: function (context: any) {
            const label = context.label || '';
            const value = context.raw || 0;
            const total = context.chart.data.datasets[0].data.reduce((a: number, b: number) => a + b, 0);
            const percentage = Math.round((value / total) * 100);
            return `${label}: ${value} (${percentage}%)`;
          },
        },
      },
    },
    animation: {
      duration: 1000,
    },
  };

  barChartOptions: any = {
    responsive: true,
    scales: {
      x: {},
      y: {
        min: 0,
      },
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
    },
    animation: {
      duration: 1000,
    },
  };

  lineChartOptions: any = {
    responsive: true,
    scales: {
      x: {},
      y: {
        min: 0,
      },
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
    },
    animation: {
      duration: 1000,
    },
  };

  horizontalBarChartOptions: any = {
    responsive: true,
    indexAxis: 'y' as IndexAxis,
    scales: {
      x: {
        min: 0,
      },
      y: {},
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
    },
    animation: {
      duration: 1000,
    },
  };

  // Tipos de gráficos
  pieChartType: ChartType = 'pie';
  barChartType: ChartType = 'bar';
  lineChartType: ChartType = 'line';
  horizontalBarChartType: ChartType = 'bar';

  // Dados para os gráficos
  serviceOrdersByStatusData: any = {
    labels: [],
    datasets: [{ data: [] }],
  };

  topServiceTypesData: any = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Quantidade',
        backgroundColor: 'rgba(90, 164, 84, 0.7)',
      },
    ],
  };

  revenueByMonthData: any = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Faturamento Mensal',
        fill: false,
        tension: 0.1,
        borderColor: 'rgba(62, 138, 204, 1)',
        backgroundColor: 'rgba(62, 138, 204, 0.2)',
      },
    ],
  };

  topClientsData: any = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Quantidade de Ordens',
        backgroundColor: 'rgba(161, 10, 40, 0.7)',
      },
    ],
  };

  // Mapeamento de status para tradução
  statusTranslation: { [key: string]: string } = {
    CREATED: 'Criada',
    IN_PROGRESS: 'Em Andamento',
    COMPLETED: 'Concluída',
    CANCELED: 'Cancelada',
  };

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    // Garantir que o loading seja exibido por pelo menos 1 segundo para evitar flash
    setTimeout(() => {
      this.chartsReady = true;
      if (this.dashboard) {
        this.isLoading = false;
      }
    }, 1000);
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.dashboardService
      .getDashboard()
      .pipe(
        finalize(() => {
          if (this.chartsReady) {
            this.isLoading = false;
          }
        }),
      )
      .subscribe({
        next: (res: HttpResponse<IDashboard>) => {
          this.dashboard = res.body;
          this.prepareChartData();
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  prepareChartData(): void {
    if (this.dashboard) {
      // Preparar dados para o gráfico de ordens de serviço por status
      const statusEntries = Object.entries(this.dashboard.serviceOrdersByStatus || {});
      this.serviceOrdersByStatusData = {
        labels: statusEntries.map(([name]) => this.statusTranslation[name] || name),
        datasets: [
          {
            data: statusEntries.map(([, value]) => value || 0),
            backgroundColor: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA'],
          },
        ],
      };

      // Preparar dados para o gráfico de tipos de serviço mais realizados
      const serviceTypes = this.dashboard.topServiceTypes || [];
      this.topServiceTypesData = {
        labels: serviceTypes.map(type => type.name || ''),
        datasets: [
          {
            data: serviceTypes.map(type => type.count || 0),
            label: 'Quantidade',
            backgroundColor: 'rgba(90, 164, 84, 0.7)',
          },
        ],
      };

      // Preparar dados para o gráfico de faturamento por mês
      const monthEntries = Object.entries(this.dashboard.revenueByMonth || {});

      // Formatar os nomes dos meses
      const formattedMonths = monthEntries.map(([name]) => {
        const [year, month] = name.split('-');
        const date = new Date(parseInt(year), parseInt(month) - 1);
        return date.toLocaleDateString('pt-BR', { month: 'short', year: 'numeric' });
      });

      this.revenueByMonthData = {
        labels: formattedMonths,
        datasets: [
          {
            data: monthEntries.map(([, value]) => value || 0),
            label: 'Faturamento Mensal',
            fill: false,
            tension: 0.1,
            borderColor: 'rgba(62, 138, 204, 1)',
            backgroundColor: 'rgba(62, 138, 204, 0.2)',
          },
        ],
      };

      // Preparar dados para o gráfico de clientes mais frequentes
      const clients = this.dashboard.topClients || [];
      this.topClientsData = {
        labels: clients.map(client => client.name || ''),
        datasets: [
          {
            data: clients.map(client => client.serviceOrderCount || 0),
            label: 'Quantidade de Ordens',
            backgroundColor: 'rgba(161, 10, 40, 0.7)',
          },
        ],
      };
    }
  }
}

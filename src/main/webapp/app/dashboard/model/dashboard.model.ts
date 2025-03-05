export interface IDashboard {
  totalRevenue?: number;
  totalServiceOrders?: number;
  serviceOrdersByStatus?: Record<string, number>;
  revenueByMonth?: Record<string, number>;
  topServiceTypes?: IServiceOrderTypeStats[];
  topClients?: IClientStats[];
}

export interface IServiceOrderTypeStats {
  id?: number;
  name?: string;
  count?: number;
  revenue?: number;
}

export interface IClientStats {
  id?: number;
  name?: string;
  serviceOrderCount?: number;
  totalSpent?: number;
}

export class Dashboard implements IDashboard {
  constructor(
    public totalRevenue?: number,
    public totalServiceOrders?: number,
    public serviceOrdersByStatus?: Record<string, number>,
    public revenueByMonth?: Record<string, number>,
    public topServiceTypes?: IServiceOrderTypeStats[],
    public topClients?: IClientStats[],
  ) {}
}

package com.vabv.crm.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO para dados do dashboard analítico.
 */
public class DashboardDTO implements Serializable {

    private BigDecimal totalRevenue;
    private Long totalServiceOrders;
    private Map<String, Long> serviceOrdersByStatus;
    private Map<String, BigDecimal> revenueByMonth;
    private List<ServiceOrderTypeStatsDTO> topServiceTypes;
    private List<ClientStatsDTO> topClients;

    // Classe interna para estatísticas de tipos de serviço
    public static class ServiceOrderTypeStatsDTO implements Serializable {

        private Long id;
        private String name;
        private Long count;
        private BigDecimal revenue;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    // Classe interna para estatísticas de clientes
    public static class ClientStatsDTO implements Serializable {

        private Long id;
        private String name;
        private Long serviceOrderCount;
        private BigDecimal totalSpent;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getServiceOrderCount() {
            return serviceOrderCount;
        }

        public void setServiceOrderCount(Long serviceOrderCount) {
            this.serviceOrderCount = serviceOrderCount;
        }

        public BigDecimal getTotalSpent() {
            return totalSpent;
        }

        public void setTotalSpent(BigDecimal totalSpent) {
            this.totalSpent = totalSpent;
        }
    }

    // Getters e Setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalServiceOrders() {
        return totalServiceOrders;
    }

    public void setTotalServiceOrders(Long totalServiceOrders) {
        this.totalServiceOrders = totalServiceOrders;
    }

    public Map<String, Long> getServiceOrdersByStatus() {
        return serviceOrdersByStatus;
    }

    public void setServiceOrdersByStatus(Map<String, Long> serviceOrdersByStatus) {
        this.serviceOrdersByStatus = serviceOrdersByStatus;
    }

    public Map<String, BigDecimal> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(Map<String, BigDecimal> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public List<ServiceOrderTypeStatsDTO> getTopServiceTypes() {
        return topServiceTypes;
    }

    public void setTopServiceTypes(List<ServiceOrderTypeStatsDTO> topServiceTypes) {
        this.topServiceTypes = topServiceTypes;
    }

    public List<ClientStatsDTO> getTopClients() {
        return topClients;
    }

    public void setTopClients(List<ClientStatsDTO> topClients) {
        this.topClients = topClients;
    }
}

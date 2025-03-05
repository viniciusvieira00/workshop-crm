package com.vabv.crm.service;

import com.vabv.crm.domain.enumeration.ServiceOrderStatus;
import com.vabv.crm.repository.ClientRepository;
import com.vabv.crm.repository.ServiceOrderRepository;
import com.vabv.crm.repository.ServiceOrderTypeRepository;
import com.vabv.crm.service.dto.DashboardDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço para fornecer dados analíticos para o dashboard.
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderTypeRepository serviceOrderTypeRepository;
    private final ClientRepository clientRepository;

    public DashboardService(
        ServiceOrderRepository serviceOrderRepository,
        ServiceOrderTypeRepository serviceOrderTypeRepository,
        ClientRepository clientRepository
    ) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderTypeRepository = serviceOrderTypeRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Obtém os dados do dashboard.
     *
     * @return os dados do dashboard.
     */
    public DashboardDTO getDashboardData() {
        log.debug("Requisição para obter dados do dashboard");

        DashboardDTO dashboardDTO = new DashboardDTO();

        // Total de ordens de serviço
        dashboardDTO.setTotalServiceOrders(serviceOrderRepository.count());

        // Faturamento total (ordens de serviço concluídas)
        BigDecimal totalRevenue = serviceOrderRepository
            .findAll()
            .stream()
            .filter(order -> ServiceOrderStatus.COMPLETED.equals(order.getStatus()))
            .map(order -> order.getTotalCost() != null ? order.getTotalCost() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboardDTO.setTotalRevenue(totalRevenue);

        // Ordens de serviço por status
        Map<String, Long> serviceOrdersByStatus = Arrays.stream(ServiceOrderStatus.values()).collect(
            Collectors.toMap(Enum::name, status ->
                serviceOrderRepository.findAll().stream().filter(order -> status.equals(order.getStatus())).count()
            )
        );
        dashboardDTO.setServiceOrdersByStatus(serviceOrdersByStatus);

        // Faturamento por mês (últimos 12 meses)
        Map<String, BigDecimal> revenueByMonth = getRevenueByMonth();
        dashboardDTO.setRevenueByMonth(revenueByMonth);

        // Top tipos de serviço
        List<DashboardDTO.ServiceOrderTypeStatsDTO> topServiceTypes = getTopServiceTypes();
        dashboardDTO.setTopServiceTypes(topServiceTypes);

        // Top clientes
        List<DashboardDTO.ClientStatsDTO> topClients = getTopClients();
        dashboardDTO.setTopClients(topClients);

        return dashboardDTO;
    }

    /**
     * Obtém o faturamento por mês nos últimos 12 meses.
     *
     * @return mapa com o faturamento por mês.
     */
    private Map<String, BigDecimal> getRevenueByMonth() {
        Map<String, BigDecimal> revenueByMonth = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // Inicializa os últimos 12 meses com zero
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            revenueByMonth.put(month.format(formatter), BigDecimal.ZERO);
        }

        // Calcula o faturamento para cada mês
        serviceOrderRepository
            .findAll()
            .stream()
            .filter(order -> ServiceOrderStatus.COMPLETED.equals(order.getStatus()))
            .filter(order -> order.getCompletionDate() != null)
            .filter(order -> order.getTotalCost() != null)
            .forEach(order -> {
                LocalDate completionDate = LocalDate.ofInstant(order.getCompletionDate(), ZoneId.systemDefault());
                String monthKey = completionDate.format(formatter);

                // Só considera os últimos 12 meses
                if (revenueByMonth.containsKey(monthKey)) {
                    BigDecimal currentRevenue = revenueByMonth.get(monthKey);
                    revenueByMonth.put(monthKey, currentRevenue.add(order.getTotalCost()));
                }
            });

        return revenueByMonth;
    }

    /**
     * Obtém os 5 tipos de serviço mais realizados.
     *
     * @return lista com os 5 tipos de serviço mais realizados.
     */
    private List<DashboardDTO.ServiceOrderTypeStatsDTO> getTopServiceTypes() {
        Map<Long, DashboardDTO.ServiceOrderTypeStatsDTO> typeStats = new HashMap<>();

        // Inicializa estatísticas para todos os tipos de serviço
        serviceOrderTypeRepository
            .findAll()
            .forEach(type -> {
                DashboardDTO.ServiceOrderTypeStatsDTO statsDTO = new DashboardDTO.ServiceOrderTypeStatsDTO();
                statsDTO.setId(type.getId());
                statsDTO.setName(type.getName());
                statsDTO.setCount(0L);
                statsDTO.setRevenue(BigDecimal.ZERO);
                typeStats.put(type.getId(), statsDTO);
            });

        // Calcula estatísticas para cada ordem de serviço
        serviceOrderRepository
            .findAll()
            .forEach(order -> {
                if (order.getType() != null) {
                    Long typeId = order.getType().getId();
                    DashboardDTO.ServiceOrderTypeStatsDTO statsDTO = typeStats.get(typeId);

                    if (statsDTO != null) {
                        // Incrementa contador
                        statsDTO.setCount(statsDTO.getCount() + 1);

                        // Adiciona receita se a ordem estiver concluída
                        if (ServiceOrderStatus.COMPLETED.equals(order.getStatus()) && order.getTotalCost() != null) {
                            statsDTO.setRevenue(statsDTO.getRevenue().add(order.getTotalCost()));
                        }
                    }
                }
            });

        // Retorna os 5 tipos mais frequentes
        return typeStats
            .values()
            .stream()
            .sorted(Comparator.comparing(DashboardDTO.ServiceOrderTypeStatsDTO::getCount).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    /**
     * Obtém os 5 clientes com mais ordens de serviço.
     *
     * @return lista com os 5 clientes com mais ordens de serviço.
     */
    private List<DashboardDTO.ClientStatsDTO> getTopClients() {
        Map<Long, DashboardDTO.ClientStatsDTO> clientStats = new HashMap<>();

        // Calcula estatísticas para cada ordem de serviço
        serviceOrderRepository
            .findAll()
            .forEach(order -> {
                if (order.getVehicle() != null && order.getVehicle().getClient() != null) {
                    Long clientId = order.getVehicle().getClient().getId();
                    String clientName = order.getVehicle().getClient().getName();

                    // Inicializa estatísticas para o cliente se necessário
                    clientStats.putIfAbsent(clientId, new DashboardDTO.ClientStatsDTO());
                    DashboardDTO.ClientStatsDTO statsDTO = clientStats.get(clientId);
                    statsDTO.setId(clientId);
                    statsDTO.setName(clientName);

                    // Inicializa contadores se necessário
                    if (statsDTO.getServiceOrderCount() == null) {
                        statsDTO.setServiceOrderCount(0L);
                    }
                    if (statsDTO.getTotalSpent() == null) {
                        statsDTO.setTotalSpent(BigDecimal.ZERO);
                    }

                    // Incrementa contador
                    statsDTO.setServiceOrderCount(statsDTO.getServiceOrderCount() + 1);

                    // Adiciona gasto se a ordem estiver concluída
                    if (ServiceOrderStatus.COMPLETED.equals(order.getStatus()) && order.getTotalCost() != null) {
                        statsDTO.setTotalSpent(statsDTO.getTotalSpent().add(order.getTotalCost()));
                    }
                }
            });

        // Retorna os 5 clientes com mais ordens de serviço
        return clientStats
            .values()
            .stream()
            .sorted(Comparator.comparing(DashboardDTO.ClientStatsDTO::getServiceOrderCount).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
}

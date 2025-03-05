package com.vabv.crm.web.rest;

import com.vabv.crm.service.DashboardService;
import com.vabv.crm.service.dto.DashboardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller para gerenciar o Dashboard.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * {@code GET /dashboard} : obter dados do dashboard.
     *
     * @return o {@link ResponseEntity} com status {@code 200 (OK)} e os dados do dashboard no corpo.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard() {
        log.debug("REST request para obter dados do Dashboard");
        DashboardDTO dashboardDTO = dashboardService.getDashboardData();
        return ResponseEntity.ok().body(dashboardDTO);
    }
}

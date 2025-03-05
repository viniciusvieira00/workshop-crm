package com.vabv.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOrderDTO.class);
        ServiceOrderDTO serviceOrderDTO1 = new ServiceOrderDTO();
        serviceOrderDTO1.setId(1L);
        ServiceOrderDTO serviceOrderDTO2 = new ServiceOrderDTO();
        assertThat(serviceOrderDTO1).isNotEqualTo(serviceOrderDTO2);
        serviceOrderDTO2.setId(serviceOrderDTO1.getId());
        assertThat(serviceOrderDTO1).isEqualTo(serviceOrderDTO2);
        serviceOrderDTO2.setId(2L);
        assertThat(serviceOrderDTO1).isNotEqualTo(serviceOrderDTO2);
        serviceOrderDTO1.setId(null);
        assertThat(serviceOrderDTO1).isNotEqualTo(serviceOrderDTO2);
    }
}

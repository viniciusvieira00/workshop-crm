package com.vabv.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceOrderTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOrderTypeDTO.class);
        ServiceOrderTypeDTO serviceOrderTypeDTO1 = new ServiceOrderTypeDTO();
        serviceOrderTypeDTO1.setId(1L);
        ServiceOrderTypeDTO serviceOrderTypeDTO2 = new ServiceOrderTypeDTO();
        assertThat(serviceOrderTypeDTO1).isNotEqualTo(serviceOrderTypeDTO2);
        serviceOrderTypeDTO2.setId(serviceOrderTypeDTO1.getId());
        assertThat(serviceOrderTypeDTO1).isEqualTo(serviceOrderTypeDTO2);
        serviceOrderTypeDTO2.setId(2L);
        assertThat(serviceOrderTypeDTO1).isNotEqualTo(serviceOrderTypeDTO2);
        serviceOrderTypeDTO1.setId(null);
        assertThat(serviceOrderTypeDTO1).isNotEqualTo(serviceOrderTypeDTO2);
    }
}

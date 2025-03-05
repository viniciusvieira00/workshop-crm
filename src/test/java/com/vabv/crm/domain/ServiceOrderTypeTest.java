package com.vabv.crm.domain;

import static com.vabv.crm.domain.ServiceOrderTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceOrderTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOrderType.class);
        ServiceOrderType serviceOrderType1 = getServiceOrderTypeSample1();
        ServiceOrderType serviceOrderType2 = new ServiceOrderType();
        assertThat(serviceOrderType1).isNotEqualTo(serviceOrderType2);

        serviceOrderType2.setId(serviceOrderType1.getId());
        assertThat(serviceOrderType1).isEqualTo(serviceOrderType2);

        serviceOrderType2 = getServiceOrderTypeSample2();
        assertThat(serviceOrderType1).isNotEqualTo(serviceOrderType2);
    }
}

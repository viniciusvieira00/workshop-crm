package com.vabv.crm.domain;

import static com.vabv.crm.domain.ProductTestSamples.*;
import static com.vabv.crm.domain.ServiceOrderTestSamples.*;
import static com.vabv.crm.domain.ServiceOrderTypeTestSamples.*;
import static com.vabv.crm.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOrder.class);
        ServiceOrder serviceOrder1 = getServiceOrderSample1();
        ServiceOrder serviceOrder2 = new ServiceOrder();
        assertThat(serviceOrder1).isNotEqualTo(serviceOrder2);

        serviceOrder2.setId(serviceOrder1.getId());
        assertThat(serviceOrder1).isEqualTo(serviceOrder2);

        serviceOrder2 = getServiceOrderSample2();
        assertThat(serviceOrder1).isNotEqualTo(serviceOrder2);
    }

    @Test
    void typeTest() {
        ServiceOrder serviceOrder = getServiceOrderRandomSampleGenerator();
        ServiceOrderType serviceOrderTypeBack = getServiceOrderTypeRandomSampleGenerator();

        serviceOrder.setType(serviceOrderTypeBack);
        assertThat(serviceOrder.getType()).isEqualTo(serviceOrderTypeBack);

        serviceOrder.type(null);
        assertThat(serviceOrder.getType()).isNull();
    }

    @Test
    void vehicleTest() {
        ServiceOrder serviceOrder = getServiceOrderRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        serviceOrder.setVehicle(vehicleBack);
        assertThat(serviceOrder.getVehicle()).isEqualTo(vehicleBack);

        serviceOrder.vehicle(null);
        assertThat(serviceOrder.getVehicle()).isNull();
    }

    @Test
    void productsTest() {
        ServiceOrder serviceOrder = getServiceOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        serviceOrder.addProducts(productBack);
        assertThat(serviceOrder.getProducts()).containsOnly(productBack);

        serviceOrder.removeProducts(productBack);
        assertThat(serviceOrder.getProducts()).doesNotContain(productBack);

        serviceOrder.products(new HashSet<>(Set.of(productBack)));
        assertThat(serviceOrder.getProducts()).containsOnly(productBack);

        serviceOrder.setProducts(new HashSet<>());
        assertThat(serviceOrder.getProducts()).doesNotContain(productBack);
    }
}

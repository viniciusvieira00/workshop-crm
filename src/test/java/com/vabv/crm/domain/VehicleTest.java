package com.vabv.crm.domain;

import static com.vabv.crm.domain.ClientTestSamples.*;
import static com.vabv.crm.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void clientTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        vehicle.setClient(clientBack);
        assertThat(vehicle.getClient()).isEqualTo(clientBack);

        vehicle.client(null);
        assertThat(vehicle.getClient()).isNull();
    }
}

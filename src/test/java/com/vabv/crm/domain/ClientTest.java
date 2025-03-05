package com.vabv.crm.domain;

import static com.vabv.crm.domain.ClientTestSamples.*;
import static com.vabv.crm.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void vehiclesTest() {
        Client client = getClientRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        client.addVehicles(vehicleBack);
        assertThat(client.getVehicles()).containsOnly(vehicleBack);
        assertThat(vehicleBack.getClient()).isEqualTo(client);

        client.removeVehicles(vehicleBack);
        assertThat(client.getVehicles()).doesNotContain(vehicleBack);
        assertThat(vehicleBack.getClient()).isNull();

        client.vehicles(new HashSet<>(Set.of(vehicleBack)));
        assertThat(client.getVehicles()).containsOnly(vehicleBack);
        assertThat(vehicleBack.getClient()).isEqualTo(client);

        client.setVehicles(new HashSet<>());
        assertThat(client.getVehicles()).doesNotContain(vehicleBack);
        assertThat(vehicleBack.getClient()).isNull();
    }
}

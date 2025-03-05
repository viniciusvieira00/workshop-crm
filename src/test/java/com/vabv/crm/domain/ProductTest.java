package com.vabv.crm.domain;

import static com.vabv.crm.domain.ProductTestSamples.*;
import static com.vabv.crm.domain.ServiceOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.vabv.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void serviceOrdersTest() {
        Product product = getProductRandomSampleGenerator();
        ServiceOrder serviceOrderBack = getServiceOrderRandomSampleGenerator();

        product.addServiceOrders(serviceOrderBack);
        assertThat(product.getServiceOrders()).containsOnly(serviceOrderBack);
        assertThat(serviceOrderBack.getProducts()).containsOnly(product);

        product.removeServiceOrders(serviceOrderBack);
        assertThat(product.getServiceOrders()).doesNotContain(serviceOrderBack);
        assertThat(serviceOrderBack.getProducts()).doesNotContain(product);

        product.serviceOrders(new HashSet<>(Set.of(serviceOrderBack)));
        assertThat(product.getServiceOrders()).containsOnly(serviceOrderBack);
        assertThat(serviceOrderBack.getProducts()).containsOnly(product);

        product.setServiceOrders(new HashSet<>());
        assertThat(product.getServiceOrders()).doesNotContain(serviceOrderBack);
        assertThat(serviceOrderBack.getProducts()).doesNotContain(product);
    }
}

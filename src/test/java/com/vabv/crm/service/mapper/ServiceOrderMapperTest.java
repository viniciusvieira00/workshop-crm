package com.vabv.crm.service.mapper;

import static com.vabv.crm.domain.ServiceOrderAsserts.*;
import static com.vabv.crm.domain.ServiceOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceOrderMapperTest {

    private ServiceOrderMapper serviceOrderMapper;

    @BeforeEach
    void setUp() {
        serviceOrderMapper = new ServiceOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceOrderSample1();
        var actual = serviceOrderMapper.toEntity(serviceOrderMapper.toDto(expected));
        assertServiceOrderAllPropertiesEquals(expected, actual);
    }
}

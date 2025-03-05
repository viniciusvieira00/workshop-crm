package com.vabv.crm.service.mapper;

import static com.vabv.crm.domain.ServiceOrderTypeAsserts.*;
import static com.vabv.crm.domain.ServiceOrderTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceOrderTypeMapperTest {

    private ServiceOrderTypeMapper serviceOrderTypeMapper;

    @BeforeEach
    void setUp() {
        serviceOrderTypeMapper = new ServiceOrderTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceOrderTypeSample1();
        var actual = serviceOrderTypeMapper.toEntity(serviceOrderTypeMapper.toDto(expected));
        assertServiceOrderTypeAllPropertiesEquals(expected, actual);
    }
}

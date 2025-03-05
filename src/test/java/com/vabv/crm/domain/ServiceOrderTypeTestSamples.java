package com.vabv.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceOrderTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceOrderType getServiceOrderTypeSample1() {
        return new ServiceOrderType()
            .id(1L)
            .name("name1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ServiceOrderType getServiceOrderTypeSample2() {
        return new ServiceOrderType()
            .id(2L)
            .name("name2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ServiceOrderType getServiceOrderTypeRandomSampleGenerator() {
        return new ServiceOrderType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}

package com.vabv.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceOrder getServiceOrderSample1() {
        return new ServiceOrder().id(1L).notes("notes1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ServiceOrder getServiceOrderSample2() {
        return new ServiceOrder().id(2L).notes("notes2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ServiceOrder getServiceOrderRandomSampleGenerator() {
        return new ServiceOrder()
            .id(longCount.incrementAndGet())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}

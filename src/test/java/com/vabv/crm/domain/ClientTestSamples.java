package com.vabv.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .documentNumber("documentNumber1")
            .name("name1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .alternativePhoneNumber("alternativePhoneNumber1")
            .address("address1")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .notes("notes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .documentNumber("documentNumber2")
            .name("name2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .alternativePhoneNumber("alternativePhoneNumber2")
            .address("address2")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .notes("notes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .documentNumber(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .alternativePhoneNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}

package com.vabv.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle()
            .id(1L)
            .licensePlate("licensePlate1")
            .model("model1")
            .brand("brand1")
            .modelYear(1)
            .color("color1")
            .renavam("renavam1")
            .fuelType("fuelType1")
            .chassiNumber("chassiNumber1")
            .currentMileage(1)
            .lastMaintenanceMileage(1)
            .nextMaintenanceMileage(1)
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle()
            .id(2L)
            .licensePlate("licensePlate2")
            .model("model2")
            .brand("brand2")
            .modelYear(2)
            .color("color2")
            .renavam("renavam2")
            .fuelType("fuelType2")
            .chassiNumber("chassiNumber2")
            .currentMileage(2)
            .lastMaintenanceMileage(2)
            .nextMaintenanceMileage(2)
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(longCount.incrementAndGet())
            .licensePlate(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .brand(UUID.randomUUID().toString())
            .modelYear(intCount.incrementAndGet())
            .color(UUID.randomUUID().toString())
            .renavam(UUID.randomUUID().toString())
            .fuelType(UUID.randomUUID().toString())
            .chassiNumber(UUID.randomUUID().toString())
            .currentMileage(intCount.incrementAndGet())
            .lastMaintenanceMileage(intCount.incrementAndGet())
            .nextMaintenanceMileage(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}

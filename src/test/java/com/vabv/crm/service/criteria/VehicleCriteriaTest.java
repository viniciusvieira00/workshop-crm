package com.vabv.crm.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VehicleCriteriaTest {

    @Test
    void newVehicleCriteriaHasAllFiltersNullTest() {
        var vehicleCriteria = new VehicleCriteria();
        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void vehicleCriteriaFluentMethodsCreatesFiltersTest() {
        var vehicleCriteria = new VehicleCriteria();

        setAllFilters(vehicleCriteria);

        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void vehicleCriteriaCopyCreatesNullFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void vehicleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        setAllFilters(vehicleCriteria);

        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vehicleCriteria = new VehicleCriteria();

        assertThat(vehicleCriteria).hasToString("VehicleCriteria{}");
    }

    private static void setAllFilters(VehicleCriteria vehicleCriteria) {
        vehicleCriteria.id();
        vehicleCriteria.licensePlate();
        vehicleCriteria.model();
        vehicleCriteria.brand();
        vehicleCriteria.fabricationDate();
        vehicleCriteria.modelYear();
        vehicleCriteria.color();
        vehicleCriteria.renavam();
        vehicleCriteria.fuelType();
        vehicleCriteria.chassiNumber();
        vehicleCriteria.currentMileage();
        vehicleCriteria.lastMaintenanceDate();
        vehicleCriteria.lastMaintenanceMileage();
        vehicleCriteria.nextMaintenanceDate();
        vehicleCriteria.nextMaintenanceMileage();
        vehicleCriteria.description();
        vehicleCriteria.status();
        vehicleCriteria.createdBy();
        vehicleCriteria.createdDate();
        vehicleCriteria.lastModifiedBy();
        vehicleCriteria.lastModifiedDate();
        vehicleCriteria.clientId();
        vehicleCriteria.distinct();
    }

    private static Condition<VehicleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLicensePlate()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getFabricationDate()) &&
                condition.apply(criteria.getModelYear()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getRenavam()) &&
                condition.apply(criteria.getFuelType()) &&
                condition.apply(criteria.getChassiNumber()) &&
                condition.apply(criteria.getCurrentMileage()) &&
                condition.apply(criteria.getLastMaintenanceDate()) &&
                condition.apply(criteria.getLastMaintenanceMileage()) &&
                condition.apply(criteria.getNextMaintenanceDate()) &&
                condition.apply(criteria.getNextMaintenanceMileage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VehicleCriteria> copyFiltersAre(VehicleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLicensePlate(), copy.getLicensePlate()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getFabricationDate(), copy.getFabricationDate()) &&
                condition.apply(criteria.getModelYear(), copy.getModelYear()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getRenavam(), copy.getRenavam()) &&
                condition.apply(criteria.getFuelType(), copy.getFuelType()) &&
                condition.apply(criteria.getChassiNumber(), copy.getChassiNumber()) &&
                condition.apply(criteria.getCurrentMileage(), copy.getCurrentMileage()) &&
                condition.apply(criteria.getLastMaintenanceDate(), copy.getLastMaintenanceDate()) &&
                condition.apply(criteria.getLastMaintenanceMileage(), copy.getLastMaintenanceMileage()) &&
                condition.apply(criteria.getNextMaintenanceDate(), copy.getNextMaintenanceDate()) &&
                condition.apply(criteria.getNextMaintenanceMileage(), copy.getNextMaintenanceMileage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

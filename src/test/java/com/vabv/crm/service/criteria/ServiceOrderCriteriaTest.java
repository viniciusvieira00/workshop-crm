package com.vabv.crm.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceOrderCriteriaTest {

    @Test
    void newServiceOrderCriteriaHasAllFiltersNullTest() {
        var serviceOrderCriteria = new ServiceOrderCriteria();
        assertThat(serviceOrderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void serviceOrderCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceOrderCriteria = new ServiceOrderCriteria();

        setAllFilters(serviceOrderCriteria);

        assertThat(serviceOrderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void serviceOrderCriteriaCopyCreatesNullFilterTest() {
        var serviceOrderCriteria = new ServiceOrderCriteria();
        var copy = serviceOrderCriteria.copy();

        assertThat(serviceOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceOrderCriteria)
        );
    }

    @Test
    void serviceOrderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceOrderCriteria = new ServiceOrderCriteria();
        setAllFilters(serviceOrderCriteria);

        var copy = serviceOrderCriteria.copy();

        assertThat(serviceOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceOrderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceOrderCriteria = new ServiceOrderCriteria();

        assertThat(serviceOrderCriteria).hasToString("ServiceOrderCriteria{}");
    }

    private static void setAllFilters(ServiceOrderCriteria serviceOrderCriteria) {
        serviceOrderCriteria.id();
        serviceOrderCriteria.creationDate();
        serviceOrderCriteria.startDate();
        serviceOrderCriteria.completionDate();
        serviceOrderCriteria.initialCost();
        serviceOrderCriteria.additionalCost();
        serviceOrderCriteria.totalCost();
        serviceOrderCriteria.notes();
        serviceOrderCriteria.status();
        serviceOrderCriteria.createdBy();
        serviceOrderCriteria.createdDate();
        serviceOrderCriteria.lastModifiedBy();
        serviceOrderCriteria.lastModifiedDate();
        serviceOrderCriteria.typeId();
        serviceOrderCriteria.vehicleId();
        serviceOrderCriteria.productsId();
        serviceOrderCriteria.distinct();
    }

    private static Condition<ServiceOrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreationDate()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getCompletionDate()) &&
                condition.apply(criteria.getInitialCost()) &&
                condition.apply(criteria.getAdditionalCost()) &&
                condition.apply(criteria.getTotalCost()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getTypeId()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getProductsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceOrderCriteria> copyFiltersAre(
        ServiceOrderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreationDate(), copy.getCreationDate()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getCompletionDate(), copy.getCompletionDate()) &&
                condition.apply(criteria.getInitialCost(), copy.getInitialCost()) &&
                condition.apply(criteria.getAdditionalCost(), copy.getAdditionalCost()) &&
                condition.apply(criteria.getTotalCost(), copy.getTotalCost()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getTypeId(), copy.getTypeId()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getProductsId(), copy.getProductsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

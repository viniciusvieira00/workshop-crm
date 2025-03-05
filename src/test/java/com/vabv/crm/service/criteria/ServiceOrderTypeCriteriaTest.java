package com.vabv.crm.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceOrderTypeCriteriaTest {

    @Test
    void newServiceOrderTypeCriteriaHasAllFiltersNullTest() {
        var serviceOrderTypeCriteria = new ServiceOrderTypeCriteria();
        assertThat(serviceOrderTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void serviceOrderTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceOrderTypeCriteria = new ServiceOrderTypeCriteria();

        setAllFilters(serviceOrderTypeCriteria);

        assertThat(serviceOrderTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void serviceOrderTypeCriteriaCopyCreatesNullFilterTest() {
        var serviceOrderTypeCriteria = new ServiceOrderTypeCriteria();
        var copy = serviceOrderTypeCriteria.copy();

        assertThat(serviceOrderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceOrderTypeCriteria)
        );
    }

    @Test
    void serviceOrderTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceOrderTypeCriteria = new ServiceOrderTypeCriteria();
        setAllFilters(serviceOrderTypeCriteria);

        var copy = serviceOrderTypeCriteria.copy();

        assertThat(serviceOrderTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceOrderTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceOrderTypeCriteria = new ServiceOrderTypeCriteria();

        assertThat(serviceOrderTypeCriteria).hasToString("ServiceOrderTypeCriteria{}");
    }

    private static void setAllFilters(ServiceOrderTypeCriteria serviceOrderTypeCriteria) {
        serviceOrderTypeCriteria.id();
        serviceOrderTypeCriteria.name();
        serviceOrderTypeCriteria.description();
        serviceOrderTypeCriteria.price();
        serviceOrderTypeCriteria.createdBy();
        serviceOrderTypeCriteria.createdDate();
        serviceOrderTypeCriteria.lastModifiedBy();
        serviceOrderTypeCriteria.lastModifiedDate();
        serviceOrderTypeCriteria.distinct();
    }

    private static Condition<ServiceOrderTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceOrderTypeCriteria> copyFiltersAre(
        ServiceOrderTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

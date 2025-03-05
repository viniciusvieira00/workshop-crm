package com.vabv.crm.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClientCriteriaTest {

    @Test
    void newClientCriteriaHasAllFiltersNullTest() {
        var clientCriteria = new ClientCriteria();
        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientCriteriaFluentMethodsCreatesFiltersTest() {
        var clientCriteria = new ClientCriteria();

        setAllFilters(clientCriteria);

        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientCriteriaCopyCreatesNullFilterTest() {
        var clientCriteria = new ClientCriteria();
        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void clientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientCriteria = new ClientCriteria();
        setAllFilters(clientCriteria);

        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientCriteria = new ClientCriteria();

        assertThat(clientCriteria).hasToString("ClientCriteria{}");
    }

    private static void setAllFilters(ClientCriteria clientCriteria) {
        clientCriteria.id();
        clientCriteria.documentNumber();
        clientCriteria.name();
        clientCriteria.email();
        clientCriteria.phoneNumber();
        clientCriteria.alternativePhoneNumber();
        clientCriteria.address();
        clientCriteria.city();
        clientCriteria.state();
        clientCriteria.zipCode();
        clientCriteria.clientType();
        clientCriteria.notes();
        clientCriteria.createdBy();
        clientCriteria.createdDate();
        clientCriteria.lastModifiedBy();
        clientCriteria.lastModifiedDate();
        clientCriteria.vehiclesId();
        clientCriteria.distinct();
    }

    private static Condition<ClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentNumber()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getAlternativePhoneNumber()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getState()) &&
                condition.apply(criteria.getZipCode()) &&
                condition.apply(criteria.getClientType()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getVehiclesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientCriteria> copyFiltersAre(ClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentNumber(), copy.getDocumentNumber()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getAlternativePhoneNumber(), copy.getAlternativePhoneNumber()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getState(), copy.getState()) &&
                condition.apply(criteria.getZipCode(), copy.getZipCode()) &&
                condition.apply(criteria.getClientType(), copy.getClientType()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getVehiclesId(), copy.getVehiclesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

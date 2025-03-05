package com.vabv.crm.domain;

import static com.vabv.crm.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ServiceOrderTypeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceOrderTypeAllPropertiesEquals(ServiceOrderType expected, ServiceOrderType actual) {
        assertServiceOrderTypeAutoGeneratedPropertiesEquals(expected, actual);
        assertServiceOrderTypeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceOrderTypeAllUpdatablePropertiesEquals(ServiceOrderType expected, ServiceOrderType actual) {
        assertServiceOrderTypeUpdatableFieldsEquals(expected, actual);
        assertServiceOrderTypeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceOrderTypeAutoGeneratedPropertiesEquals(ServiceOrderType expected, ServiceOrderType actual) {
        assertThat(actual)
            .as("Verify ServiceOrderType auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceOrderTypeUpdatableFieldsEquals(ServiceOrderType expected, ServiceOrderType actual) {
        assertThat(actual)
            .as("Verify ServiceOrderType relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getDescription()).as("check description").isEqualTo(expected.getDescription()))
            .satisfies(a -> assertThat(a.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(expected.getPrice()))
            .satisfies(a -> assertThat(a.getCreatedBy()).as("check createdBy").isEqualTo(expected.getCreatedBy()))
            .satisfies(a -> assertThat(a.getCreatedDate()).as("check createdDate").isEqualTo(expected.getCreatedDate()))
            .satisfies(a -> assertThat(a.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(expected.getLastModifiedBy()))
            .satisfies(a -> assertThat(a.getLastModifiedDate()).as("check lastModifiedDate").isEqualTo(expected.getLastModifiedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceOrderTypeUpdatableRelationshipsEquals(ServiceOrderType expected, ServiceOrderType actual) {
        // empty method
    }
}

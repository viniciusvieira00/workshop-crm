package com.vabv.crm.repository;

import com.vabv.crm.domain.ServiceOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ServiceOrderRepositoryWithBagRelationshipsImpl implements ServiceOrderRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SERVICEORDERS_PARAMETER = "serviceOrders";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ServiceOrder> fetchBagRelationships(Optional<ServiceOrder> serviceOrder) {
        return serviceOrder.map(this::fetchProducts);
    }

    @Override
    public Page<ServiceOrder> fetchBagRelationships(Page<ServiceOrder> serviceOrders) {
        return new PageImpl<>(
            fetchBagRelationships(serviceOrders.getContent()),
            serviceOrders.getPageable(),
            serviceOrders.getTotalElements()
        );
    }

    @Override
    public List<ServiceOrder> fetchBagRelationships(List<ServiceOrder> serviceOrders) {
        return Optional.of(serviceOrders).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    ServiceOrder fetchProducts(ServiceOrder result) {
        return entityManager
            .createQuery(
                "select serviceOrder from ServiceOrder serviceOrder left join fetch serviceOrder.products where serviceOrder.id = :id",
                ServiceOrder.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ServiceOrder> fetchProducts(List<ServiceOrder> serviceOrders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, serviceOrders.size()).forEach(index -> order.put(serviceOrders.get(index).getId(), index));
        List<ServiceOrder> result = entityManager
            .createQuery(
                "select serviceOrder from ServiceOrder serviceOrder left join fetch serviceOrder.products where serviceOrder in :serviceOrders",
                ServiceOrder.class
            )
            .setParameter(SERVICEORDERS_PARAMETER, serviceOrders)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

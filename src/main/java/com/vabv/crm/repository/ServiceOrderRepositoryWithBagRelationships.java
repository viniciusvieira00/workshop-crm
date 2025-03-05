package com.vabv.crm.repository;

import com.vabv.crm.domain.ServiceOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ServiceOrderRepositoryWithBagRelationships {
    Optional<ServiceOrder> fetchBagRelationships(Optional<ServiceOrder> serviceOrder);

    List<ServiceOrder> fetchBagRelationships(List<ServiceOrder> serviceOrders);

    Page<ServiceOrder> fetchBagRelationships(Page<ServiceOrder> serviceOrders);
}

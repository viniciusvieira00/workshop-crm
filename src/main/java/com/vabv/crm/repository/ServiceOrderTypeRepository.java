package com.vabv.crm.repository;

import com.vabv.crm.domain.ServiceOrderType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceOrderType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceOrderTypeRepository extends JpaRepository<ServiceOrderType, Long>, JpaSpecificationExecutor<ServiceOrderType> {}

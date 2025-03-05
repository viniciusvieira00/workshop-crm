package com.vabv.crm.repository;

import com.vabv.crm.domain.ServiceOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceOrder entity.
 *
 * When extending this class, extend ServiceOrderRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ServiceOrderRepository
    extends ServiceOrderRepositoryWithBagRelationships, JpaRepository<ServiceOrder, Long>, JpaSpecificationExecutor<ServiceOrder> {
    default Optional<ServiceOrder> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ServiceOrder> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ServiceOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select serviceOrder from ServiceOrder serviceOrder left join fetch serviceOrder.type left join fetch serviceOrder.vehicle",
        countQuery = "select count(serviceOrder) from ServiceOrder serviceOrder"
    )
    Page<ServiceOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceOrder from ServiceOrder serviceOrder left join fetch serviceOrder.type left join fetch serviceOrder.vehicle")
    List<ServiceOrder> findAllWithToOneRelationships();

    @Query(
        "select serviceOrder from ServiceOrder serviceOrder left join fetch serviceOrder.type left join fetch serviceOrder.vehicle where serviceOrder.id =:id"
    )
    Optional<ServiceOrder> findOneWithToOneRelationships(@Param("id") Long id);
}

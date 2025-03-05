package com.vabv.crm.repository;

import com.vabv.crm.domain.Vehicle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    default Optional<Vehicle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vehicle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vehicle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vehicle from Vehicle vehicle left join fetch vehicle.client",
        countQuery = "select count(vehicle) from Vehicle vehicle"
    )
    Page<Vehicle> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vehicle from Vehicle vehicle left join fetch vehicle.client")
    List<Vehicle> findAllWithToOneRelationships();

    @Query("select vehicle from Vehicle vehicle left join fetch vehicle.client where vehicle.id =:id")
    Optional<Vehicle> findOneWithToOneRelationships(@Param("id") Long id);
}

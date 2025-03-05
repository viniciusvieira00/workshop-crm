package com.vabv.crm.service.mapper;

import com.vabv.crm.domain.Product;
import com.vabv.crm.domain.ServiceOrder;
import com.vabv.crm.service.dto.ProductDTO;
import com.vabv.crm.service.dto.ServiceOrderDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "serviceOrders", source = "serviceOrders", qualifiedByName = "serviceOrderIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "serviceOrders", ignore = true)
    @Mapping(target = "removeServiceOrders", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("serviceOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceOrderDTO toDtoServiceOrderId(ServiceOrder serviceOrder);

    @Named("serviceOrderIdSet")
    default Set<ServiceOrderDTO> toDtoServiceOrderIdSet(Set<ServiceOrder> serviceOrder) {
        return serviceOrder.stream().map(this::toDtoServiceOrderId).collect(Collectors.toSet());
    }
}

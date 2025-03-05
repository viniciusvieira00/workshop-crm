package com.vabv.crm.service.mapper;

import com.vabv.crm.domain.Product;
import com.vabv.crm.domain.ServiceOrder;
import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.service.dto.ProductDTO;
import com.vabv.crm.service.dto.ServiceOrderDTO;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import com.vabv.crm.service.dto.VehicleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceOrder} and its DTO {@link ServiceOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceOrderMapper extends EntityMapper<ServiceOrderDTO, ServiceOrder> {
    @Mapping(target = "type", source = "type", qualifiedByName = "serviceOrderTypeName")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleLicensePlate")
    @Mapping(target = "products", source = "products", qualifiedByName = "productNameSet")
    ServiceOrderDTO toDto(ServiceOrder s);

    @Mapping(target = "removeProducts", ignore = true)
    ServiceOrder toEntity(ServiceOrderDTO serviceOrderDTO);

    @Named("serviceOrderTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceOrderTypeDTO toDtoServiceOrderTypeName(ServiceOrderType serviceOrderType);

    @Named("vehicleLicensePlate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "licensePlate", source = "licensePlate")
    VehicleDTO toDtoVehicleLicensePlate(Vehicle vehicle);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "sellPrice", source = "sellPrice")
    ProductDTO toDtoProductName(Product product);

    @Named("productNameSet")
    default Set<ProductDTO> toDtoProductNameSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductName).collect(Collectors.toSet());
    }
}

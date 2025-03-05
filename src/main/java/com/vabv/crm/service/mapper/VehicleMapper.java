package com.vabv.crm.service.mapper;

import com.vabv.crm.domain.Client;
import com.vabv.crm.domain.Vehicle;
import com.vabv.crm.service.dto.ClientDTO;
import com.vabv.crm.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicle} and its DTO {@link VehicleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientName")
    VehicleDTO toDto(Vehicle s);

    @Named("clientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClientDTO toDtoClientName(Client client);
}

package com.vabv.crm.service.mapper;

import com.vabv.crm.domain.ServiceOrderType;
import com.vabv.crm.service.dto.ServiceOrderTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceOrderType} and its DTO {@link ServiceOrderTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceOrderTypeMapper extends EntityMapper<ServiceOrderTypeDTO, ServiceOrderType> {}

package com.vabv.crm.service.mapper;

import com.vabv.crm.domain.Client;
import com.vabv.crm.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}

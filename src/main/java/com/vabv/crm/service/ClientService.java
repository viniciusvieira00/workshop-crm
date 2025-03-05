package com.vabv.crm.service;

import com.vabv.crm.domain.Client;
import com.vabv.crm.repository.ClientRepository;
import com.vabv.crm.service.dto.ClientDTO;
import com.vabv.crm.service.mapper.ClientMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.vabv.crm.domain.Client}.
 */
@Service
@Transactional
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientDTO save(ClientDTO clientDTO) {
        LOG.debug("Request to save Client : {}", clientDTO);

        // Preencher campos de auditoria para novos registros
        if (clientDTO.getId() == null) {
            clientDTO.setCreatedBy(getCurrentUserLogin());
            clientDTO.setCreatedDate(Instant.now());
        }

        // Sempre atualizar os campos de última modificação
        clientDTO.setLastModifiedBy(getCurrentUserLogin());
        clientDTO.setLastModifiedDate(Instant.now());

        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    /**
     * Update a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientDTO update(ClientDTO clientDTO) {
        LOG.debug("Request to update Client : {}", clientDTO);

        // Preservar os dados de criação originais
        Optional<ClientDTO> existingClientOpt = findOne(clientDTO.getId());
        if (existingClientOpt.isPresent()) {
            ClientDTO existingClient = existingClientOpt.get();
            clientDTO.setCreatedBy(existingClient.getCreatedBy());
            clientDTO.setCreatedDate(existingClient.getCreatedDate());
        }

        // Atualizar os campos de última modificação
        clientDTO.setLastModifiedBy(getCurrentUserLogin());
        clientDTO.setLastModifiedDate(Instant.now());

        Client client = clientMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    /**
     * Partially update a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        LOG.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(existingClient -> {
                // Preservar os dados de criação originais
                if (clientDTO.getCreatedBy() == null) {
                    clientDTO.setCreatedBy(existingClient.getCreatedBy());
                }
                if (clientDTO.getCreatedDate() == null) {
                    clientDTO.setCreatedDate(existingClient.getCreatedDate());
                }

                // Atualizar os campos de última modificação
                clientDTO.setLastModifiedBy(getCurrentUserLogin());
                clientDTO.setLastModifiedDate(Instant.now());

                clientMapper.partialUpdate(existingClient, clientDTO);

                return existingClient;
            })
            .map(clientRepository::save)
            .map(clientMapper::toDto);
    }

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        LOG.debug("Request to get all Clients");
        return clientRepository.findAll().stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findOne(Long id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }

    /**
     * Obtém o login do usuário atual.
     *
     * @return o login do usuário atual ou "system" se não houver usuário autenticado
     */
    private String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "system";
    }
}

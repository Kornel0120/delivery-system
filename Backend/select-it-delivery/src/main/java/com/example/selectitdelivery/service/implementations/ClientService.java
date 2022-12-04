package com.example.selectitdelivery.service.implementations;

import com.example.selectitdelivery.dao.entity.ClientEntity;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.dao.repositories.ClientRepository;
import com.example.selectitdelivery.service.exceptions.ClientAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;
import com.example.selectitdelivery.service.interfaces.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;

    private static Client convertEntityToModel(ClientEntity client) {
        return new Client(
                client.getClientId(),
                client.getUser(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone()
        );
    }

    private static ClientEntity convertModelToEntity(Client client) {
        return ClientEntity.builder()
                .clientId(client.getClientId())
                .user(client.getUser())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phone(client.getPhone())
                .build();
    }

    @Override
    public Client record(Client client) throws ClientAlreadyExistsException {
        if(clientRepository.findById(client.getClientId()).isPresent()) {
            throw new ClientAlreadyExistsException();
        }

        ClientEntity recordClientEntity = clientRepository.save(convertModelToEntity(client));

        return convertEntityToModel(recordClientEntity);
    }

    @Override
    public Client readById(long id) throws ClientNotFoundException {
        if(clientRepository.findById(id).isEmpty()) {
            throw new ClientNotFoundException();
        }

        return convertEntityToModel(clientRepository.findById(id).get());
    }

    @Override
    public Client readByUserId(long userId) throws ClientNotFoundException {
        if(clientRepository.readByUserUserId(userId).isEmpty()) {
            throw new ClientNotFoundException();
        }

        return convertEntityToModel(clientRepository.readByUserUserId(userId).get());
    }

    @Override
    public Collection<Client> readAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientService::convertEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Client modify(Client client) throws ClientNotFoundException {
        ClientEntity clientEntity = convertModelToEntity(client);
        if(clientRepository.findById(clientEntity.getClientId()).isEmpty()) {
            throw new ClientNotFoundException();
        }

        return convertEntityToModel(clientRepository.save(clientEntity));
    }

    @Override
    public void delete(Client client) throws ClientNotFoundException {
        if(clientRepository.findById(client.getClientId()).isEmpty()) {
            throw new ClientNotFoundException();
        }

        clientRepository.delete(convertModelToEntity(client));
    }
}

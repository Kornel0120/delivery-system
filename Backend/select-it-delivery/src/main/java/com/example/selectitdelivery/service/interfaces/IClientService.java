package com.example.selectitdelivery.service.interfaces;

import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.service.exceptions.ClientAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;

import java.util.Collection;

public interface IClientService {
    Client record(Client client) throws ClientAlreadyExistsException;
    Client readById(long id) throws ClientNotFoundException;
    Client readByUserId(long userId) throws ClientNotFoundException;
    Collection<Client> readAll();
    Client modify(Client client) throws ClientNotFoundException;
    void delete(Client client) throws ClientNotFoundException;
}

package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ClientMapper {
    ClientDto clientToClientDto(Client client);

    Client clientDtoToClient(ClientDto clientDto);
}

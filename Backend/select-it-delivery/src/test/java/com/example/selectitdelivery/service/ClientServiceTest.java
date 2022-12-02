package com.example.selectitdelivery.service;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.ClientEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.dao.repositories.ClientRepository;
import com.example.selectitdelivery.service.exceptions.ClientAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;
import com.example.selectitdelivery.service.implementations.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientService clientService;

    @Test
    void recordClientHappyPath() throws ClientAlreadyExistsException {
        Client client = TestDataProvider.getClient();
        ClientEntity clientEntity = TestDataProvider.getClientEntity();
        when(clientRepository.findById(any())).thenReturn(Optional.empty());
        when(clientRepository.save(any())).thenReturn(clientEntity);
        Client actual = clientService.record(client);
        assertThat(actual).usingRecursiveComparison().isEqualTo(client);
    }

    @Test
    void readByIdHappyPath() throws ClientNotFoundException {
        when(clientRepository.findById(TestDataProvider.clientId))
                .thenReturn(Optional.of(TestDataProvider.getClientEntity()));
        Client expected = TestDataProvider.getClient();
        Client actual = clientService.readById(TestDataProvider.clientId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void modifyClientHappyPath() throws ClientNotFoundException {
        Client client = TestDataProvider.getClient();
        ClientEntity clientEntity = TestDataProvider.getClientEntity();
        when(clientRepository.findById(client.getClientId()))
                .thenReturn(Optional.of(clientEntity));
        when(clientRepository.save(any())).thenReturn(clientEntity);
        Client actual = clientService.modify(client);
        assertThat(actual).usingRecursiveComparison().isEqualTo(client);
    }

    @Test
    void readAllHappyPath() {
        List<ClientEntity> clientEntities = List.of(
                TestDataProvider.getClientEntity()
        );

        Collection<Client> expectedClients = List.of(
                TestDataProvider.getClient()
        );

        when(clientRepository.findAll()).thenReturn(clientEntities);
        Collection<Client> actualClients = clientService.readAll();
        assertThat(actualClients).usingRecursiveComparison().isEqualTo(expectedClients);
    }

    @Test
    void deleteClientHappyPath() throws ClientNotFoundException {
        Client client = TestDataProvider.getClient();
        ClientEntity clientEntity = TestDataProvider.getClientEntity();
        when(clientRepository.findById(client.getClientId()))
                .thenReturn(Optional.of(clientEntity));
        clientService.delete(client);
    }

    @Test
    void recordThrowsClientAlreadyExistsException() {
        Client client = TestDataProvider.getClient();
        ClientEntity clientEntity = TestDataProvider.getClientEntity();
        when(clientRepository.findById(TestDataProvider.clientId))
                .thenReturn(Optional.ofNullable(clientEntity));
        assertThatThrownBy(() -> clientService.record(client))
                .isInstanceOf(ClientAlreadyExistsException.class);
    }

    @Test
    void readByIdThrowsClientNotFoundException() {
        when(clientRepository.findById(TestDataProvider.clientId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> clientService.readById(TestDataProvider.clientId))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void modifyThrowsClientNotFoundException() {
        Client client = TestDataProvider.getClient();
        when(clientRepository.findById(client.getClientId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> clientService.modify(client))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void deleteThrowsClientNotFoundException() {
        Client client = TestDataProvider.getClient();
        assertThatThrownBy(() -> clientService.delete(client))
                .isInstanceOf(ClientNotFoundException.class);
    }

    private static class TestDataProvider {
        public static final long clientId = 9999;
        public static final AppUserEntity user = new AppUserEntity(9999,"ClientTest@Test.com","TestPw", Set.of(new RoleEntity(9998,"TEST_ROLE")));
        public static final String firstName = "TestFirstName";
        public static final String lastName = "TestLastName";
        public static final String phone = "00000000000";

        public static Client getClient() {
            return new Client(
                    clientId,
                    user,
                    firstName,
                    lastName,
                    phone);
        }

        public static ClientEntity getClientEntity() {
            return ClientEntity.builder()
                    .clientId(clientId)
                    .user(user)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .build();
        }
    }
}

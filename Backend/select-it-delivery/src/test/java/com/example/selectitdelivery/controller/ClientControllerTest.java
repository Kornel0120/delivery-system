package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Response.UserModifyResponse;
import com.example.selectitdelivery.controller.dto.ClientDto;
import com.example.selectitdelivery.controller.dto.ClientMapper;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;
import com.example.selectitdelivery.service.implementations.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @InjectMocks
    ClientController clientController;
    @Mock
    ClientService clientService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ClientMapper clientMapper;

    @BeforeEach
    public void setup() {
        this.clientController = new ClientController(clientService, clientMapper);
        this.clientController.passwordEncoder = passwordEncoder;
    }

    @Test
    void readAllHappyPath() {
        when(clientService.readAll()).thenReturn(List.of(TestDataProvider.getClient()));
        when(clientMapper.clientToClientDto(any())).thenReturn(TestDataProvider.getClientDto());
        Collection<ClientDto> expected = List.of(TestDataProvider.getClientDto());
        Collection<ClientDto> actual = clientController.getEmployees();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws ClientNotFoundException {
        when(clientService.readById(TestDataProvider.getClient().getClientId()))
                .thenReturn(TestDataProvider.getClient());
        ClientDto expected = TestDataProvider.getClientDto();
        when(clientMapper.clientToClientDto(any()))
                .thenReturn(TestDataProvider.getClientDto());

        ClientDto actual = clientController.readById(TestDataProvider.getClient().getClientId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void updateClientHappyPath() throws ClientNotFoundException {
        ClientDto clientDto = TestDataProvider.getClientDto();
        Client client = TestDataProvider.getClient();

        when(clientMapper.clientDtoToClient(clientDto)).thenReturn(client);
        when(clientService.modify(client)).thenReturn(client);

        ResponseEntity<?> expected = TestDataProvider.expectedResponse;
        ResponseEntity<?> actual = clientController.modify(clientDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteClientHappyPath() throws ClientNotFoundException {
        Client client = TestDataProvider.getClient();
        when(clientService.readById(TestDataProvider.clientId)).thenReturn(client);
        doNothing().when(clientService).delete(client);

        clientController.delete(TestDataProvider.clientId);
    }

    @Test
    void readByIdThrowsResponseStatusException() throws ClientNotFoundException {
        when(clientService.readById(TestDataProvider.getClient().getClientId()))
                .thenThrow(new ClientNotFoundException());

        assertThatThrownBy(() -> {
            clientController.readById(TestDataProvider.getClient().getClientId());})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateThrowsResponseStatusException() throws ClientNotFoundException {
        ClientDto clientDto = TestDataProvider.getClientDto();
        Client client = TestDataProvider.getClient();

        when(clientMapper.clientDtoToClient(clientDto)).thenReturn(client);
        when(clientService.modify(client)).thenThrow(new ClientNotFoundException());

        assertThatThrownBy(() -> {
            clientController.modify(clientDto);})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteThrowsResponseStatusException() throws ClientNotFoundException {
        final long notFoundClientId = TestDataProvider.clientId;

        doThrow(new ClientNotFoundException()).when(clientService).readById(notFoundClientId);

        assertThatThrownBy(() -> {
            clientController.delete(notFoundClientId);})
                .isInstanceOf(ResponseStatusException.class);
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

        public static ClientDto getClientDto() {
            return ClientDto.builder()
                    .clientId(clientId)
                    .user(user)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .build();
        }

        public static ResponseEntity<?> expectedResponse = ResponseEntity.ok(new UserModifyResponse("Client's account updated successfully.",
                getClient().getClientId(),
                getClient().getUser(),
                getClient().getFirstName(),
                getClient().getLastName(),
                getClient().getPhone()));
    }
}

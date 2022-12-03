package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.SignupRequest;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.controller.dto.ClientDto;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.service.exceptions.ClientAlreadyExistsException;
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

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    AuthController authController;
    @Mock
    ClientService clientService;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;


    @BeforeEach
    public void setup() {
        this.authController.passwordEncoder = passwordEncoder;
    }

    @Test
    void createEmployeeHappyPath() throws ClientAlreadyExistsException {
        Client client = TestDataProvider.getClient();
        AppUserEntity newAppUser = new AppUserEntity("ClientTest@Test.com", "TestPw", Set.of(new RoleEntity(3,"ROLE_USER")));
        SignupRequest signupRequest = new SignupRequest(
                client.getUser().getEmail(),
                client.getUser().getPassword(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone()
        );

        Client newClient = new Client(newAppUser,signupRequest.getFirstName(),signupRequest.getLastName(),signupRequest.getPhone());
        when(appUserRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(new RoleEntity(3,"ROLE_USER")));
        when(passwordEncoder.encode(any())).thenReturn(newAppUser.getPassword());
        when(clientService.record(newClient)).thenReturn(newClient);

        ResponseEntity<?> expected = ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        ResponseEntity<?> actual = authController.registerUser(signupRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createEmployeeBadRequestOnTakenEmail() {
        Client client = TestDataProvider.getClient();
        SignupRequest signupRequest = new SignupRequest(
                client.getUser().getEmail(),
                client.getUser().getPassword(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone()
        );

        when(appUserRepository.existsByEmail(any())).thenReturn(true);

        ResponseEntity<?> expected = ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        ResponseEntity<?> actual = authController.registerUser(signupRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createThrowsEmployeeAlreadyExistsException() throws ClientAlreadyExistsException {
        Client client = TestDataProvider.getClient();
        AppUserEntity newAppUser = new AppUserEntity("ClientTest@Test.com", "TestPw", Set.of(new RoleEntity(3,"ROLE_USER")));
        SignupRequest signupRequest = new SignupRequest(
                client.getUser().getEmail(),
                client.getUser().getPassword(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone()
        );

        Client newClient = new Client(newAppUser,signupRequest.getFirstName(),signupRequest.getLastName(),signupRequest.getPhone());
        when(appUserRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(new RoleEntity(3,"ROLE_USER")));
        when(passwordEncoder.encode(any())).thenReturn(newAppUser.getPassword());
        when(clientService.record(newClient)).thenThrow(new ClientAlreadyExistsException());

        assertThatThrownBy(() -> {
            authController.registerUser(signupRequest);})
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
    }
}

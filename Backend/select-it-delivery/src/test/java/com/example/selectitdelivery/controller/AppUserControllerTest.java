package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Response.UserModifyResponse;
import com.example.selectitdelivery.controller.dto.AppUserDto;
import com.example.selectitdelivery.controller.dto.AppUserMapper;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {
    @Mock
    AppUserService appUserService;

    @Mock
    AppUserMapper appUserMapper;

    @InjectMocks
    AppUserController appUserController;

    @Test
    void readAllHappyPath() {
        when(appUserService.readAll()).thenReturn(List.of(TestDataProvider.getAppUser()));
        when(appUserMapper.appUserToAppUserDto(any())).thenReturn(TestDataProvider.getAppUserDto());
        Collection<AppUserDto> expected = List.of(TestDataProvider.getAppUserDto());
        Collection<AppUserDto> actual = appUserController.getUsers();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws AppUserNotFoundException {
        when(appUserService.readById(TestDataProvider.getAppUser().getUserId()))
                .thenReturn(TestDataProvider.getAppUser());
        AppUserDto expected = TestDataProvider.getAppUserDto();
        when(appUserMapper.appUserToAppUserDto(any()))
                .thenReturn(TestDataProvider.getAppUserDto());

        AppUserDto actual = appUserController.readById(TestDataProvider.getAppUser().getUserId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /*@Test
    void createAppUserHappyPath() throws AppUserAlreadyExistsException {
        AppUser appUser = TestDataProvider.getAppUser();
        AppUserDto appUserDto = TestDataProvider.getAppUserDto();
        CreateEmployeeUserRequest createEmployeeUserRequest = new CreateEmployeeUserRequest(appUser.getEmail(),appUser.getPassword(), TestDataProvider.roleNames);

        when(appUserService.record(appUser)).thenReturn(appUser);

        ResponseEntity<?> expected = ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        ResponseEntity<?> actual = appUserController.createUser(createEmployeeUserRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }*/

    @Test
    void updateAppUserHappyPath() throws AppUserNotFoundException {
        AppUserDto appUserDto = TestDataProvider.getAppUserDto();
        AppUser appUser = TestDataProvider.getAppUser();

        when(appUserMapper.appUserDtoToAppUser(appUserDto)).thenReturn(appUser);
        when(appUserService.modify(appUser)).thenReturn(appUser);
        when(appUserMapper.appUserToAppUserDto(appUser)).thenReturn(appUserDto);

        AppUserDto expected = TestDataProvider.getAppUserDto();
        AppUserDto actual = appUserController.modify(appUserDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteHappyPath() throws AppUserNotFoundException {
        AppUser appUser = TestDataProvider.getAppUser();
        when(appUserService.readById(TestDataProvider.id)).thenReturn(appUser);
        doNothing().when(appUserService).delete(appUser);

        appUserController.delete(TestDataProvider.id);
    }

    @Test
    void readByIdThrowsAppUserNotFoundException() throws AppUserNotFoundException {
        when(appUserService.readById(TestDataProvider.getAppUser().getUserId()))
                .thenThrow(new AppUserNotFoundException());

        assertThatThrownBy(() -> {appUserController.readById(TestDataProvider.getAppUser().getUserId());})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateThrowsAppUserNotFoundException() throws AppUserNotFoundException {
        AppUserDto appUserDto = TestDataProvider.getAppUserDto();
        AppUser appUser = TestDataProvider.getAppUser();

        when(appUserMapper.appUserDtoToAppUser(appUserDto)).thenReturn(appUser);
        when(appUserService.modify(appUser)).thenThrow(new AppUserNotFoundException());

        assertThatThrownBy(() -> {appUserController.modify(appUserDto);})
                .isInstanceOf(ResponseStatusException.class);
    }

    /*@Test
    void createThrowsAppUserAlreadyExistsException() throws AppUserAlreadyExistsException {
        AppUserDto appUserDto = TestDataProvider.getAppUserDto();
        AppUser appUser = TestDataProvider.getAppUser();

        when(appUserMapper.appUserDtoToAppUser(appUserDto)).thenReturn(appUser);
        when(appUserService.record(appUser)).thenThrow(new AppUserAlreadyExistsException());

        assertThatThrownBy(() -> {appUserController.create(appUserDto);})
                .isInstanceOf(ResponseStatusException.class);
    }*/

    @Test
    void deleteThrowsAppUserNotFoundException() throws AppUserNotFoundException {
        final long notFoundAppUserId = TestDataProvider.id;

        doThrow(new AppUserNotFoundException()).when(appUserService).readById(notFoundAppUserId);

        assertThatThrownBy(() -> {appUserController.delete(notFoundAppUserId);})
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {
        public static final long id = 9999;
        public static final String email = "Test@email.com";
        public static final String password = "TestPassword";

        private static final RoleEntity roleEntityAdmin = RoleEntity.builder()
                .roleId(1)
                .roleName("TEST_ADMIN")
                .build();

        private static final RoleEntity roleEntityAUser = RoleEntity.builder()
                .roleId(3)
                .roleName("TEST_USER")
                .build();

        private static final RoleEntity[] roleValues = new RoleEntity[] {roleEntityAdmin, roleEntityAUser};
        private static final Set<RoleEntity> roles = new HashSet<>(Arrays.asList(roleValues)) {};

        public static AppUser getAppUser() {
            return new AppUser(
                    id,
                    email,
                    password,
                    roles);
        }

        public static AppUserDto getAppUserDto() {
            return AppUserDto.builder()
                    .userId(id)
                    .email(email)
                    .password(password)
                    .role(roles)
                    .build();
        }

        private static final Set<String> roleNames = new HashSet<>(Arrays.asList("ROLE_ADMIN","ROLE_EMPLOYEE")) {};

    }
}

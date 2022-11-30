package com.example.selectitdelivery.service;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.service.exceptions.AppUserAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    AppUserRepository appUserRepository;

    @InjectMocks
    AppUserService appUserService;

    @Test
    void recordAppUserHappyPath() throws AppUserAlreadyExistsException {
        AppUser appUser = TestDataProvider.getAppUser();
        AppUserEntity appUserEntity = TestDataProvider.getAppUserEntity();
        when(appUserRepository.findById(any())).thenReturn(Optional.empty());
        when(appUserRepository.save(any())).thenReturn(appUserEntity);
        AppUser actual = appUserService.record(appUser);
        assertThat(actual).usingRecursiveComparison().isEqualTo(appUser);
    }

    @Test
    void readByIdHappyPath() throws AppUserNotFoundException {
        when(appUserRepository.findById(TestDataProvider.id))
                .thenReturn(Optional.of(TestDataProvider.getAppUserEntity()));
        AppUser expected = TestDataProvider.getAppUser();
        AppUser actual = appUserService.readById(TestDataProvider.id);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void modifyAppUserHappyPath() throws AppUserNotFoundException {
        AppUser appUser = TestDataProvider.getAppUser();
        AppUserEntity appUserEntity = TestDataProvider.getAppUserEntity();
        when(appUserRepository.findById(appUser.getUserId()))
                .thenReturn(Optional.of(appUserEntity));
        when(appUserRepository.save(any())).thenReturn(appUserEntity);
        AppUser actual = appUserService.modify(appUser);
        assertThat(actual).usingRecursiveComparison().isEqualTo(appUser);
    }

    @Test
    void readAllHappyPath() {
        List<AppUserEntity> appUserEntities = List.of(
                TestDataProvider.getAppUserEntity()
        );

        Collection<AppUser> expectedAppUsers = List.of(
                TestDataProvider.getAppUser()
        );

        when(appUserRepository.findAll()).thenReturn(appUserEntities);
        Collection<AppUser> actualAppUsers = appUserService.readAll();
        assertThat(actualAppUsers).usingRecursiveComparison().isEqualTo(expectedAppUsers);
    }

    @Test
    void deleteAppUserHappyPath() throws AppUserNotFoundException {
        AppUser appUser = TestDataProvider.getAppUser();
        AppUserEntity appUserEntity = TestDataProvider.getAppUserEntity();
        when(appUserRepository.findById(appUser.getUserId()))
                .thenReturn(Optional.of(appUserEntity));
        appUserService.delete(appUser);
    }

    @Test
    void recordThrowsAppUserAlreadyExistsException() {
        AppUser appUser = TestDataProvider.getAppUser();
        AppUserEntity appUserEntity = TestDataProvider.getAppUserEntity();
        when(appUserRepository.findById(TestDataProvider.id))
                .thenReturn(Optional.ofNullable(appUserEntity));
        assertThatThrownBy(() -> appUserService.record(appUser))
                .isInstanceOf(AppUserAlreadyExistsException.class);
    }

    @Test
    void readByIdThrowsAppUserNotFoundException() {
        when(appUserRepository.findById(TestDataProvider.id))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> appUserService.readById(TestDataProvider.id))
                .isInstanceOf(AppUserNotFoundException.class);
    }

    @Test
    void modifyThrowsAppUserNotFoundException() {
        AppUser appUser = TestDataProvider.getAppUser();
        when(appUserRepository.findById(appUser.getUserId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> appUserService.modify(appUser))
                .isInstanceOf(AppUserNotFoundException.class);
    }

    @Test
    void deleteThrowsAppUserNotFoundException() {
        AppUser appUser = TestDataProvider.getAppUser();
        assertThatThrownBy(() -> appUserService.delete(appUser))
                .isInstanceOf(AppUserNotFoundException.class);
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

        public static AppUserEntity getAppUserEntity() {
            return AppUserEntity.builder()
                    .userId(id)
                    .email(email)
                    .password(password)
                    .role(roles)
                    .build();
        }
    }
}

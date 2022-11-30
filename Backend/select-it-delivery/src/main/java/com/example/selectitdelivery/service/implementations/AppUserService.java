package com.example.selectitdelivery.service.implementations;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.service.exceptions.AppUserAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.interfaces.IAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserService implements IAppUserService {
    private final AppUserRepository appUserRepository;

    private static AppUser convertEntityToModel(AppUserEntity appUser) {
        return new AppUser(
                appUser.getUserId(),
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.getRole()
        );
    }

    private static AppUserEntity convertModelToEntity(AppUser appUser) {
        return AppUserEntity.builder()
                .userId(appUser.getUserId())
                .email(appUser.getEmail())
                .password(appUser.getPassword())
                .role(appUser.getRole())
                .build();
    }

    @Override
    public AppUser record(AppUser appUser) throws AppUserAlreadyExistsException {
        if(appUserRepository.findById(appUser.getUserId()).isPresent()) {
            throw new AppUserAlreadyExistsException();
        }

        AppUserEntity recordAppUserEntity = appUserRepository.save(convertModelToEntity(appUser));

        return convertEntityToModel(recordAppUserEntity);
    }

    @Override
    public AppUser readById(long id) throws AppUserNotFoundException {
        if(appUserRepository.findById(id).isEmpty()) {
            throw new AppUserNotFoundException();
        }

        return convertEntityToModel(appUserRepository.findById(id).get());
    }

    @Override
    public Collection<AppUser> readAll() {
        return appUserRepository.findAll()
                .stream()
                .map(AppUserService::convertEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public AppUser modify(AppUser appUser) throws AppUserNotFoundException {
        AppUserEntity appUserEntity = convertModelToEntity(appUser);
        if(appUserRepository.findById(appUserEntity.getUserId()).isEmpty()) {
            throw new AppUserNotFoundException();
        }

        return convertEntityToModel(appUserRepository.save(appUserEntity));
    }

    @Override
    public void delete(AppUser appUser) throws AppUserNotFoundException {
        if(appUserRepository.findById(appUser.getUserId()).isEmpty()) {
            throw new AppUserNotFoundException();
        }

        appUserRepository.delete(convertModelToEntity(appUser));
    }
}

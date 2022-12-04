package com.example.selectitdelivery.service.interfaces;

import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.service.exceptions.AppUserAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;

import java.util.Collection;

public interface IAppUserService {
    AppUser record(AppUser appUser) throws AppUserAlreadyExistsException;
    AppUser readById(long id) throws AppUserNotFoundException;
    public AppUser readByEmail(String email) throws AppUserNotFoundException;
    Collection<AppUser> readAll();
    AppUser modify(AppUser appUser) throws AppUserNotFoundException;
    void delete(AppUser appUser) throws AppUserNotFoundException;

    boolean existsByEmail(String email);
}

package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDto appUserToAppUserDto(AppUser appUser);

    AppUser appUserDtoToAppUser(AppUserDto appUserDto);
}

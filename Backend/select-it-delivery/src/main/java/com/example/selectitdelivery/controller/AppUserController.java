package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.controller.dto.AppUserDto;
import com.example.selectitdelivery.controller.dto.AppUserMapper;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.service.exceptions.AppUserAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/appUser")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class AppUserController {
    private final AppUserService appUserService;

    private final AppUserMapper appUserMapper;

    @GetMapping(value = "/all")
    public Collection<AppUserDto> getUsers() {
        return appUserService.readAll()
                .stream()
                .map(appUserMapper::appUserToAppUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public AppUserDto readById(@PathVariable long id) {
        try {
            return appUserMapper.appUserToAppUserDto(appUserService.readById(id));
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public AppUserDto create(@Valid @RequestBody AppUserDto createRequestAppUserDto) {
        AppUser createRequestAppUser = appUserMapper.appUserDtoToAppUser(createRequestAppUserDto);
        try {
            AppUser createdAppUser = appUserService.record(createRequestAppUser);
            return appUserMapper.appUserToAppUserDto(createdAppUser);
        } catch (AppUserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/modify")
    public AppUserDto modify(@Valid @RequestBody AppUserDto updateRequestAppUserDto) {
        AppUser updateRequestAppUser = appUserMapper.appUserDtoToAppUser(updateRequestAppUserDto);

        try {
            AppUser updatedAppUser = appUserService.modify(updateRequestAppUser);
            return appUserMapper.appUserToAppUserDto(updatedAppUser);
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public void delete(@RequestParam long id) {
        try {
            appUserService.delete(appUserService.readById(id));
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

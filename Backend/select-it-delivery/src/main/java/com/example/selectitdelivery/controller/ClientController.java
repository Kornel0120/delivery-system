package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Response.UserModifyResponse;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.controller.dto.ClientDto;
import com.example.selectitdelivery.controller.dto.ClientMapper;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;
import com.example.selectitdelivery.service.implementations.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/client")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<ClientDto> getEmployees() {
        return clientService.readAll()
                .stream()
                .map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ClientDto readById(@PathVariable long id) {
        try {
            return clientMapper.clientToClientDto(clientService.readById(id));
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> modify(@Valid @RequestBody ClientDto updateRequestClientDto) {
        Client updateRequestClient = clientMapper.clientDtoToClient(updateRequestClientDto);
        updateRequestClient.getUser().setPassword(passwordEncoder.encode(updateRequestClient.getUser().getPassword()));

        try {
            Client updatedClient = clientService.modify(updateRequestClient);
            return ResponseEntity.ok(new UserModifyResponse("Client's account updated successfully.",
                    updatedClient.getClientId(),
                    updatedClient.getUser(),
                    updatedClient.getFirstName(),
                    updatedClient.getLastName(),
                    updatedClient.getPhone()));
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@RequestParam long id) {
        try {
            clientService.delete(clientService.readById(id));
        } catch (ClientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("Client deleted successfully!"));
    }
}

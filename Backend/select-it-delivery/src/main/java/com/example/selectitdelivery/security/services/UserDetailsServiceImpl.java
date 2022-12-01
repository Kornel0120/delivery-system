package com.example.selectitdelivery.security.services;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AppUserRepository appUserRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUserEntity user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + email)
                );

        return UserDetailsImpl.build(user);
    }
}

package com.example.organogram.web;

import com.example.organogram.model.UserRepository;
import com.example.organogram.security.SecurityUtils;
import com.example.organogram.services.errors.AccountNotFoundException;
import com.example.organogram.web.vm.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountResource {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserDTO getAccount() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByUsername)
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountNotFoundException("User could not be found"));
    }

}

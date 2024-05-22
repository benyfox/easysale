package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.request.SignupRequest;
import com.example.onlinemarketplace.dto.request.UserRequest;
import com.example.onlinemarketplace.dto.response.UserDto;
import com.example.onlinemarketplace.model.User;
import com.example.onlinemarketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final AuthController authController;
    private final UserService userService;

    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDto>> getPageable(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<UserDto> userDto = userService.getAllUsersPageable(paging);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto getUserByUsername(@RequestParam String username) {
        Objects.requireNonNull(username, "parameter can not be null");
        return userService.findByUsername(username);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SignupRequest signupRequest) {
        return authController.registerUser(signupRequest);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean delete(@Valid @RequestBody User user) {
        return userService.delete(user.getUsername());
    }

    @PatchMapping
    public User update(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserRequest user) {
        return userService.updateUser(userDetails.getUsername(), user);
    }
}
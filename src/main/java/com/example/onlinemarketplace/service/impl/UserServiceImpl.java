package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.UserRequest;
import com.example.onlinemarketplace.dto.response.UserDto;
import com.example.onlinemarketplace.exception.UserNotFoundException;
import com.example.onlinemarketplace.model.User;
import com.example.onlinemarketplace.repository.UserRepository;
import com.example.onlinemarketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return (userRepository
                .findAll())
                .stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> getAllUsersPageable(Pageable paging) {
        Page<User> page = userRepository.findAll(paging);
        return new PageImpl<UserDto>(page.getContent().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = _findByUsername(username);

        return convertToUserDto(user);
    }

    @Override
    public User _findByUsername(String username) {
        Objects.requireNonNull(username, "username cannot be null");
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Boolean existsByUsername(String username) {
        Objects.requireNonNull(username, "username cannot be null");
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        Objects.requireNonNull(email, "email cannot be null");
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean delete(String username) {
        Objects.requireNonNull(username, "username cannot be null");
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Long id = user.getId();
        userRepository.delete(user);
        return !userRepository.existsById(id);
    }

    @Override
    public User updateUser(String username, UserRequest user) {
        User userToUpdate = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        User updatedUser = partialUpdateMapper(userToUpdate, user);
        return userRepository.save(updatedUser);
    }

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User partialUpdateMapper(User userToUpdate, UserRequest user) {
        for (Field field : user.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(user);
                if (value != null) {
                    Field userField = userToUpdate.getClass().getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(userToUpdate, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("Error updating user: {}", e.getMessage());
            }
        }
        return userToUpdate;
    }
}

package ru.zhadaev.taskmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dao.repository.UserRepository;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.exception.AlreadyExistsException;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;
import ru.zhadaev.taskmanagementsystem.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto save(CreateUpdateUserDto createUpdateUserDto) {
        if (existsByEmail(createUpdateUserDto.getEmail())) {
            throw new AlreadyExistsException(String.format("A user with the login '%s' already exists", createUpdateUserDto.getEmail()));
        }
        User user = userMapper.toEntity(createUpdateUserDto);
        user.setPassword(passwordEncoder.encode(createUpdateUserDto.getPassword()));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserDto findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User not found by id = %s", id)));
        return userMapper.toDto(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User not found by email = %s", email)));
        return userMapper.toDto(user);
    }

    public List<UserDto> findAll(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).toList();
        return userMapper.toDtos(users);
    }

    public UserDto update(CreateUpdateUserDto userDto, UUID id) {
        User user = userMapper.toEntity(this.findById(id));
        userMapper.update(userDto, user);
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteById(UUID id) {
        if (existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("User delete error. User not found by id = %s", id));
        }
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    private boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    private boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new AuthorizationServiceException("User not authenticated");
        }
    }

    public String getAuthUserEmail() {
        UserDetails userDetails = getCurrentUserDetails();
        return userDetails.getUsername();
    }

    public User getAuthUser() {
        return userRepository.findByEmail(getAuthUserEmail()).orElseThrow(() -> new NotFoundException(String.format("User not found by email = %s", getAuthUserEmail())));
    }
}

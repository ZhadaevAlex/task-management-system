package ru.zhadaev.taskmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dao.repository.UserRepository;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException(String.format("User not found by email = %s", username)));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}

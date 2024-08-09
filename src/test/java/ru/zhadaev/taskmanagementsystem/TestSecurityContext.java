package ru.zhadaev.taskmanagementsystem;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class TestSecurityContext {
    public static UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserDetails) context.getAuthentication().getPrincipal();
    }
}

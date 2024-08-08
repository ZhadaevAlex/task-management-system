package ru.zhadaev.taskmanagementsystem.integration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.zhadaev.taskmanagementsystem.TaskManagementSystemApplication;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TaskManagementSystemApplication.class)
public class UserServiceIT {
    @Test
    void findById() {

    }
}

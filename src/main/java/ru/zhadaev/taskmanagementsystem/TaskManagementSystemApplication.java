package ru.zhadaev.taskmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

    //todo: убрать лишние jwt зависимости
    //todo: сделать инструкцию по развертыванию
    //todo: Проблема с исключениями при аутентификации

}

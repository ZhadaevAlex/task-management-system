package ru.zhadaev.taskmanagementsystem.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends
        CrudRepository<User, UUID>,
        PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}

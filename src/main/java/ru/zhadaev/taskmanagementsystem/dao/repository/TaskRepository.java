package ru.zhadaev.taskmanagementsystem.dao.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zhadaev.taskmanagementsystem.dao.entity.Task;

import java.util.UUID;

@Repository
public interface TaskRepository extends
        CrudRepository<Task, UUID>,
        PagingAndSortingRepository<Task, UUID> {

    @Modifying
    @Query("delete Task t where t.author.email = :authUserEmail")
    int deleteAllByAuthor(@Param("authUserEmail")String authUserEmail);
}

package ru.zhadaev.taskmanagementsystem.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.zhadaev.taskmanagementsystem.dao.entity.Comment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends
        CrudRepository<Comment, UUID>,
        PagingAndSortingRepository<Comment, UUID> {

    @Query("from Comment where task.id = :taskId and author.email = :email")
    List<Comment> findAllByAuthorByTaskId(Pageable pageable, String email, UUID taskId);

    List<Comment> findAllByTaskId(Pageable pageable, UUID taskId);
}

package ru.zhadaev.taskmanagementsystem.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.zhadaev.taskmanagementsystem.dao.entity.Comment;

import java.util.UUID;

@Repository
public interface CommentRepository extends
        CrudRepository<Comment, UUID>,
        PagingAndSortingRepository<Comment, UUID> {
    @Query("delete from Comment c where c.author.id = :authUserEmail")
    void deleteAllByAuthor(String authUserEmail);
}

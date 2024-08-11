package ru.zhadaev.taskmanagementsystem.dao.repository;

import org.springframework.data.domain.Pageable;
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

    List<Comment> findAllByTaskId(UUID taskId, Pageable pageable);
}

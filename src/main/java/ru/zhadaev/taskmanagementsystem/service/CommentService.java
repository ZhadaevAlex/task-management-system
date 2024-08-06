package ru.zhadaev.taskmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.taskmanagementsystem.dao.entity.Comment;
import ru.zhadaev.taskmanagementsystem.dao.entity.Task;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dao.repository.CommentRepository;
import ru.zhadaev.taskmanagementsystem.dto.CommentDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateCommentDto;
import ru.zhadaev.taskmanagementsystem.exception.AccessPermissionException;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;
import ru.zhadaev.taskmanagementsystem.mapper.CommentMapper;
import ru.zhadaev.taskmanagementsystem.mapper.TaskMapper;
import ru.zhadaev.taskmanagementsystem.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public CommentDto addToTask(CreateUpdateCommentDto createUpdateCommentDto, UUID taskId) {
        String authUserEmail = userService.getAuthUserEmail();
        User aurtUser = userMapper.toEntity(userService.findByEmail(authUserEmail));
        Task task = taskMapper.toEntity(taskService.findById(taskId));
        Comment comment = commentMapper.toEntity(createUpdateCommentDto);
        comment.setAuthor(aurtUser);
        comment.setTime(LocalDateTime.now());
        task.getComments().add(comment); //todo проверить автоматическое сохранение таски
        comment.setTask(task);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto findById(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found by id = %s", id)));
        return commentMapper.toDto(comment);
    }

    public List<CommentDto> findAll(Pageable pageable) {
        List<Comment> comments = commentRepository.findAll(pageable).toList();
        return commentMapper.toDtos(comments);
    }

    public CommentDto updateByAuthor(CreateUpdateCommentDto createUpdateCommentDto, UUID id) {
        if (isAuthor(id)) {
            Comment comment = commentMapper.toEntity(findById(id));
            commentMapper.update(createUpdateCommentDto, comment);
            return commentMapper.toDto(commentRepository.save(comment));
        } else {
            throw new AccessPermissionException("Only the author can edit the comment");
        }
    }

    public void deleteByIdByAuthor(UUID id) {
        if (!existsById(id)) {
            throw new NotFoundException(String.format("Comment delete error. Comment not found by id = %s", id));
        }

        if (isAuthor(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new AccessPermissionException("Only the author can delete the comment");
        }
    }

    public void deleteAllByAuthor() {
        String authUserEmail = userService.getAuthUserEmail();
        commentRepository.deleteAllByAuthor(authUserEmail);
    }

    private boolean existsById(UUID id) {
        return commentRepository.existsById(id);
    }

    public boolean isAuthor(UUID id) {
        String authUserEmail = userService.getAuthUserEmail();
        Comment comment = commentMapper.toEntity(findById(id));
        return comment.getAuthor().getEmail().equals(authUserEmail);
    }
}

package ru.zhadaev.taskmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.taskmanagementsystem.dao.entity.Task;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dao.repository.TaskRepository;
import ru.zhadaev.taskmanagementsystem.dto.AssignTaskPerformerDto;
import ru.zhadaev.taskmanagementsystem.dto.ChangeStatusTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.TaskDto;
import ru.zhadaev.taskmanagementsystem.exception.AccessPermissionException;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;
import ru.zhadaev.taskmanagementsystem.mapper.TaskMapper;
import ru.zhadaev.taskmanagementsystem.mapper.UserMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskDto save(CreateUpdateTaskDto createUpdateTaskDto) {
        String authUserEmail = userService.getAuthUserEmail();
        User userAuthor = userMapper.toEntity(userService.findByEmail(authUserEmail));
        User userPerformer = userMapper.toEntity(userService.findByEmail(createUpdateTaskDto.getPerformerEmail()));
        Task task = taskMapper.toEntity(createUpdateTaskDto);
        task.setAuthor(userAuthor);
        task.setPerformer(userPerformer);
        return taskMapper.toTaskResponseDto(taskRepository.save(task));
    }

    public TaskDto findById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Task not found by id = %s", id)));
        return taskMapper.toTaskResponseDto(task);
    }

    public List<TaskDto> findAll(Pageable pageable) {
        List<Task> tasks = taskRepository.findAll(pageable).toList();
        return taskMapper.toDtos(tasks);
    }

    public TaskDto updateByAuthor(CreateUpdateTaskDto createUpdateTaskDto, UUID id) {
        if (isAuthor(id)) {
            Task task = taskMapper.toEntity(findById(id));
            User userPerformer;
            if (createUpdateTaskDto.getPerformerEmail() != null) {
                userPerformer = userMapper.toEntity(userService.findByEmail(createUpdateTaskDto.getPerformerEmail()));
                task.setPerformer(userPerformer);
            }
            taskMapper.update(createUpdateTaskDto, task);
            return taskMapper.toTaskResponseDto(taskRepository.save(task));
        } else {
            throw new AccessPermissionException("Only the author can edit the task");
        }
    }

    public TaskDto changeStatusById(ChangeStatusTaskDto changeStatusTaskDto, UUID id) {
        if (isAuthor(id) || isPerformer(id)) {
            Task task = taskMapper.toEntity(findById(id));
            taskMapper.update(changeStatusTaskDto, task);
            return taskMapper.toTaskResponseDto(taskRepository.save(task));
        } else {
            throw new AccessPermissionException("Only the author or performer can edit the status");
        }
    }

    public TaskDto assignPerformerById(AssignTaskPerformerDto assignTaskPerformerDto, UUID id) {
        if (isAuthor(id)) {
            Task task = taskMapper.toEntity(findById(id));
            taskMapper.update(assignTaskPerformerDto, task);
            return taskMapper.toTaskResponseDto(taskRepository.save(task));
        } else {
            throw new AccessPermissionException("only the author can assign performer");
        }
    }

    public void deleteByIdByAuthor(UUID id) {
        if (!existsById(id)) {
            throw new NotFoundException(String.format("Task delete error. Task not found by id = %s", id));
        }

        if (isAuthor(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new AccessPermissionException("Only the author can delete the task");
        }
    }

    public void deleteAllByAuthor() {
        String authUserEmail = userService.getAuthUserEmail();
        taskRepository.deleteAllByAuthor(authUserEmail);
    }

    private boolean existsById(UUID id) {
        return taskRepository.existsById(id);
    }

    public boolean isAuthor(UUID taskId) {
        String authUserEmail = userService.getAuthUserEmail();
        Task task = taskMapper.toEntity(findById(taskId));
        return task.getAuthor().getEmail().equals(authUserEmail);
    }

    public boolean isPerformer(UUID taskId) {
        String authUserEmail = userService.getAuthUserEmail();
        Task task = taskMapper.toEntity(findById(taskId));
        return task.getPerformer().getEmail().equals(authUserEmail);
    }
}

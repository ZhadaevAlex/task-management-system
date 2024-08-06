package ru.zhadaev.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.taskmanagementsystem.dao.entity.Task;
import ru.zhadaev.taskmanagementsystem.dto.AssignTaskPerformerDto;
import ru.zhadaev.taskmanagementsystem.dto.ChangeStatusTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    Task toEntity(CreateUpdateTaskDto createUpdateTaskDto);
    Task toEntity(TaskDto taskResponse);
    TaskDto toTaskResponseDto(Task task);
    List<TaskDto> toDtos(List<Task> tasks);
    void update(CreateUpdateTaskDto getTaskDto, @MappingTarget Task task);
    void update(TaskDto getTaskDto, @MappingTarget Task task);
    void update(ChangeStatusTaskDto changeStatusTaskDto, @MappingTarget Task task);
    void update(AssignTaskPerformerDto assignTaskPerformerDto, @MappingTarget Task task);
}

package ru.zhadaev.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.taskmanagementsystem.dao.entity.Comment;
import ru.zhadaev.taskmanagementsystem.dto.CommentDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateCommentDto;
import ru.zhadaev.taskmanagementsystem.dto.UpdateCommentDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    Comment toEntity(CreateCommentDto createCommentDto);
    Comment toEntity(CommentDto commentDto);
    CommentDto toDto(Comment comment);
    List<CommentDto> toDtos(List<Comment> comments);
    void update(CreateCommentDto createCommentDto, @MappingTarget Comment comment);
    void update(UpdateCommentDto updateCommentDto, @MappingTarget Comment comment);
}

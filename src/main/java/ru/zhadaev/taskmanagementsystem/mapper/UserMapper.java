package ru.zhadaev.taskmanagementsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto userDto);
    User toEntity(CreateUpdateUserDto createUpdateUserDto);
    UserDto toDto(User user);
    List<UserDto> toDtos(List<User> users);
    void update(CreateUpdateUserDto createUpdateUserDto, @MappingTarget User user);
}

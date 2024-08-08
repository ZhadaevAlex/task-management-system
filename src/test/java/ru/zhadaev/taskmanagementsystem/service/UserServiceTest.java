package ru.zhadaev.taskmanagementsystem.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
import ru.zhadaev.taskmanagementsystem.dao.repository.UserRepository;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.exception.AlreadyExistsException;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;
import ru.zhadaev.taskmanagementsystem.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    public static final UUID ID = UUID.randomUUID();
    public static final User USER = new User(
            ID,
            "example1@mail.ru",
            "Password1#"
    );

    public static final UserDto USER_DTO = new UserDto(
            ID,
            "example1@mail.ru",
            "Password1#"
    );

    @Nested
    @DisplayName("Tests for saving users")
    class SaveTest {
        @Test
        void save_shouldReturnValidUserDto() {
            UUID id = UUID.randomUUID();
            String email = "example1@mail.ru";
            String password = "Password1#";
            CreateUpdateUserDto createUserDto = new CreateUpdateUserDto(email, password);

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            User saved = new User(id, email, password);
            UserDto userDto = new UserDto(id, email, password);

            doReturn(user).when(userMapper).toEntity(createUserDto);
            doReturn(saved).when(userRepository).save(user);
            doReturn(userDto).when(userMapper).toDto(saved);

            UserDto actual = userService.save(createUserDto);
            verify(userRepository, times(1)).save(user);
            verify(userMapper, times(1)).toEntity(createUserDto);
            verify(userMapper, times(1)).toDto(saved);
            assertEquals(actual, userDto);
        }

        @Test
        void save_shouldThrowAlreadyExistsException_whenUserIsExists() {
            String email = "example1@mail.ru";
            String password = "Password1#";
            CreateUpdateUserDto createUserDto = new CreateUpdateUserDto(email, password);

            doReturn(true).when(userRepository).existsByEmail(email);
            assertThrows(AlreadyExistsException.class, () -> userService.save(createUserDto));
            verify(userRepository, times(1)).existsByEmail(email);
        }
    }

    @Nested
    @DisplayName("Tests for finding an user")
    class FindTests {
        @Test
        void findById_shouldReturnValidUserDto_whenUserUserIsExistsById() {
            Mockito.doReturn(Optional.of(USER)).when(userRepository).findById(ID);
            Mockito.doReturn(USER_DTO).when(userMapper).toDto(USER);
            UserDto actual = userService.findById(ID);
            verify(userRepository, times(1)).findById(ID);
            verify(userMapper, times(1)).toDto(USER);
            assertEquals(actual, USER_DTO);
        }

        @Test
        void findById_shouldThrowNotFoundException_whenUserIsNotExistsById() {
            doReturn(Optional.empty()).when(userRepository).findById(ID);
            assertThrows(NotFoundException.class, () -> userService.findById(ID));
            verify(userRepository, times(1)).findById(ID);
        }

        @Test
        void findByEmail_shouldReturnValidUserDto_whenUserIsExistByEmail() {
            String email = "example1@mail.ru";
            Mockito.doReturn(Optional.of(USER)).when(userRepository).findByEmail(email);
            Mockito.doReturn(USER_DTO).when(userMapper).toDto(USER);
            UserDto actual = userService.findByEmail(email);
            verify(userRepository, times(1)).findByEmail(email);
            verify(userMapper, times(1)).toDto(USER);
            assertEquals(actual, USER_DTO);
        }


        @Test
        void findById_shouldThrowNotFoundException_whenUserNotExistsByEmail() {
            String email = "example1@mail.ru";
            doReturn(Optional.empty()).when(userRepository).findByEmail(email);
            assertThrows(NotFoundException.class, () -> userService.findByEmail(email));
            verify(userRepository, times(1)).findByEmail(email);
        }

        @Test
        void findById_shouldReturnAllUsers() {
            UserDto userDto1 = new UserDto(UUID.randomUUID(), "example1@mail.ru", "Password1#");
            UserDto userDto2 = new UserDto(UUID.randomUUID(), "example2@mail.ru", "Password2#");
            UserDto userDto3 = new UserDto(UUID.randomUUID(), "example3@mail.ru", "Password3#");
            User user1 = new User(UUID.randomUUID(), "example1@mail.ru", "Password1#");
            User user2 = new User(UUID.randomUUID(), "example2@mail.ru", "Password2#");
            User user3 = new User(UUID.randomUUID(), "example3@mail.ru", "Password3#");
            List<UserDto> userDtos = List.of(userDto1, userDto2, userDto3);
            List<User> users = List.of(user1, user2, user3);
            Page<User> page = new PageImpl<>(users);
            Pageable pageable = PageRequest.of(1, 2);

            doReturn(page).when(userRepository).findAll(pageable);
            doReturn(userDtos).when(userMapper).toDtos(users);
            List<UserDto> actual = userService.findAll(pageable);
            verify(userRepository, times(1)).findAll(pageable);
            assertEquals(actual, userDtos);
        }
    }

    @Nested
    @DisplayName("Tests for update users")
    class UpdateTest {
        @Test
        void update_shouldReturnValidUserDto_whenUserIsExistsById() {
            String email = "example1@mail.ru";
            String password1 = "Password1#";
            String password2 = "Password2#";

            CreateUpdateUserDto userUpdateDto = new CreateUpdateUserDto();
            userUpdateDto.setPassword(password2);

            User userFound = new User(ID, email, password1);
            UserDto userFoundDto = new UserDto(ID, email, password1);

            User userUpdated = new User(ID, email, password2);
            UserDto userUpdatedDto = new UserDto(ID, email, password2);

            doReturn(Optional.of(userFound)).when(userRepository).findById(ID);
            doReturn(userFoundDto).when(userMapper).toDto(userFound);
            doReturn(userFound).when(userMapper).toEntity(userFoundDto);
            doNothing().when(userMapper).update(userUpdateDto, userFound);
            doReturn(password2).when(passwordEncoder).encode(userUpdateDto.getPassword());
            doReturn(userUpdated).when(userRepository).save(userUpdated);
            doReturn(userUpdatedDto).when(userMapper).toDto(userUpdated);

            UserDto actual = userService.update(userUpdateDto, ID);
            verify(userRepository, times(1)).findById(ID);
            verify(userMapper, times(2)).toDto(userFound);
            verify(userMapper, times(1)).toEntity(userFoundDto);
            verify(userMapper, times(1)).update(userUpdateDto, userFound);
            verify(passwordEncoder, times(1)).encode(userUpdateDto.getPassword());
            verify(userRepository, times(1)).save(userUpdated);
            assertEquals(actual, userUpdatedDto);
        }

        @Test
        void save_shouldThrowNotFoundException_whenUserIsNotExists() {
            String password2 = "Password2#";
            CreateUpdateUserDto userUpdateDto = new CreateUpdateUserDto();
            userUpdateDto.setPassword(password2);

            doReturn(Optional.empty()).when(userRepository).findById(ID);
            assertThrows(NotFoundException.class, () -> userService.update(userUpdateDto, ID));
            verify(userRepository, times(1)).findById(ID);
        }
    }

    @Nested
    @DisplayName("Tests for delete users")
    class DeleteTest {
        @Test
        void deleteById_shouldExecutedOneTime_whenEntityIsExistsById() {
            doReturn(true).when(userRepository).existsById(ID);
            doNothing().when(userRepository).deleteById(ID);
            userService.deleteById(ID);
            verify(userRepository, times(1)).existsById(ID);
            verify(userRepository, times(1)).deleteById(ID);
        }

        @Test
        void deleteById_shouldThrowNotFoundException_whenEntityIsNotExistsById() {
            doReturn(false).when(userRepository).existsById(ID);
            assertThrows(NotFoundException.class, () -> userService.deleteById(ID));
            verify(userRepository, times(1)).existsById(ID);
            verify(userRepository, times(0)).deleteById(ID);
        }

        @Test
        void deleteByEmail_shouldExecutedOneTime_whenEntityIsExistsByEmail() {
            String email = "example1@mail.ru";
            doReturn(true).when(userRepository).existsByEmail(email);
            doNothing().when(userRepository).deleteByEmail(email);
            userService.deleteByEmail(email);
            verify(userRepository, times(1)).existsByEmail(email);
            verify(userRepository, times(1)).deleteByEmail(email);
        }

        @Test
        void deleteByEmail_shouldThrowNotFoundException_whenEntityIsNotExistsByEmail() {
            String email = "example1@mail.ru";
            doReturn(false).when(userRepository).existsByEmail(email);
            assertThrows(NotFoundException.class, () -> userService.deleteByEmail(email));
            verify(userRepository, times(1)).existsByEmail(email);
            verify(userRepository, times(0)).deleteByEmail(email);
        }

        @Test
        void deleteAll_shouldExecutedOneTime() {
            doNothing().when(userRepository).deleteAll();

            userService.deleteAll();

            verify(userRepository, times(1)).deleteAll();
        }
    }
}
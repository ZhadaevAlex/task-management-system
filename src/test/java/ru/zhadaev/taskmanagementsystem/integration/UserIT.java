package ru.zhadaev.taskmanagementsystem.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.zhadaev.taskmanagementsystem.TaskManagementSystemApplication;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.security.JwtTokenUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = TaskManagementSystemApplication.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql({
        "classpath:schemaIntegrationTest.sql",
        "classpath:dataIntegrationTest.sql"
})
@Sql(
        scripts = {"classpath:schemaDropIntegrationTest.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class UserIT {

    private final MockMvc mockMvc;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private String jwtToken;
    @Autowired

    private final PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("Tests for creation an user")
    class CreateTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void create_shouldReturnValidUserDto_whenEmailIsValid() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String email = "example7@mail.ru";
            String password = "Password7#";
            CreateUpdateUserDto saved = new CreateUpdateUserDto();
            saved.setEmail(email);
            saved.setPassword(password);

            UserDto expected = new UserDto();
            expected.setEmail(email);
            expected.setPassword(password);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);

            MvcResult result = mockMvc.perform(post("/api/users")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isCreated())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            UserDto actual = objectMapper.readValue(responseBody, UserDto.class);
            expected.setId(actual.getId());
            assertTrue(passwordEncoder.matches(saved.getPassword(), actual.getPassword()));
            expected.setPassword(actual.getPassword());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void create_shouldReturnError_whenEmailIsNotValid() throws Exception {
            String expectedMsg = "The email address must be in the format user@example.com";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String email = "example7";
            String password = "Password7#";
            UserDto saved = new UserDto();
            saved.setEmail(email);
            saved.setPassword(password);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);

            mockMvc.perform(post("/api/users")
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$..message").value(expectedMsg))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Test for user's find")
    class FindTest {
        @Test
        @WithUserDetails("example1@mail.ru")
        void findAll_shouldReturnFirstPageOfListOfTwoValidUserDto() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String id1 = "5405c989-9dbf-4e35-a923-8b1d4e4ad7bc";
            String id2 = "fe388668-d648-49f5-9764-5c6c8190806a";
            String email1= "example3@mail.ru";
            String email2 = "example4@mail.ru";
            String password1 = "$2a$12$S6/o8jkidnqFvwx16KXHVOyAVrXwDiusCGsBECYhFUrTCsJAPsRFu";
            String password2 = "$2a$12$2LXjXVV3zBwrr5HBlp.KzOPLE77wwM4wtGA6rEdge3I.tsmcfR4XO";
            List<UserDto> expected = List.of(
                    new UserDto(UUID.fromString(id1), email1, password1),
                    new UserDto(UUID.fromString(id2), email2, password2));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("page", "1");
            params.add("size", "2");
            params.add("sort", "email,asc");

            MvcResult mvcResult = mockMvc.perform(get("/api/users")
                            .params(params)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = mvcResult.getResponse().getContentAsString();
            List<UserDto> actual = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void findById_shouldReturnValidUserDto_whenUserUserIsExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id1 = UUID.fromString("fedd6a4f-f0e8-4a50-82e7-8b69bffc6507");
            String email = "example1@mail.ru";
            String password = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            UserDto expected = new UserDto(id1, email, password);

            MvcResult mvcResult = mockMvc.perform(get("/api/users/{id}", id1))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = mvcResult.getResponse().getContentAsString();
            UserDto actual = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            assertEquals(expected, actual);
        }

        @WithUserDetails("example1@mail.ru")
        @Test
        void findById_shouldReturnNotFoundError_whenUserIsNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "8e2e1511-8105-441f-97e8-5bce88c0267b";
            String expectedMsg = "User not found by id = " + badId;

            mockMvc.perform(get("/api/users/{id}", badId)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void findByEmail_shouldReturnValidUserDto_whenUserUserIsExistsByEmail() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("fedd6a4f-f0e8-4a50-82e7-8b69bffc6507");
            String email = "example1@mail.ru";
            String password = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            UserDto expected = new UserDto(id, email, password);

            MvcResult mvcResult = mockMvc.perform(get("/api/users/email/{email}", email))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = mvcResult.getResponse().getContentAsString();
            UserDto actual = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            assertEquals(expected, actual);
        }

        @WithUserDetails("example1@mail.ru")
        @Test
        void findByEmail_shouldReturnNotFoundError_whenUserIsNotExistsByEmail() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badEmail = "someemail";
            String expectedMsg = "User not found by email = " + badEmail;

            mockMvc.perform(get("/api/users/email/{badEmail}", badEmail)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }
    }

    @Nested
    @DisplayName("Tests for update an user")
    class UpdateTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void update_shouldReturnValidUserDto_whenUserIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("fedd6a4f-f0e8-4a50-82e7-8b69bffc6507");
            String password = "Password7#";
            CreateUpdateUserDto updated = new CreateUpdateUserDto();
            updated.setPassword(password);

            UserDto expected = new UserDto();
            expected.setId(id);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            MvcResult result = mockMvc.perform(patch("/api/users/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isAccepted())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            UserDto actual = objectMapper.readValue(responseBody, UserDto.class);
            assertTrue(passwordEncoder.matches(updated.getPassword(), actual.getPassword()));
            expected.setPassword(actual.getPassword());
            expected.setEmail(actual.getEmail());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void update_shouldReturnError_whenUserIsNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "8e2e1511-8105-441f-97e8-5bce88c0267b";
            String expectedMsg = "User not found by id = " + badId;

            String password = "Password7#";
            CreateUpdateUserDto updated = new CreateUpdateUserDto();
            updated.setPassword(password);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            mockMvc.perform(patch("/api/users/{id}", badId)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }
    }

    @Nested
    @DisplayName("Tests for delete an user")
    class DeleteTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteById_shouldReturnOk_whenUserIsExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("fedd6a4f-f0e8-4a50-82e7-8b69bffc6507");
            mockMvc.perform(delete("/api/users/{id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteById_shouldReturnNotFoundError_whenUserNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6506";
            String expectedMsg = "User delete error. User not found by id = " + badId;
            mockMvc.perform(delete("/api/users/{id}", badId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteByEmail_shouldReturnOk_whenUserIsExistsByEmail() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String email = "example1@mail.ru";
            mockMvc.perform(delete("/api/users/email/{id}", email))
                    .andExpect(status().isOk());
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteByEmail_shouldReturnNotFoundError_whenUserNotExistsByEmail() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badEmail = "example8@mail.ru";
            String expectedMsg = "User delete error. User not found by email = " + badEmail;
            mockMvc.perform(delete("/api/users/email/{id}", badEmail))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteAll_shouldReturnOk() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            mockMvc.perform(delete("/api/users"))
                    .andExpect(status().isOk());
        }
    }
}

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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.zhadaev.taskmanagementsystem.TaskManagementSystemApplication;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;
import ru.zhadaev.taskmanagementsystem.dto.*;
import ru.zhadaev.taskmanagementsystem.security.JwtTokenUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class CommentIT {

    private final MockMvc mockMvc;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private String jwtToken;

    @Nested
    @DisplayName("Tests for creation an comment")
    class CreateTest {
        @Test
        @WithUserDetails("example1@mail.ru")
        void create_shouldReturnValidCommentDto_whenDataIsValid() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String commentContent = "Some content";
            String taskId = "3412cd10-9ec0-41f4-802f-e6440792fed2";
            CreateCommentDto saved = new CreateCommentDto(commentContent, taskId);

            CommentDto expected = new CommentDto();
            expected.setContent(commentContent);
            expected.setTaskId(UUID.fromString(taskId));
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);
            MvcResult result = mockMvc.perform(post("/api/comments")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isCreated())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            CommentDto actual = objectMapper.readValue(responseBody, CommentDto.class);
            expected.setId(actual.getId());
            expected.setAuthor(actual.getAuthor());
            expected.setTime(actual.getTime());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void save_shouldReturnError_whenSomeDataIsNotValid() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String expectedMsg = "It must be in the UUID format";

            String commentContent = "Some content";
            String taskId = "3412cd10-9ec0-41f4-802f-e6440792fed";
            CreateCommentDto saved = new CreateCommentDto(commentContent, taskId);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);

            mockMvc.perform(post("/api/comments")
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$..message").value(expectedMsg))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Test for task's find")
    class FindTest {
        @Test
        @WithUserDetails("example1@mail.ru")
        void findAllByTaskId_shouldReturnListValidTaskDto() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id1 = UUID.fromString("216a035d-3c5f-4c74-a860-6d1cb6d27a88");
            UUID id2 = UUID.fromString("433c2bf7-41c3-462d-8d71-e01ee1849bcc");
            Timestamp time1 = Timestamp.valueOf("2024-01-01 00:01:01");
            Timestamp time2 = Timestamp.valueOf("2024-01-02 00:01:01");
            String userId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
            String userId2 = "82dc22a0-d332-4241-959a-b20f8590155f";
            String userEmail1 = "example1@mail.ru";
            String userEmail2 = "example2@mail.ru";
            String userPassword1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            String userPassword2 = "$2a$12$URB3m7C1..eOQRmSPOhDVu6aXKx4VqD1ZTtjRTr8nIjW88uYJnP7S";
            UserDto user1 = new UserDto(UUID.fromString(userId1), userEmail1, userPassword1);
            UserDto user2 = new UserDto(UUID.fromString(userId2), userEmail2, userPassword2);
            String content1 = "Content1";
            String content2 = "Content2";
            String taskId = "3412cd10-9ec0-41f4-802f-e6440792fed2";
            List<CommentDto> expected = List.of(
                    new CommentDto(id1, time1, user1, content1, UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2")),
                    new CommentDto(id2, time2, user2, content2, UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2")));

            MvcResult mvcResult = mockMvc.perform(get("/api/comments/{taskId}", taskId)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = mvcResult.getResponse().getContentAsString();
            List<CommentDto> actual = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("Tests for update an comments")
    class UpdateTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void updateByAuthor_shouldReturnValidCommentDto_whenCommentIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("216a035d-3c5f-4c74-a860-6d1cb6d27a88");
            String commentContent = "New content";
            UpdateCommentDto updated = new UpdateCommentDto();
            updated.setContent(commentContent);

            CommentDto expected = new CommentDto();
            expected.setId(id);
            expected.setContent(commentContent);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            MvcResult result = mockMvc.perform(patch("/api/comments/author/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isAccepted())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            CommentDto actual = objectMapper.readValue(responseBody, CommentDto.class);
            expected.setAuthor(actual.getAuthor());
            expected.setTime(actual.getTime());
            expected.setTaskId(actual.getTaskId());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void updateByAuthor_shouldReturnError_whenCommentIsNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "8e2e1511-8105-441f-97e8-5bce88c0267b";
            String expectedMsg = "Comment update error. Comment not found by id = " + badId;

            String commentContent = "New content";
           UpdateCommentDto updated = new UpdateCommentDto();
            updated.setContent(commentContent);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            mockMvc.perform(patch("/api/comments/author/{id}", badId)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void updateByAuthor_shouldReturnError_whenUserIsNotAuthor() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String message = "Only the author can edit the comment";

            UUID id = UUID.fromString("1272adcc-ed29-4835-9a26-bf9be266ab64");
            String commentContent = "New content";
            UpdateCommentDto updated = new UpdateCommentDto();
            updated.setContent(commentContent);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            mockMvc.perform(patch("/api/comments/author/{id}", id)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$..message").value(message));
        }
    }

    @Nested
    @DisplayName("Tests for delete an comment")
    class DeleteTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteByIdByAuthor_shouldReturnOk_whenCommentIsExistsByIdAndUserIsAuthor() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("216a035d-3c5f-4c74-a860-6d1cb6d27a88");
            mockMvc.perform(delete("/api/comments/author/{id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteByIdByAuthor_shouldReturnNotFoundError_whenCommentNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6506";
            String expectedMsg = "Comment delete error. Comment not found by id = " + badId;

            mockMvc.perform(delete("/api/comments/author/{id}", badId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteByIdByAuthor_shouldReturnError_whenUserIsNotAuthor() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String expectedMsg = "Only the author can delete the comment";
            UUID id = UUID.fromString("1272adcc-ed29-4835-9a26-bf9be266ab64");

            mockMvc.perform(delete("/api/comments/author/{id}", id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }
    }
}

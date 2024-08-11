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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
public class TaskIT {

    private final MockMvc mockMvc;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private String jwtToken;

    @Nested
    @DisplayName("Tests for creation an task")
    class CreateTest {
        @Test
        @WithUserDetails("example1@mail.ru")
        void create_shouldReturnValidTaskDto_whenDataIsValid() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String header = "header4";
            String description = "description4";
            Status status = Status.OPENED;
            Priority priority = Priority.HIGH;
            String performerId = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
            String performerEmail = "example1@mail.ru";
            String performerPassword = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            UserDto performer = new UserDto(UUID.fromString(performerId), performerEmail, performerPassword);

            CreateUpdateTaskDto saved = new CreateUpdateTaskDto(header, description, status.toString(), priority.toString(), performerEmail);
            TaskDto expected = new TaskDto(null, header, description, status, priority, null, performer);
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);
            MvcResult result = mockMvc.perform(post("/api/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isCreated())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TaskDto actual = objectMapper.readValue(responseBody, TaskDto.class);
            expected.setId(actual.getId());
            expected.setAuthor(actual.getAuthor());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void create_shouldReturnError_whenSomeDataIsNotValid() throws Exception {
            String expectedMsg = "The header must contain at least one non-whitespace character";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String header = null;
            String description = "description4";
            Status status = Status.OPENED;
            Priority priority = Priority.HIGH;
            String performerEmail = "example1@mail.ru";

            CreateUpdateTaskDto saved = new CreateUpdateTaskDto(header, description, status.toString(), priority.toString(), performerEmail);
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(saved);

            mockMvc.perform(post("/api/tasks")
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
        void findAll_shouldReturnListValidTaskDto() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String userId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
            String userId2 = "82dc22a0-d332-4241-959a-b20f8590155f";
            String userId3 = "5405c989-9dbf-4e35-a923-8b1d4e4ad7bc";
            String userEmail1 = "example1@mail.ru";
            String userEmail2 = "example2@mail.ru";
            String userEmail3 = "example3@mail.ru";
            String userPassword1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            String userPassword2 = "$2a$12$URB3m7C1..eOQRmSPOhDVu6aXKx4VqD1ZTtjRTr8nIjW88uYJnP7S";
            String userPassword3 = "$2a$12$S6/o8jkidnqFvwx16KXHVOyAVrXwDiusCGsBECYhFUrTCsJAPsRFu";
            UserDto user1 = new UserDto(UUID.fromString(userId1), userEmail1, userPassword1);
            UserDto user2 = new UserDto(UUID.fromString(userId2), userEmail2, userPassword2);
            UserDto user3 = new UserDto(UUID.fromString(userId3), userEmail3, userPassword3);

            String id1 = "3412cd10-9ec0-41f4-802f-e6440792fed2";
            String id2 = "28ce2670-453a-46d5-b9a0-786fd4a52eab";
            String id3 = "380f1c97-d71f-4a04-b025-0ba103b4f6b0";
            String header1 = "header1";
            String header2 = "header2";
            String header3 = "header3";
            String description1 = "description1";
            String description2 = "description2";
            String description3 = "description3";
            Status status1 = Status.OPENED;
            Status status2 = Status.IN_PROGRESS;
            Status status3 = Status.COMPLETED;
            Priority priority1 = Priority.HIGH;
            Priority priority2 = Priority.MEDIUM;
            Priority priority3 = Priority.LOW;

            TaskDto task1 = new TaskDto(UUID.fromString(id1), header1, description1, status1, priority1, user1, user2); //, List.of(commentDto1, commentDto2
            TaskDto task2 = new TaskDto(UUID.fromString(id2), header2, description2, status2, priority2, user1, user3); //, List.of(commentDto3, commentDto4, commentDto5)
            TaskDto task3 = new TaskDto(UUID.fromString(id3), header3, description3, status3, priority3, user2, user3); //, new ArrayList<>()
            List<TaskDto> expected = List.of(task1, task2, task3);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("page", "0");
            params.add("size", "3");
            params.add("sort", "header,asc");

            MvcResult mvcResult = mockMvc.perform(get("/api/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .params(params))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = mvcResult.getResponse().getContentAsString();
            List<TaskDto> actual = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void findById_shouldReturnValidTaskDto_whenTaskIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String userId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
            String userId2 = "82dc22a0-d332-4241-959a-b20f8590155f";
            String userEmail1 = "example1@mail.ru";
            String userEmail2 = "example2@mail.ru";
            String userPassword1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
            String userPassword2 = "$2a$12$URB3m7C1..eOQRmSPOhDVu6aXKx4VqD1ZTtjRTr8nIjW88uYJnP7S";
            UserDto user1 = new UserDto(UUID.fromString(userId1), userEmail1, userPassword1);
            UserDto user2 = new UserDto(UUID.fromString(userId2), userEmail2, userPassword2);

            String id = "3412cd10-9ec0-41f4-802f-e6440792fed2";
            String header = "header1";
            String description = "description1";
            Status status = Status.OPENED;
            Priority priority = Priority.HIGH;
            TaskDto expected = new TaskDto(UUID.fromString(id), header, description, status, priority, user1, user2);

            MvcResult result = mockMvc.perform(get("/api/tasks/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andReturn();

            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = result.getResponse().getContentAsString();
            TaskDto actual = objectMapper.readValue(responseBody, TaskDto.class);
            assertEquals(expected, actual);
        }

        @WithUserDetails("example1@mail.ru")
        @Test
        void findById_shouldReturnNotFoundError_whenTaskIsNotExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "8e2e1511-8105-441f-97e8-5bce88c0267b";
            String expectedMsg = "Task not found by id = " + badId;

            mockMvc.perform(get("/api/tasks/{id}", badId)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }
    }

    @Nested
    @DisplayName("Tests for update an task")
    class UpdateTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void updateByAuthor_shouldReturnValidTaskDto_whenTaskIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2");
            String header = "New header";
            CreateUpdateTaskDto updated = new CreateUpdateTaskDto();
            updated.setHeader(header);

            TaskDto expected = new TaskDto();
            expected.setId(id);
            expected.setHeader(header);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            MvcResult result = mockMvc.perform(patch("/api/tasks/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isAccepted())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TaskDto actual = objectMapper.readValue(responseBody, TaskDto.class);
            expected.setAuthor(actual.getAuthor());
            expected.setDescription(actual.getDescription());
            expected.setPerformer(actual.getPerformer());
            expected.setPriority(actual.getPriority());
            expected.setStatus(actual.getStatus());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void updateByAuthor_shouldReturnError_whenUserIsNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "8e2e1511-8105-441f-97e8-5bce88c0267b";
            String expectedMsg = "Task not found by id = " + badId;

            String header = "New header";
            CreateUpdateTaskDto updated = new CreateUpdateTaskDto();
            updated.setHeader(header);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            mockMvc.perform(patch("/api/tasks/{id}", badId)
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

            String message = "Only the author can edit the task";
            UUID id = UUID.fromString("380f1c97-d71f-4a04-b025-0ba103b4f6b0");
            String header = "New header";
            CreateUpdateTaskDto updated = new CreateUpdateTaskDto();
            updated.setHeader(header);

            TaskDto expected = new TaskDto();
            expected.setId(id);
            expected.setHeader(header);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            mockMvc.perform(patch("/api/tasks/{id}", id)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$..message").value(message));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void changeStatusById_shouldReturnValidTaskDto_whenTaskIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2");
            String status = "COMPLETED";
            ChangeStatusTaskDto updated = new ChangeStatusTaskDto();
            updated.setStatus(status);

            TaskDto expected = new TaskDto();
            expected.setId(id);
            expected.setStatus(Status.COMPLETED);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            MvcResult result = mockMvc.perform(patch("/api/tasks/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isAccepted())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TaskDto actual = objectMapper.readValue(responseBody, TaskDto.class);
            expected.setAuthor(actual.getAuthor());
            expected.setDescription(actual.getDescription());
            expected.setPerformer(actual.getPerformer());
            expected.setPriority(actual.getPriority());
            expected.setHeader(actual.getHeader());
            assertEquals(expected, actual);
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void assignPerformerById_shouldReturnValidTaskDto_whenTaskIsExists() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2");
            String email = "example2@mail.ru";
            AssignTaskPerformerDto updated = new AssignTaskPerformerDto();
            updated.setPerformerEmail(email);

            TaskDto expected = new TaskDto();
            expected.setId(id);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(updated);

            MvcResult result = mockMvc.perform(patch("/api/tasks/{id}", id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(content))
                    .andExpect(status().isAccepted())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TaskDto actual = objectMapper.readValue(responseBody, TaskDto.class);
            assertEquals(email, actual.getPerformer().getEmail());
            expected.setAuthor(actual.getAuthor());
            expected.setDescription(actual.getDescription());
            expected.setPriority(actual.getPriority());
            expected.setHeader(actual.getHeader());
            expected.setStatus(actual.getStatus());
            expected.setPerformer(actual.getPerformer());
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("Tests for delete an task")
    class DeleteTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteById_shouldReturnOk_whenTaskIsExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            UUID id = UUID.fromString("3412cd10-9ec0-41f4-802f-e6440792fed2");
            mockMvc.perform(delete("/api/tasks/{id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteById_shouldReturnNotFoundError_whenTaskNotExistsById() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String badId = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6506";
            String expectedMsg = "Task delete error. Task not found by id = " + badId;
            mockMvc.perform(delete("/api/tasks/{id}", badId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$..message").value(expectedMsg));
        }

        @Test
        @WithUserDetails("example1@mail.ru")
        void deleteAll_shouldReturnOk() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            mockMvc.perform(delete("/api/tasks"))
                    .andExpect(status().isOk());
        }
    }
}

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
import java.time.LocalDateTime;
import java.util.*;
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
public class TaskIT {

    private final MockMvc mockMvc;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private String jwtToken;

    @Nested
    @DisplayName("Test for task's find")
    class FindTest {
        @Test
        @WithUserDetails("example1@mail.ru")
        void findAllByTaskId_shouldReturnListValidTaskDto() throws Exception {
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

            String commentId1 = "216a035d-3c5f-4c74-a860-6d1cb6d27a88";
            String commentId2 = "433c2bf7-41c3-462d-8d71-e01ee1849bcc";
            String commentId3 = "5ea51c64-a8f8-42b2-9e8a-626690e0a187";
            String commentId4 = "b12c19cd-ebe2-4c10-be56-4623ce569542";
            String commentId5 = "1272adcc-ed29-4835-9a26-bf9be266ab64";
            Timestamp time1 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 0, 1, 1));
            Timestamp time2 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 1, 1));
            Timestamp time3 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 3, 0, 1, 1));
            Timestamp time4 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 4, 0, 1, 1));
            Timestamp time5 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 5, 0, 1, 1));
            String content1 = "Content1";
            String content2 = "Content2";
            String content3 = "Content3";
            String content4 = "Content4";
            String content5 = "Content5";
            CommentDto commentDto1 = new CommentDto(UUID.fromString(commentId1), time1, user1, content1, UUID.fromString(id1));
            CommentDto commentDto2 = new CommentDto(UUID.fromString(commentId2), time2, user2, content2, UUID.fromString(id1));
            CommentDto commentDto3 = new CommentDto(UUID.fromString(commentId3), time3, user3, content3, UUID.fromString(id2));
            CommentDto commentDto4 = new CommentDto(UUID.fromString(commentId4), time4, user1, content4, UUID.fromString(id2));
            CommentDto commentDto5 = new CommentDto(UUID.fromString(commentId5), time5, user3, content5, null);

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

            String id1 = "3412cd10-9ec0-41f4-802f-e6440792fed2";
            String header1 = "header1";
            String description1 = "description1";
            Status status1 = Status.OPENED;
            Priority priority1 = Priority.HIGH;

            String commentId1 = "216a035d-3c5f-4c74-a860-6d1cb6d27a88";
            String commentId2 = "433c2bf7-41c3-462d-8d71-e01ee1849bcc";
            Timestamp time1 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 1, 0, 1, 1));
            Timestamp time2 = Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 1, 1));
            String content1 = "Content1";
            String content2 = "Content2";
            CommentDto commentDto1 = new CommentDto(UUID.fromString(commentId1), time1, user1, content1, UUID.fromString(id1));
            CommentDto commentDto2 = new CommentDto(UUID.fromString(commentId2), time2, user2, content2, UUID.fromString(id1));

            TaskDto expected = new TaskDto(UUID.fromString(id1), header1, description1, status1, priority1, user1, user2); //, List.of(commentDto1, commentDto2)

            MvcResult result = mockMvc.perform(get("/api/tasks/{id}", id1)
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


    @Test
    @WithUserDetails("example1@mail.ru")
    void save_shouldReturnValidTaskDto_whenDataIsValid() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        jwtToken = jwtTokenUtils.generateToken(userDetails);

        String header1 = "header4";
        String description1 = "description4";
        Status status1 = Status.OPENED;
        Priority priority1 = Priority.HIGH;
        String performerId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
        String performerEmail = "example1@mail.ru";
        String performerPassword = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
        UserDto performer = new UserDto(UUID.fromString(performerId1), performerEmail, performerPassword);

        CreateUpdateTaskDto saved = new CreateUpdateTaskDto(header1, description1, status1.toString(), priority1.toString(), performerEmail);
        TaskDto expected = new TaskDto(null, header1, description1, status1, priority1, null, performer); //, null
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
    void save_shouldReturnError_whenSomeDataIsNotValid() throws Exception {
        String expectedMsg = "The header must contain at least one non-whitespace character";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        jwtToken = jwtTokenUtils.generateToken(userDetails);

        String header1 = null;
        String description1 = "description4";
        Status status1 = Status.OPENED;
        Priority priority1 = Priority.HIGH;
        String performerId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
        String performerEmail = "example1@mail.ru";
        String performerPassword = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
        UserDto performer = new UserDto(UUID.fromString(performerId1), performerEmail, performerPassword);

        CreateUpdateTaskDto saved = new CreateUpdateTaskDto(header1, description1, status1.toString(), priority1.toString(), performerEmail);
        TaskDto expected = new TaskDto(null, header1, description1, status1, priority1, null, performer);
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

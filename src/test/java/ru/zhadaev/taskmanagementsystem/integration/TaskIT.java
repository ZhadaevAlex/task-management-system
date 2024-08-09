package ru.zhadaev.taskmanagementsystem.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import ru.zhadaev.taskmanagementsystem.dto.CommentDto;
import ru.zhadaev.taskmanagementsystem.dto.Priority;
import ru.zhadaev.taskmanagementsystem.dto.TaskDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.security.JwtTokenUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @BeforeEach
    public void setUp() {

    }

    @WithUserDetails("example1@mail.ru")
    @Test
    void findAllByTaskId_shouldReturnListValidTaskDto() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        jwtToken = jwtTokenUtils.generateToken(userDetails);

        String userId1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
        String userId2 = "82dc22a0-d332-4241-959a-b20f8590155f";
        String userId3 = "5405c989-9dbf-4e35-a923-8b1d4e4ad7bc";
        String userId4 = "fe388668-d648-49f5-9764-5c6c8190806a";
        String userId5 = "8deadac2-b96a-4c21-bafc-bde14f018716";
        String userId6 = "3a551150-aa77-4b64-a38b-ba42519fbfd3";
        String userEmail1 = "example1@mail.ru";
        String userEmail2 = "example2@mail.ru";
        String userEmail3 = "example3@mail.ru";
        String userEmail4 = "example4@mail.ru";
        String userEmail5 = "example5@mail.ru";
        String userEmail6 = "example6@mail.ru";
        String userPassword1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
        String userPassword2 = "$2a$12$URB3m7C1..eOQRmSPOhDVu6aXKx4VqD1ZTtjRTr8nIjW88uYJnP7S";
        String userPassword3 = "$2a$12$S6/o8jkidnqFvwx16KXHVOyAVrXwDiusCGsBECYhFUrTCsJAPsRFu";
        String userPassword4 = "$2a$12$2LXjXVV3zBwrr5HBlp.KzOPLE77wwM4wtGA6rEdge3I.tsmcfR4XO";
        String userPassword5 = "$2a$12$/l7H.G8I8vO4DzuTYrkzzuCX.dM/khqUc.xnFGe6yhgKO5t0s2n7m";
        String userPassword6 = "$2a$12$QE8RVMAGfL5rA0GkicrMHOejsmduokWnmpz9ckxGV0DA1u7BUOWMC";
        UserDto user1 = new UserDto(UUID.fromString(userId1), userEmail1, userPassword1);
        UserDto user2 = new UserDto(UUID.fromString(userId2), userEmail2, userPassword2);
        UserDto user3 = new UserDto(UUID.fromString(userId3), userEmail3, userPassword3);
        UserDto user4 = new UserDto(UUID.fromString(userId4), userEmail4, userPassword4);
        UserDto user5 = new UserDto(UUID.fromString(userId5), userEmail5, userPassword5);
        UserDto user6 = new UserDto(UUID.fromString(userId6), userEmail6, userPassword6);

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
        Timestamp time1 = Timestamp.valueOf(LocalDateTime.of(2024, 01, 01, 00, 01, 01));
        Timestamp time2 = Timestamp.valueOf(LocalDateTime.of(2024, 01, 02, 00, 01, 01));
        Timestamp time3 = Timestamp.valueOf(LocalDateTime.of(2024, 01, 03, 00, 01, 01));
        Timestamp time4 = Timestamp.valueOf(LocalDateTime.of(2024, 01, 04, 00, 01, 01));
        Timestamp time5 = Timestamp.valueOf(LocalDateTime.of(2024, 01, 05, 00, 01, 01));
        String content1 = "Content1";
        String content2 = "Content2";
        String content3 = "Content3";
        String content4 = "Content4";
        String content5 = "Content5";
        CommentDto commentDto1 = new CommentDto(UUID.fromString(commentId1), time1, user1, content1);
        CommentDto commentDto2 = new CommentDto(UUID.fromString(commentId2), time2, user2, content2);
        CommentDto commentDto3 = new CommentDto(UUID.fromString(commentId3), time3, user3, content3);
        CommentDto commentDto4 = new CommentDto(UUID.fromString(commentId4), time4, user1, content4);
        CommentDto commentDto5 = new CommentDto(UUID.fromString(commentId5), time5, user3, content5);

        TaskDto task1 = new TaskDto(UUID.fromString(id1), header1, description1, status1, priority1, user1, user2, List.of(commentDto1, commentDto2));
        TaskDto task2 = new TaskDto(UUID.fromString(id2), header2, description2, status2, priority2, user1, user3, List.of(commentDto3, commentDto4, commentDto5));
        TaskDto task3 = new TaskDto(UUID.fromString(id3), header3, description3, status3, priority3, user2, user3, new ArrayList<>());
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
}

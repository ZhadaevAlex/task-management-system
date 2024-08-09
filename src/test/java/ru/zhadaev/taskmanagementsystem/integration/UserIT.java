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
import org.springframework.security.core.userdetails.UserDetailsService;
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
import ru.zhadaev.taskmanagementsystem.dao.entity.User;
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

    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private String jwtToken;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Test
    @WithUserDetails("example1@mail.ru")
    void findAll_shouldReturnFirstPageOfListOfTwoValidUserDto() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        jwtToken = jwtTokenUtils.generateToken(userDetails);

        String id1 = "fedd6a4f-f0e8-4a50-82e7-8b69bffc6507";
        String id2 = "82dc22a0-d332-4241-959a-b20f8590155f";
        String id3 = "5405c989-9dbf-4e35-a923-8b1d4e4ad7bc";
        String id4 = "fe388668-d648-49f5-9764-5c6c8190806a";
        String id5 = "8deadac2-b96a-4c21-bafc-bde14f018716";
        String id6 = "3a551150-aa77-4b64-a38b-ba42519fbfd3";
        String email1 = "example1@mail.ru";
        String email2 = "example2@mail.ru";
        String email3 = "example3@mail.ru";
        String email4 = "example4@mail.ru";
        String email5 = "example5@mail.ru";
        String email6 = "example6@mail.ru";
        String password1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
        String password2 = "$2a$12$URB3m7C1..eOQRmSPOhDVu6aXKx4VqD1ZTtjRTr8nIjW88uYJnP7S";
        String password3 = "$2a$12$S6/o8jkidnqFvwx16KXHVOyAVrXwDiusCGsBECYhFUrTCsJAPsRFu";
        String password4 = "$2a$12$2LXjXVV3zBwrr5HBlp.KzOPLE77wwM4wtGA6rEdge3I.tsmcfR4XO";
        String password5 = "$2a$12$/l7H.G8I8vO4DzuTYrkzzuCX.dM/khqUc.xnFGe6yhgKO5t0s2n7m";
        String password6 = "$2a$12$QE8RVMAGfL5rA0GkicrMHOejsmduokWnmpz9ckxGV0DA1u7BUOWMC";
        List<UserDto> expected = List.of(
                new UserDto(UUID.fromString(id3), email3, password3),
                new UserDto(UUID.fromString(id4), email4, password4));

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
        String email1 = "example1@mail.ru";
        String password1 = "$2a$12$XCoPKbTUfLzWYda0yCsSHuy9M9gVJATttpyvuInpkXmso9g2SME1W";
        UserDto expected = new UserDto(id1, email1, password1);

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
    void findById_shouldReturnNotFoundError_whenUserIsNotExists() throws Exception {
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

    @Nested
    @DisplayName("Tests for creation an student")
    class CreateTest {

        @Test
        @WithUserDetails("example1@mail.ru")
        void save_shouldReturnValidUserDto_whenNameIsValid() throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtToken = jwtTokenUtils.generateToken(userDetails);

            String email = "example7@mail.ru";
            String password = "Password7#";
            UserDto saved = new UserDto();
            saved.setEmail(email);
            saved.setPassword(password);

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
            saved.setId(actual.getId());
            assertTrue(passwordEncoder.matches(saved.getPassword(), actual.getPassword()));
            saved.setPassword(actual.getPassword());
            assertEquals(saved, actual);
        }
    }
}

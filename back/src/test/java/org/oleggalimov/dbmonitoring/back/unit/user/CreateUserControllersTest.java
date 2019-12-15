package org.oleggalimov.dbmonitoring.back.unit.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.endpoints.user.CreateUser;
import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateUserControllersTest {
    private MockMvc mockMvc;
    private JsonNode responseSchema;
    private static ObjectMapper mapper = new ObjectMapper();
    private static User user;

    @Spy
    ResponseBuilder builder = new ResponseBuilder(mapper);

    @Mock
    UserService userService;

    @InjectMocks
    private CreateUser createUser;


    @BeforeEach
    private void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(createUser).build();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
        user = new User("login", "e@mail.ru", null, "firstname", "lastName", "personNumber", "password", UserStatus.ACTIVE);
    }

    @Tag("create/user")
    @Test
    void createUserTest() throws Exception {
        String body = mapper.writeValueAsString(user);
        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);
        String result = mockMvc.
                perform(post("/create/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("U_I_01"))
                .andExpect(jsonPath("$.messages[0].title").value("Success user operation: create"))
                .andExpect(jsonPath("$.messages[0].message").value("User was successfully added"))
                .andExpect(jsonPath("$.messages[0].type").value("INFO"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    @Tag("create/user")
    @Test
    void createUserTestWithNotAddedResult() throws Exception {
        String body = mapper.writeValueAsString(user);
        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(null);
        String result = mockMvc.
                perform(post("/create/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("U_W_01"))
                .andExpect(jsonPath("$.messages[0].title").value("Add user error"))
                .andExpect(jsonPath("$.messages[0].message").value("User was not added"))
                .andExpect(jsonPath("$.messages[0].type").value("WARNING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    @Tag("create/user")
    @Test
    void createUserTestWithBadUserInfo() throws Exception {
        User badUser = new User("login", null, null, "firstname", "lastName", "personNumber", "q", UserStatus.ACTIVE);
        String body = mapper.writeValueAsString(badUser);
        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(null);
        String result = mockMvc.
                perform(post("/create/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("U_04"))
                .andExpect(jsonPath("$.errors[0].title").value("User info is invalid"))
                .andExpect(jsonPath("$.errors[0].message").value("Password is too short"))
                .andExpect(jsonPath("$.errors[1].code").value("U_05"))
                .andExpect(jsonPath("$.errors[1].title").value("User info is invalid"))
                .andExpect(jsonPath("$.errors[1].message").value("E-mail address is malformed"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    private void validateJsonFromString(JsonNode json) throws ProcessingException {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(responseSchema);
        ProcessingReport report = schema.validate(json);
        if (!report.isSuccess()) {
            System.out.println("Validation errors: " + report);
            Assertions.fail();
        }
    }
}
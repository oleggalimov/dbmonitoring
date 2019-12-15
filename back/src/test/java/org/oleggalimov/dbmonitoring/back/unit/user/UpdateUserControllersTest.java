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
import org.oleggalimov.dbmonitoring.back.endpoints.user.UpdateUser;
import org.oleggalimov.dbmonitoring.back.entities.User;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateUserControllersTest {
    private MockMvc mockMvc;
    private JsonNode responseSchema;
    private static ObjectMapper mapper = new ObjectMapper();
    private static User user;

    @Spy
    ResponseBuilder builder = new ResponseBuilder(mapper);

    @Mock
    UserService userService;

    @InjectMocks
    private UpdateUser updateUser;


    @BeforeEach
    private void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(updateUser).build();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
        user = new User("login", "e@mail.ru", null, "firstname", "lastName", "personNumber", "password", UserStatus.ACTIVE);
    }

    @Tag("update/user")
    @Test
    void updateUserTest() throws Exception {
        String body = "{\"login\":\"login\",\"roles\":null,\"firstName\":\"firstname\",\"lastName\":\"lastName\",\"personNumber\":\"personNumber\",\"password\":\"q12345678\",\"status\":\"ACTIVE\",\"email\":\"qq@qq.ru\"}";
        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(true);
        String result = mockMvc.
                perform(put("/update/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("U_I_02"))
                .andExpect(jsonPath("$.messages[0].title").value("Success user operation: update"))
                .andExpect(jsonPath("$.messages[0].message").value("User was successfully updated"))
                .andExpect(jsonPath("$.messages[0].type").value("INFO"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    @Tag("update/user")
    @Test
    void updateUserTestWithNotUpdatedResult() throws Exception {
        String body = "{\"login\":\"login\",\"roles\":null,\"firstName\":\"firstname\",\"lastName\":\"lastName\",\"personNumber\":\"personNumber\",\"password\":\"q12345678\",\"status\":\"ACTIVE\",\"email\":\"qq@qq.ru\"}";
        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(false);
        String result = mockMvc.
                perform(put("/update/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("U_W_04"))
                .andExpect(jsonPath("$.messages[0].title").value("Update instance error"))
                .andExpect(jsonPath("$.messages[0].message").value("User was not updated"))
                .andExpect(jsonPath("$.messages[0].type").value("WARNING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    @Tag("update/user")
    @Test
    void updateUserTestWithBadUser() throws Exception {
        String body = "{\"login\":\"login\",\"roles\":null,\"firstName\":\"firstname\",\"lastName\":\"lastName\",\"personNumber\":\"personNumber\",\"password\":\"q678\",\"status\":\"ACTIVE\",\"email\":\"\"}";
        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(false);
        String result = mockMvc.
                perform(put("/update/user")
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
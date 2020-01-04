package org.oleggalimov.dbmonitoring.back.tests.unit.user;

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
import org.oleggalimov.dbmonitoring.back.endpoints.user.GetUser;
import org.oleggalimov.dbmonitoring.back.entities.MonitoringSystemUser;
import org.oleggalimov.dbmonitoring.back.enumerations.Role;
import org.oleggalimov.dbmonitoring.back.enumerations.UserStatus;
import org.oleggalimov.dbmonitoring.back.services.UserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetMonitoringSystemUserControllersTest {
    private MockMvc mockMvc;
    private JsonNode responseSchema;
    private static ObjectMapper mapper = new ObjectMapper();
    private static MonitoringSystemUser monitoringSystemUser;

    @Spy
    ResponseBuilder builder = new ResponseBuilder(mapper);

    @Mock
    UserService userService;

    @InjectMocks
    private GetUser getUser;


    @BeforeEach
    private void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(getUser).build();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
        Set<Role> roles = EnumSet.allOf(Role.class);
        monitoringSystemUser = new MonitoringSystemUser("login", "e@mail.ru", roles, "first_name", "lastName", "personNumber", "password", UserStatus.ACTIVE);
    }

    @Tag("list/user/{login}")
    @Test
    void getUserTest() throws Exception {
        Mockito.when(userService.findUserByLogin(Mockito.anyString())).thenReturn(monitoringSystemUser);
        String result = mockMvc.
                perform(get("/list/user/login"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body.USERS[0].login").value("login"))
                .andExpect(jsonPath("$.body.USERS[0].roles[0]").value("ADMIN"))
                .andExpect(jsonPath("$.body.USERS[0].roles[1]").value("USER_ADMIN"))
                .andExpect(jsonPath("$.body.USERS[0].roles[2]").value("USER"))
                .andExpect(jsonPath("$.body.USERS[0].firstName").value("first_name"))
                .andExpect(jsonPath("$.body.USERS[0].lastName").value("lastName"))
                .andExpect(jsonPath("$.body.USERS[0].personNumber").value("personNumber"))
                .andExpect(jsonPath("$.body.USERS[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.body.USERS[0].email").value("e@mail.ru"))
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        validateJsonFromString(mapper.readTree(result));
        System.out.println(result);
    }

    @Tag("list/user/{login}")
    @Test
    void getUserTestWithNoSuchUser() throws Exception {
        Mockito.when(userService.findUserByLogin(Mockito.anyString())).thenReturn(null);
        String result = mockMvc.
                perform(get("/list/user/login"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("U_W_02"))
                .andExpect(jsonPath("$.messages[0].title").value("Read user error"))
                .andExpect(jsonPath("$.messages[0].message").value("No user was found in database"))
                .andExpect(jsonPath("$.messages[0].type").value("WARNING"))
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
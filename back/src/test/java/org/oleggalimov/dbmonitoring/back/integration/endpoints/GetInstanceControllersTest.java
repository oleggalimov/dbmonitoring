package org.oleggalimov.dbmonitoring.back.integration.endpoints;

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
import org.oleggalimov.dbmonitoring.back.configuration.MainConfiguration;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseUser;
import org.oleggalimov.dbmonitoring.back.dto.Error;
import org.oleggalimov.dbmonitoring.back.dto.Message;
import org.oleggalimov.dbmonitoring.back.dto.RestResponse;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)
class GetInstanceControllersTest {
    private static MockMvc mockMvc;
    private static ObjectMapper mapper;
    private static JsonNode responseSchema;
    private static boolean isContextInit = false;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() throws Exception {
        if (!isContextInit) {
            mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
            mapper = new ObjectMapper();
            File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
            responseSchema = mapper.readTree(file);
            isContextInit = true;
        }
    }

    @Tag("/list/instance/{id}")
    @Test
    void listInstanceById() throws Exception {
        String responseString = mockMvc
                .perform(get("/list/instance/test"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseString);
        validateJsonFromString(mapper.readTree(responseString));
        RestResponse response = mapper.readerFor(RestResponse.class).readValue(responseString);
        assertEquals(0, response.getErrors().size());
        assertEquals(0, response.getMessages().size());

        assertTrue(response.isSuccess());
        Map body = response.getBody().getItems();
        ArrayList instances = mapper.convertValue(body.get("INSTANCES"), ArrayList.class);
        DataBaseInstance dataBaseInstance = mapper.convertValue(instances.get(0), DataBaseInstance.class);
        assertEquals("test", dataBaseInstance.getId());
        assertEquals("host", dataBaseInstance.getHost());
        assertEquals(1520, dataBaseInstance.getPort());
        assertEquals("sid", dataBaseInstance.getSid());
        assertEquals(DatabaseInstanceType.ORACLE, dataBaseInstance.getType());
        DataBaseUser dataBaseInstanceUser = dataBaseInstance.getUser();
        assertEquals("login", dataBaseInstanceUser.getLogin());
        assertNull(dataBaseInstanceUser.getPassword());
    }

    @Tag("/list/instance/{id}")
    @Test
    void listInstanceByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/list/instance/"))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Tag("/list/instance/{id}")
    @Test
    void listInstanceByIdEmptyString() throws Exception {
        String responseString = mockMvc
                .perform(get("/list/instance/   "))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseString);
        validateJsonFromString(mapper.readTree(responseString));
        RestResponse response = mapper.readerFor(RestResponse.class).readValue(responseString);
        assertFalse(response.isSuccess());
        assertEquals(0, response.getBody().getItems().size());
        assertEquals(0, response.getMessages().size());
        Error error = response.getErrors().get(0);
        assertEquals("DBI_02", error.getCode());
        assertEquals("Database instance info is invalid", error.getTitle());
        assertEquals("Id is empty", error.getMessage());
    }

    @Tag("/list/instance/{id}")
    @Test
    void listInstanceByIdNoSuchElement() throws Exception {
        String responseString = mockMvc
                .perform(get("/list/instance/noSuchElement"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseString);
        validateJsonFromString(mapper.readTree(responseString));
        RestResponse response = mapper.readerFor(RestResponse.class).readValue(responseString);
        assertFalse(response.isSuccess());
        assertEquals(0, response.getBody().getItems().size());
        assertEquals(0, response.getErrors().size());
        Message message = response.getMessages().get(0);
        assertEquals("DBI_W_01", message.getCode());
        assertEquals("Read instance error", message.getTitle());
        assertEquals("No instance was found in list", message.getMessage());
        assertEquals(MessageType.WARNING, message.getType());
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
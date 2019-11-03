package org.oleggalimov.dbmonitoring.back.endpoints.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbInstanceImpl;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbUserImpl;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.CheckStatus;
import org.oleggalimov.dbmonitoring.back.endpoints.CreateInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.ListInstances;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RestControllersErrorsTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private JsonNode responseSchema;

    @InjectMocks
    ListInstances listInstancesController;

    @InjectMocks
    CheckStatus statusController;

    @InjectMocks
    CreateInstance createInstanceController;
    @Mock
    CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    @Spy
    ResponseBuilder builder = new ResponseBuilder(new ObjectMapper());

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(listInstancesController, statusController, createInstanceController).build();
        mapper = new ObjectMapper();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
    }

    @Tag("Rest")
    @Test
    void listTest() throws Exception {
        String response = this.mockMvc
                .perform(get("/list"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        assertTrue(validateJsonFromString(jsonNode));
        assertFalse(jsonNode.get("success").asBoolean());
        assertNull(jsonNode.get("body"));
    }

    @Tag("Rest")
    @Test
    void shouldReturnTwoMessagesTest() throws Exception {
        String response = this.mockMvc
                .perform(
                        post("/createinstance")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content("{}"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        final JsonNode jsonNode = mapper.readTree(response);
        assertFalse(jsonNode.get("success").asBoolean());
        assertNull(jsonNode.get("body"));
        assertEquals(2, jsonNode.get("messages").size());
        assertEquals(0, jsonNode.get("errors").size());
        validateJsonFromString(jsonNode);
    }

    @Tag("Rest")
    @Test
    void shouldReturnOneInstanceErrorMessagesTest() throws Exception {
        DbInstanceImpl instance = new DbInstanceImpl("test_id", "test_host", 1251, "sid", null);
        String response = this.mockMvc
                .perform(
                        post("/createinstance")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(mapper.writeValueAsString(instance)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        final JsonNode jsonNode = mapper.readTree(response);
        assertFalse(jsonNode.get("success").asBoolean());
        assertNull(jsonNode.get("body"));
        assertEquals(1, jsonNode.get("messages").size());
        assertEquals(0, jsonNode.get("errors").size());
        validateJsonFromString(jsonNode);
    }

    @Tag("Rest")
    @Test
    void shouldReturnOneUserErrorMessagesTest() throws Exception {
        DbInstanceImpl instance = new DbInstanceImpl("test_id", "test_host", 1251, "sid", new DbUserImpl("login", null));
        String response = this.mockMvc
                .perform(
                        post("/createinstance")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(mapper.writeValueAsString(instance)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        final JsonNode jsonNode = mapper.readTree(response);
        assertFalse(jsonNode.get("success").asBoolean());
        assertNull(jsonNode.get("body"));
        assertEquals(1, jsonNode.get("messages").size());
        assertEquals(0, jsonNode.get("errors").size());
        validateJsonFromString(jsonNode);
    }


    private boolean validateJsonFromString(JsonNode json) throws ProcessingException {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(responseSchema);
        ProcessingReport report = schema.validate(json);
        boolean result = report.isSuccess();
        if (!result) {
            System.out.println("Ошибка валидации по схеме!");
            System.out.println(report);
        }
        return result;
    }
}
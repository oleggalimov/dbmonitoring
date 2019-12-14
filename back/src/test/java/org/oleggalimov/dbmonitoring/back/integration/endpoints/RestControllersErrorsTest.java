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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.CheckStatus;
import org.oleggalimov.dbmonitoring.back.endpoints.instance.CreateInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.instance.DeleteInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.instance.GetInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.instance.ListInstances;
import org.oleggalimov.dbmonitoring.back.endpoints.instance.UpdateInstance;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @InjectMocks
    DeleteInstance deleteInstanceController;

    @InjectMocks
    GetInstance getInstanceController;

    @InjectMocks
    UpdateInstance updateInstanceController;


    @Mock
    CopyOnWriteArraySet<DataBaseInstance> instanceSet;

    @Spy //добавит настоящий объект в контекст?!
            ResponseBuilder builder = new ResponseBuilder(new ObjectMapper());

    private Throwable exception;
    private static String body = "{\"id\":\"test_id\",\"host\":\"test_host\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"password\"}, \"type\":\"ORACLE\"}";

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(
                        listInstancesController, statusController, createInstanceController, deleteInstanceController,
                        getInstanceController, updateInstanceController
                )
                .build();
        mapper = new ObjectMapper();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
        exception = new NullPointerException("Crap");
    }

    @Tag("/create/instance")
    @Test
    void listErrorTest() throws Exception {
        Mockito.when(instanceSet.add(Mockito.any(DataBaseInstance.class))).thenThrow(exception);
        String response = this.mockMvc
                .perform(post("/create/instance")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("REST_EXCEPTION"))
                .andExpect(jsonPath("$.errors[0].title").value("Critical error"))
                .andExpect(jsonPath("$.errors[0].message").value("Crap"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
    }

    @Tag("/delete/instance/{id}")
    @Test
    void deleteInstanceErrorTest() throws Exception {
        Mockito.when(instanceSet.remove(Mockito.any(DataBaseInstance.class))).thenThrow(exception);
        String response = this.mockMvc
                .perform(delete("/delete/instance/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("REST_EXCEPTION"))
                .andExpect(jsonPath("$.errors[0].title").value("Critical error"))
                .andExpect(jsonPath("$.errors[0].message").value("null"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
    }

    @Tag("/list/instance/{id}")
    @Test
    void getInstanceErrorTest() throws Exception {
        String response = this.mockMvc
                .perform(get("/list/instance/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("REST_EXCEPTION"))
                .andExpect(jsonPath("$.errors[0].title").value("Critical error"))
                .andExpect(jsonPath("$.errors[0].message").value("null"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
    }

    @Tag("/list/instance/all")
    @Test
    void listInstanceErrorTest() throws Exception {
        String response = this.mockMvc
                .perform(get("/list/instance/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("REST_EXCEPTION"))
                .andExpect(jsonPath("$.errors[0].title").value("Critical error"))
                .andExpect(jsonPath("$.errors[0].message").value("java.lang.NullPointerException"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
    }

    @Tag("/update/instance")
    @Test
    void updateInstanceErrorTest() throws Exception {
        Mockito.when(instanceSet.contains(Mockito.any(DataBaseInstance.class))).thenThrow(exception);
        String response = this.mockMvc
                .perform(put("/update/instance")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("REST_EXCEPTION"))
                .andExpect(jsonPath("$.errors[0].title").value("Critical error"))
                .andExpect(jsonPath("$.errors[0].message").value("Crap"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(response);
        assertNotNull(response);
        assertTrue(response.length() > 0);
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
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
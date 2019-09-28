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
import org.oleggalimov.dbmonitoring.back.builders.ResponceBuilder;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.endpoints.ListInstances;
import org.oleggalimov.dbmonitoring.back.endpoints.Status;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RestControllersErrorsTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private JsonNode responseSchema;

    @InjectMocks
    ListInstances listInstancesController;

    @InjectMocks
    Status statusController;
    @Mock
    CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    @Spy
    ResponceBuilder builder = new ResponceBuilder(new ObjectMapper());

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(listInstancesController, statusController).build();
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
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)

class CreateInstanceControllersTest {
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

    @Tag("/create/instance")
    @Test
    void addInstance() throws Exception {
        String body = "{\"id\":\"test_id\",\"host\":\"test_host\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"password\"}, \"type\":\"ORACLE\"}";
        String response = mockMvc
                .perform(
                        post("/create/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("DBI_I_01"))
                .andExpect(jsonPath("$.messages[0].title").value("Success instance operation: create"))
                .andExpect(jsonPath("$.messages[0].message").value("Instance was successfully added"))
                .andExpect(jsonPath("$.messages[0].type").value("INFO"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        assertTrue(instanceHashSet.size() > 1);
    }

    @Tag("/create/instance")
    @Test
    void addInstanceWithBadIdAndUserPassword() throws Exception {
        String body = "{\"id\":\"   \",\"host\":\"test_host\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\" 11\"}, \"type\":\"ORACLE\"}";
        String response = mockMvc
                .perform(
                        post("/create/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("DBI_02"))
                .andExpect(jsonPath("$.errors[0].title").value("Database instance info is invalid"))
                .andExpect(jsonPath("$.errors[0].message").value("Id is empty"))
                .andExpect(jsonPath("$.errors[1].code").value("DBU_04"))
                .andExpect(jsonPath("$.errors[1].title").value("Database user info is invalid"))
                .andExpect(jsonPath("$.errors[1].message").value("Password is too short"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
    }

    @Tag("/create/instance")
    @Test
    void addInstanceWithBadRequest() throws Exception {
        String body = "{\"id\":\"   \",\"host\":\"test_host\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\" 11\"}, \"type\":\"ORACLEEEEEEE\"}";
        mockMvc
                .perform(
                        post("/create/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is4xxClientError());
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
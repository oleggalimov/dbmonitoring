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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)

class UpdateInstanceControllersTest {
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

    @Tag("/update/instance")
    @Test
    void updateInstance() throws Exception {
        String body = "{\"id\":\"test\",\"host\":\"1520\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"newPassword\"}, \"type\":\"ORACLE\"}";
        String response = mockMvc
                .perform(
                        put("/update/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("DBI_I_02"))
                .andExpect(jsonPath("$.messages[0].title").value("Success instance operation: update"))
                .andExpect(jsonPath("$.messages[0].message").value("Instance was successfully updated"))
                .andExpect(jsonPath("$.messages[0].type").value("INFO"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        instanceHashSet.forEach(instance -> {
            DataBaseInstance dataBaseInstance = (DataBaseInstance) instance;
            assertEquals("newPassword", dataBaseInstance.getUser().getPassword());
        });
    }

    @Tag("/update/instance")
    @Test
    void updateInstanceWithBadData() throws Exception {
        String body = "{\"id\":\"test\",\"host\":\"awe\",\"port\":123,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"pass\"}, \"type\":\"ORACLE\"}";
        String response = mockMvc
                .perform(
                        put("/update/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("DBU_04"))
                .andExpect(jsonPath("$.errors[0].title").value("Database user info is invalid"))
                .andExpect(jsonPath("$.errors[0].message").value("Password is too short"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        instanceHashSet.forEach(instance -> {
            DataBaseInstance dataBaseInstance = (DataBaseInstance) instance;
            assertEquals("password", dataBaseInstance.getUser().getPassword());
        });
    }

    @Tag("/update/instance")
    @Test
    void updateInstanceWithNoSuchInstance() throws Exception {
        String body = "{\"id\":\" q\",\"host\":\"awe\",\"port\":123,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"password\"}, \"type\":\"ORACLE\"}";
        String response = mockMvc
                .perform(
                        put("/update/instance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("DBI_W_01"))
                .andExpect(jsonPath("$.messages[0].title").value("Read instance error"))
                .andExpect(jsonPath("$.messages[0].message").value("No instance was found in list"))
                .andExpect(jsonPath("$.messages[0].type").value("WARNING"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        instanceHashSet.forEach(instance -> {
            DataBaseInstance dataBaseInstance = (DataBaseInstance) instance;
            assertEquals("password", dataBaseInstance.getUser().getPassword());
        });
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
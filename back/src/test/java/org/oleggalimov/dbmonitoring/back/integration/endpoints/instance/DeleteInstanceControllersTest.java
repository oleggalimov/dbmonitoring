package org.oleggalimov.dbmonitoring.back.integration.endpoints.instance;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DeleteInstanceControllersTest {
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

    @Tag("/delete/instance/{id}")
    @Test
    void deleteInstance() throws Exception {

        String response = mockMvc
                .perform(
                        delete("/delete/instance/test"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("DBI_I_03"))
                .andExpect(jsonPath("$.messages[0].title").value("Success instance operation: delete"))
                .andExpect(jsonPath("$.messages[0].message").value("Instance was successfully deleted"))
                .andExpect(jsonPath("$.messages[0].type").value("INFO"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        assertEquals(0, instanceHashSet.size());
    }

    @Tag("/delete/instance/{id}")
    @Test
    void deleteInstanceWithBadId() throws Exception {

        String response = mockMvc
                .perform(
                        delete("/delete/instance/   "))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.messages").isEmpty())
                .andExpect(jsonPath("$.errors[0].code").value("DBI_02"))
                .andExpect(jsonPath("$.errors[0].title").value("Database instance info is invalid"))
                .andExpect(jsonPath("$.errors[0].message").value("Id is empty"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        assertEquals(1, instanceHashSet.size());
    }

    @Tag("/delete/instance/{id}")
    @Test
    void deleteInstanceWithNoSuchId() throws Exception {

        String response = mockMvc
                .perform(
                        delete("/delete/instance/noSuchId"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.body").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.messages[0].code").value("DBI_W_02"))
                .andExpect(jsonPath("$.messages[0].title").value("Delete instance error"))
                .andExpect(jsonPath("$.messages[0].message").value("Instance was not deleted"))
                .andExpect(jsonPath("$.messages[0].type").value("WARNING"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final JsonNode jsonNode = mapper.readTree(response);
        validateJsonFromString(jsonNode);
        System.out.println(jsonNode);
        CopyOnWriteArraySet instanceHashSet = (CopyOnWriteArraySet) wac.getBean("instanceHashSet");
        assertEquals(1, instanceHashSet.size());
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
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
import org.oleggalimov.dbmonitoring.back.dto.RestResponse;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)

class ListInstanceControllersTest {
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

    @Tag("list/instance/all")
    @Test
    void listInstances() throws Exception {
        String responseString = mockMvc
                .perform(get("/list/instance/all"))
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
package org.oleggalimov.dbmonitoring.back.endpoints.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.oleggalimov.dbmonitoring.back.configuration.MainConfiguration;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = MainConfiguration.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)
class RestControllersTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private JsonNode responseSchema;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mapper = new ObjectMapper();
        File file = ResourceUtils.getFile(this.getClass().getResource("/Response.json"));
        responseSchema = mapper.readTree(file);
    }

    @Test
    void testContext() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("checkStatus"));
        assertNotNull(wac.getBean("listInstances"));
    }

    @Tag("Rest")
    @Test
    void statusGet() throws Exception {
        this.mockMvc
                .perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Back is Ok!"));
    }

    @Tag("Rest")
    @Test
    void listInstancesGet() throws Exception {
        String response = this.mockMvc
                .perform(get("/list"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertNotNull(response);
        assertTrue(response.length() > 0);
        System.out.println(response);
        final JsonNode jsonNode = mapper.readTree(response);
        assertTrue(validateJsonFromString(jsonNode));
        assertTrue(jsonNode.get("success").asBoolean());
        assertFalse(jsonNode.get("body").get("INSTANCES").isNull());
    }

    @Tag("Rest")
    @Test
    void addInstance() throws Exception {
        String body = "{\"id\":\"test_id\",\"host\":\"test_host\",\"port\":1251,\"sid\":\"sid\",\"user\":{\"login\":\"login\", \"password\":\"pass\"}}";
        System.out.println(body);
        String response = this.mockMvc
                .perform(
                        post("/createinstance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode jsonNode = mapper.readTree(response);
        assertTrue(jsonNode.get("success").asBoolean());
        assertNull(jsonNode.get("body"));
        assertEquals(1, jsonNode.get("messages").size());
        assertEquals(0, jsonNode.get("errors").size());
        CopyOnWriteArraySet<CommonDbInstance> instanceHashSet = (CopyOnWriteArraySet<CommonDbInstance>) wac.getBean("instanceHashSet");
        assertTrue(instanceHashSet.size() > 1);
        validateJsonFromString(jsonNode);
    }


    private boolean validateJsonFromString(JsonNode json) throws ProcessingException {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonSchema schema = factory.getJsonSchema(responseSchema);
        ProcessingReport report = schema.validate(json);
        return report.isSuccess();
    }
}
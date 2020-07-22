package org.oleggalimov.dbmonitoring.back.tests.integration.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.oleggalimov.dbmonitoring.back.tests.TestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = TestContext.class)
// обертка для @WebAppConfiguration @ExtendWith(SpringExtension.class)
class CheckStatusControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Tag("Rest")
    @Test
    void statusGet() throws Exception {
        mockMvc
                .perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Back is Ok! ActiveProfiles: []"));
    }
}
package rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
  Based on https://www.baeldung.com/integration-testing-in-spring
 */
/*
 * @ExtendWith используется вместо @RunWith(SpringJUnit4ClassRunner.class) - для интеграции тестового контекста
 с другими платформами или для изменения общего потока выполнения в тестовых примерах
 плюс можно подключать свои расширения см. тут https://www.codeflow.site/ru/article/junit-5-migration
 */
@ExtendWith(SpringExtension.class)

/*
    @WebAppConfiguration will load the web application context.
    By default, it looks for the root web application at default path src/main/webapp;
    the location can be overridden by passing value argument as:
    @WebAppConfiguration(value = "")
 */

@WebAppConfiguration
/*
    to load the context configuration and bootstrap the context that the test will use.
 */
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
public class StatusTest {
    /*
        MockMvc provides support for Spring MVC testing.
        It encapsulates all web application beans and make them available for testing.
     */
    private MockMvc mockMvc;
    /*
        WebApplicationContext (wac) provides the web application configuration.
        It loads all the application beans and controllers into the context.
     */
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getStatus() {
        ServletContext servletContext = wac.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("status"));
    }

    /*
    Let's break that down:
        * perform() method will call a get request method which returns the ResultActions. Using this result we can have assertion expectations on response like content, HTTP status, header, etc
        * andDo(print()) will print the request and response. This is helpful to get detailed view in case of error
        * andExpect() will expect the provided argument. In our case we are expecting “index” to be returned via MockMvcResultMatchers.view()
     */
    @Tag("Rest")
    @Test
    public void statusControllerTest() throws Exception {
        this.mockMvc
                .perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Back is Ok!"));
    }

//    @Tag("Rest")
//    @Test
//    public void postControllerTest() throws Exception {
//        this.mockMvc.perform(post("/greetWithPostAndFormData").param("id", "1")
//                .param("name", "John Doe")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.message").value("Hello World John Doe!!!"))
//                .andExpect(jsonPath("$.id").value(1));
//    }
}
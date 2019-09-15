package org.oleggalimov.dbmonitoring.back.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@Configuration
@ComponentScan(basePackages = {"org.oleggalimov.dbmonitoring.back"})
public class MainConfiguration implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext){
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setConfigLocation("org.oleggalimov.dbmonitoring.back.configuration");
        servletContext.addListener(new ContextLoaderListener(applicationContext));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(applicationContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }
}

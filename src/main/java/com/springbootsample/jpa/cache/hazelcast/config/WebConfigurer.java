package com.springbootsample.jpa.cache.hazelcast.config;

import com.hazelcast.core.HazelcastInstance;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

import javax.servlet.*;
import java.util.EnumSet;

@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final JHipsterProperties jHipsterProperties;

    private final HazelcastInstance hazelcastInstance;


    public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties, HazelcastInstance hazelcastInstance) {

        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
        this.hazelcastInstance = hazelcastInstance;
    }

    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
            initH2Console(servletContext);
        }
        log.info("Web application fully configured");
    }

    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
        mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
        container.setMimeMappings(mappings);
    }

    /**
     * Initializes H2 console.
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> servletClass = Class.forName("org.h2.server.web.WebServlet", true, loader);
            Servlet servlet = (Servlet) servletClass.newInstance();
            ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", servlet);
            h2ConsoleServlet.addMapping("/h2-console/*");
            h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
            h2ConsoleServlet.setLoadOnStartup(1);
        } catch (ClassNotFoundException | LinkageError e) {
            throw new RuntimeException("Failed to load and initialize org.h2.server.web.WebServlet", e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to instantiate org.h2.server.web.WebServlet", e);
        }
    }

}

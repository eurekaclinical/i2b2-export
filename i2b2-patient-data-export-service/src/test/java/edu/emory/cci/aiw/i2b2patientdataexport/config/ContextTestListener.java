package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContextEvent;

public class ContextTestListener extends GuiceServletContextListener {

    private final Injector injector = Guice.createInjector(new ServletTestModule(), new AppTestModule());
    private PersistService persistSerivce = null;

    @Override
    protected Injector getInjector() {
        return this.injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        this.persistSerivce = this.getInjector().getInstance(PersistService.class);
        this.persistSerivce.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        this.persistSerivce.stop();
    }
}

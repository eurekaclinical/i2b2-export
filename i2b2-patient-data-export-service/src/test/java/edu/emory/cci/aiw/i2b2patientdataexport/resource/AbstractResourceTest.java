package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import edu.emory.cci.aiw.i2b2patientdataexport.config.AppTestModule;
import edu.emory.cci.aiw.i2b2patientdataexport.config.ContextTestListener;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

public abstract class AbstractResourceTest extends JerseyTest {

    private final Injector injector;
    private PersistService persistService;

    protected AbstractResourceTest() {
        super();
        this.injector = Guice.createInjector(this.getModules());
    }

    @Override
    protected AppDescriptor configure() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        return new WebAppDescriptor.Builder().
                contextListenerClass(getListener()).
                filterClass(getFilter()).
                clientConfig(clientConfig).
                build();
    }

    @Override
    public final void setUp() throws Exception {
        super.setUp();
        this.persistService = this.injector.getInstance(PersistService.class);
        this.persistService.start();
    }

    @Override
    public final void tearDown() throws Exception {
        this.persistService.stop();
        this.persistService = null;
        super.tearDown();
    }

    protected final <T> T getInstance(Class<T> className) {
        return this.injector.getInstance(className);
    }

    protected final Class<? extends ServletContextListener> getListener() {
        return ContextTestListener.class;
    }

    protected Class<? extends Filter> getFilter() {
        return GuiceFilter.class;
    }

    protected Module[] getModules() {
        return new Module[]{ new AppTestModule() };
    }
}

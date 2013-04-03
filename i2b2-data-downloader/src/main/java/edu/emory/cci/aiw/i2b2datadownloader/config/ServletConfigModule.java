package edu.emory.cci.aiw.i2b2datadownloader.config;

import com.google.inject.persist.PersistFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

public class ServletConfigModule extends JerseyServletModule {

	@Override
	protected void configureServlets() {
		filter("/rest/*").through(PersistFilter.class);

		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
				"edu.emory.cci.aiw.i2b2datadownloader.resource");
		serve("/rest/*").with(GuiceContainer.class, params);
	}
}

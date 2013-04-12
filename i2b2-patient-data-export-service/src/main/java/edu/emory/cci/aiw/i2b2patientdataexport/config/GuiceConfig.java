package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public final class GuiceConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JpaPersistModule
				("i2b2-patient-data-export-persist"), new ServletConfigModule(),
				new GuiceConfigModule());
	}
}

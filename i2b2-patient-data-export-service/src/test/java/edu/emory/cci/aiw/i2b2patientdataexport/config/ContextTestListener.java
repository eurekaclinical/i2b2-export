package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ContextTestListener extends GuiceServletContextListener {

	private final Injector injector = Guice.createInjector(new ServletTestModule(), new AppTestModule());

	@Override
	protected Injector getInjector() {
		return this.injector;
	}
}

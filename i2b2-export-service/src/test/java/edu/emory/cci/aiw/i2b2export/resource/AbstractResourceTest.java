package edu.emory.cci.aiw.i2b2export.resource;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import edu.emory.cci.aiw.i2b2export.config.AppTestModule;
import edu.emory.cci.aiw.i2b2export.config.ContextTestListener;

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
		return new Module[]{new AppTestModule()};
	}
}

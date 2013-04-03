package edu.emory.cci.aiw.i2b2datadownloader.config;

import com.google.inject.AbstractModule;
import edu.emory.cci.aiw.i2b2datadownloader.dao.JpaOutputConfigurationDao;
import edu.emory.cci.aiw.i2b2datadownloader.dao.OutputConfigurationDao;

public class GuiceConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(OutputConfigurationDao.class).to(JpaOutputConfigurationDao.class);
	}
}

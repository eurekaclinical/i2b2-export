package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.AbstractModule;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.JpaOutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.JpaOutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticatorImpl;


public class GuiceConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(OutputConfigurationDao.class).to(JpaOutputConfigurationDao.class);
        bind(OutputColumnConfigurationDao.class).to(JpaOutputColumnConfigurationDao.class);
		bind(I2b2UserAuthenticator.class).to(I2b2UserAuthenticatorImpl.class);
	}
}

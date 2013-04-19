package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.MockOutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.MockOutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.MockI2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.MockI2b2UserAuthenticator;

public class AppTestModule extends AbstractModule {

    @Override
    protected void configure() {
		install(new JpaPersistModule("i2b2-patient-data-export-persist"));
        bind(OutputConfigurationDao.class).to(MockOutputConfigurationDao.class);
        bind(OutputColumnConfigurationDao.class).to(MockOutputColumnConfigurationDao.class);
		bind(I2b2UserAuthenticator.class).to(MockI2b2UserAuthenticator.class);
		bind(I2b2PdoRetriever.class).to(MockI2b2PdoRetriever.class);
    }
}

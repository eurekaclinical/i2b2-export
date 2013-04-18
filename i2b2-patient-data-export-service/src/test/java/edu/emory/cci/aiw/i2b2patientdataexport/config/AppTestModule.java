package edu.emory.cci.aiw.i2b2patientdataexport.config;

import com.google.inject.AbstractModule;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.JpaOutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.JpaOutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputColumnConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;

public class AppTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OutputConfigurationDao.class).to(JpaOutputConfigurationDao.class);
        bind(OutputColumnConfigurationDao.class).to(JpaOutputColumnConfigurationDao.class);
    }
}

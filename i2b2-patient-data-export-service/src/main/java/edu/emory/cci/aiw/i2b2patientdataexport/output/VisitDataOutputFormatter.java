package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Patient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class VisitDataOutputFormatter {
	private final OutputConfiguration config;
	private final Collection<Patient> patients;

	public VisitDataOutputFormatter(OutputConfiguration config, Collection<Patient> patients) {
		this.config = config;
		this.patients = patients;
	}

	public List<String[]> format() {
//		List<String> result = new ArrayList<String>();
        List<String[]> result = new ArrayList<String[]>();

		for (Patient patient : this.patients) {
			for (Event visit : patient.getEvents()) {
				result.add(new VisitDataRowOutputFormatter(this.config, visit).format());
			}
		}

//		return StringUtils.join(result, "\n");
        return result;
	}
}

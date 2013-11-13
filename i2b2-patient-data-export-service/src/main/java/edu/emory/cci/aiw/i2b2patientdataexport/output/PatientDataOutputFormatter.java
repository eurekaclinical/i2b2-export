package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class PatientDataOutputFormatter {

	private final OutputConfiguration config;
	private final Collection<Patient> patients;

	public PatientDataOutputFormatter(OutputConfiguration config, Collection<Patient> patients) {
		this.config = config;
		this.patients = patients;
	}

	public List<String[]> format() {
		List<String[]> result = new ArrayList<String[]>();

		for (Patient patient : this.patients) {
			result.add(new PatientDataRowOutputFormatter(this.config, patient).format());
		}

		return result;
	}
}

package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;
import org.apache.commons.lang3.StringUtils;

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

    public String format() {
        List<String> result = new ArrayList<String>();
        FormatOptions formatOptions = new FormatOptions(this.config);

        for (Patient patient : this.patients) {
            result.add(new PatientDataRowOutputFormatter(this.config, patient).format());
        }

        return StringUtils.join(result, "\n");
    }
}

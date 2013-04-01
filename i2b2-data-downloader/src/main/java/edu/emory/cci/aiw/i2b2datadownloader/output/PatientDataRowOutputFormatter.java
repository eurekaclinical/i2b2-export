package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;

import java.util.ArrayList;
import java.util.Collection;

final class PatientDataRowOutputFormatter extends DataRowOutputFormatter {
    private final Patient patient;

    public PatientDataRowOutputFormatter(OutputConfiguration config, Patient patient) {
        super(config);
        this.patient = patient;
    }

    @Override
    protected String rowPrefix() {
        return patient.getPatientId();
    }

    @Override
    protected Collection<Observation> matchingObservations(String i2b2Concept) {
        Collection<Observation> result = new ArrayList<Observation>();

        for (Event e : patient.getEvents()) {
            for (Observation o : e.getObservations()) {
                if (o.getConcept().equals(i2b2Concept)) {
                    result.add(o);
                }
            }
        }

        return result;
    }
}
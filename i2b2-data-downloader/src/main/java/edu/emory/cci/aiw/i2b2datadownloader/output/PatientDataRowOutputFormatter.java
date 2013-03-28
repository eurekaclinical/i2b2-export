package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PatientDataRowOutputFormatter extends DataRowOutputFormatter {
    private final Patient patient;

    public PatientDataRowOutputFormatter(OutputConfiguration config, Patient patient) {
        super(config);
        this.patient = patient;
    }

    @Override
    public String format() {
        List<String> result = new ArrayList<String>();

        result.add(patient.getPatientId());
        for (OutputColumnConfiguration colConfig : getConfig().getColumnConfigs()) {
            Collection<Observation> obxs = matchingObservations(colConfig.getI2b2ConceptPath());
            switch (colConfig.getDisplayFormat()) {
                case EXISTENCE:
                    result.add(new ExistenceColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                case VALUE:
                    result.add(new ValueColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                case AGGREGATION:
                    result.add(new AggregationColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                default:
                    throw new RuntimeException("display format not provided");
            }
        }

        return StringUtils.join(result, getConfig().getSeparator());
    }

    private Collection<Observation> matchingObservations(String i2b2Concept) {
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

package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class ProviderDataRowOutputFormatter extends DataRowOutputFormatter {
	private final Observer provider;

	public ProviderDataRowOutputFormatter(OutputConfiguration config, Observer provider) {
		super(config);
		this.provider = provider;
	}

	@Override
	protected List<String> rowPrefix() {
		List<String> result = new ArrayList<String>();
		result.add(provider.getName());
        return result;
//		return StringUtils.join(result, getConfig().getSeparator());
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2Concept i2b2Concept) {
		Collection<Observation> result = new ArrayList<Observation>();

		for (Observation o : provider.getObservations()) {
			if (o.getConceptPath().equals(i2b2Concept.getI2b2Key())) {
				result.add(o);
			}
		}

		return result;
	}
}

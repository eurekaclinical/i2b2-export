package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class ExistenceColumnOutputFormatter extends
		AbstractColumnOutputFormatter {

	public ExistenceColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public List<String> format(Collection<Observation> data) {
		if (null == data || data.isEmpty()) {
			return Collections.singletonList("false");
        } else {
			return Collections.singletonList("true");
		}
	}
}

package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class ProviderDataOutputFormatter {

	private final OutputConfiguration config;
	private final Collection<Observer> providers;

	public ProviderDataOutputFormatter(OutputConfiguration config, Collection<Observer> providers) {
		this.config = config;
		this.providers = providers;
	}

	public List<String[]> format() {
//		List<String> result = new ArrayList<String>();
        List<String[]> result = new ArrayList<String[]>();

		for (Observer provider : this.providers) {
			result.add(new ProviderDataRowOutputFormatter(this.config, provider).format());
		}

//		return StringUtils.join(result, "\n");
        return result;
	}
}

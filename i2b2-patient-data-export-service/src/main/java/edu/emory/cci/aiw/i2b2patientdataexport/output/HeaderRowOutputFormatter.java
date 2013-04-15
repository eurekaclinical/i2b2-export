package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HeaderRowOutputFormatter {
	private final OutputConfiguration outputConfiguration;

	public HeaderRowOutputFormatter(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}

	public String formatHeader() {
		List<String> result = new ArrayList<String>();
		switch (outputConfiguration.getRowDimension()) {
			case PROVIDER:
				result.add("Provider_name");
				break;
			case VISIT:
				result.add("Visit_id");
				result.add("Visit_start");
				result.add("Visit_end");
				// fall through
			case PATIENT:
				result.add(0, "Patient_id"); // patient ID always goes first
				break;
			default:
				throw new RuntimeException("row dimension not provided: user:" +
						" " + outputConfiguration.getUsername() + ", " +
						"name: " + outputConfiguration.getName());
		}
		Collections.sort(outputConfiguration.getColumnConfigs());
		for (int i = 0; i < outputConfiguration.getColumnConfigs().size(); i++) {
			OutputColumnConfiguration colConfig = outputConfiguration
					.getColumnConfigs().get(i);
            String baseColName = colConfig.getColumnName();
            if (outputConfiguration.getWhitespaceReplacement() != null && !outputConfiguration.getWhitespaceReplacement().equals("")) {
                baseColName = colConfig.getColumnName().replaceAll("\\s",
                        outputConfiguration.getWhitespaceReplacement());
            }
			switch (colConfig.getDisplayFormat()) {
				case EXISTENCE:
					result.add(baseColName);
					break;
				case VALUE:
					for (int j = 0; j < colConfig.getHowMany(); j++) {
						result.add(baseColName + "_value");
						if (colConfig.getIncludeUnits()) {
							result.add(baseColName + "_units");
						}
						if (colConfig.getIncludeTimeRange()) {
							result.add(baseColName + "_start");
							result.add(baseColName + "_end");
						}
					}
					break;
				case AGGREGATION:
					switch (colConfig.getAggregation()) {
						case MIN:
							result.add(baseColName + "_min");
							break;
						case MAX:
							result.add(baseColName + "_max");
							break;
						case AVG:
							result.add(baseColName + "_avg");
							break;
						default:
							continue;
					}
					if (colConfig.getIncludeUnits()) {
						result.add(baseColName + "_units");
					}
					break;
				default:
					break;
			}
		}
		return StringUtils.join(result, outputConfiguration.getSeparator());
	}
}

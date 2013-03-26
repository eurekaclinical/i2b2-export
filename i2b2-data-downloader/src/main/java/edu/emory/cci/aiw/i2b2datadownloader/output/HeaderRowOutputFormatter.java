package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class HeaderRowOutputFormatter {
	private final OutputConfiguration outputConfiguration;

	HeaderRowOutputFormatter(OutputConfiguration outputConfiguration) {
		this.outputConfiguration = outputConfiguration;
	}

	public String formatHeader() {
		List<String> result = new ArrayList<String>();
		Collections.sort(outputConfiguration.getColumnConfigs());
		for (int i = 0; i < outputConfiguration.getColumnConfigs().size() -
				1; i++) {
			OutputColumnConfiguration colConfig = outputConfiguration
					.getColumnConfigs().get(i);
			String baseColName = colConfig.getColumnName().replaceAll("\\s",
					outputConfiguration.getWhitespaceReplacement());
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

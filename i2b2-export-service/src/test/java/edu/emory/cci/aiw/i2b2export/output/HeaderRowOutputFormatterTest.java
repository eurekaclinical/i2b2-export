package edu.emory.cci.aiw.i2b2export.output;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import edu.emory.cci.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;

/**
 *
 */
public class HeaderRowOutputFormatterTest {

	@Test
	public void testFormatHeader() {
		OutputConfiguration config = new OutputConfiguration();
		config.setName("foo");
		config.setUsername("i2b2");
		config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
		config.setSeparator(",");
		config.setQuoteChar("\"");
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		OutputColumnConfiguration colConfig1 = new OutputColumnConfiguration();
		colConfig1.setOutputConfig(config);
		colConfig1.setColumnOrder(1);
		colConfig1.setColumnName("Concept FOO");
		colConfig1.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.EXISTENCE);

		OutputColumnConfiguration colConfig2 = new OutputColumnConfiguration();
		colConfig2.setOutputConfig(config);
		colConfig2.setColumnOrder(2);
		colConfig2.setColumnName("Concept BAR 1");
		colConfig2.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig2.setHowMany(3);
		colConfig2.setIncludeTimeRange(true);
		colConfig2.setIncludeUnits(true);

		OutputColumnConfiguration colConfig3 = new OutputColumnConfiguration();
		colConfig3.setOutputConfig(config);
		colConfig3.setColumnOrder(3);
		colConfig3.setColumnName("ConceptAgg");
		colConfig3.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig3.setIncludeUnits(true);

		OutputColumnConfiguration colConfig4 = new OutputColumnConfiguration();
		colConfig4.setOutputConfig(config);
		colConfig4.setColumnOrder(4);
		colConfig4.setColumnName("Concept BAZ");
		colConfig4.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig4.setHowMany(1);
		colConfig4.setIncludeUnits(false);
		colConfig4.setIncludeTimeRange(false);

		OutputColumnConfiguration colConfig5 = new OutputColumnConfiguration();
		colConfig5.setOutputConfig(config);
		colConfig5.setColumnOrder(5);
		colConfig5.setColumnName("ConceptAgg2");
		colConfig5.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig5.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig5.setIncludeUnits(false);

		OutputColumnConfiguration colConfig6 = new OutputColumnConfiguration();
		colConfig6.setOutputConfig(config);
		colConfig6.setColumnOrder(6);
		colConfig6.setColumnName("Concept QUUX");
		colConfig6.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig6.setHowMany(1);
		colConfig6.setIncludeUnits(false);
		colConfig6.setIncludeTimeRange(true);

		config.getColumnConfigs().add(colConfig1);
		config.getColumnConfigs().add(colConfig3);
		config.getColumnConfigs().add(colConfig4);
		config.getColumnConfigs().add(colConfig5);
		config.getColumnConfigs().add(colConfig6);
		config.getColumnConfigs().add(colConfig2);

		HeaderRowOutputFormatter formatter = new HeaderRowOutputFormatter(config);
		Assert.assertEquals("Patient_id,Concept_FOO,Concept_BAR_1_value,Concept_BAR_1_units,Concept_BAR_1_start,Concept_BAR_1_end,Concept_BAR_1_value,Concept_BAR_1_units,Concept_BAR_1_start,Concept_BAR_1_end,Concept_BAR_1_value,Concept_BAR_1_units,Concept_BAR_1_start,Concept_BAR_1_end," +
				"ConceptAgg_max,ConceptAgg_units,Concept_BAZ_value,ConceptAgg2_avg,Concept_QUUX_value,Concept_QUUX_start,Concept_QUUX_end", StringUtils.join(formatter.formatHeader(), ','));
	}
}

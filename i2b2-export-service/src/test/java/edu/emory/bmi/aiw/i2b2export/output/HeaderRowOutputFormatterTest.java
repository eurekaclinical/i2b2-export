package edu.emory.bmi.aiw.i2b2export.output;

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

import edu.emory.bmi.aiw.i2b2export.entity.AggregationType;
import edu.emory.bmi.aiw.i2b2export.entity.DisplayFormat;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfigurationEntity;
import edu.emory.bmi.aiw.i2b2export.entity.RowDimension;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class HeaderRowOutputFormatterTest extends AbstractRowOutputFormatterTest {

	@Test
	public void testFormatHeader() throws IOException, SQLException {
		OutputConfigurationEntity config = new OutputConfigurationEntity();
		config.setName("foo");
		config.setUsername("i2b2");
		config.setRowDimension(RowDimension.PATIENT);
		config.setSeparator(",");
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfigurationEntity>());

		OutputColumnConfigurationEntity colConfig1 = new OutputColumnConfigurationEntity();
		colConfig1.setOutputConfig(config);
		colConfig1.setColumnOrder(1);
		colConfig1.setColumnName("Concept FOO");
		colConfig1.setDisplayFormat(DisplayFormat.EXISTENCE);

		OutputColumnConfigurationEntity colConfig2 = new OutputColumnConfigurationEntity();
		colConfig2.setOutputConfig(config);
		colConfig2.setColumnOrder(2);
		colConfig2.setColumnName("Concept BAR 1");
		colConfig2.setDisplayFormat(DisplayFormat.VALUE);
		colConfig2.setHowMany(3);
		colConfig2.setIncludeTimeRange(true);
		colConfig2.setIncludeUnits(true);

		OutputColumnConfigurationEntity colConfig3 = new OutputColumnConfigurationEntity();
		colConfig3.setOutputConfig(config);
		colConfig3.setColumnOrder(3);
		colConfig3.setColumnName("ConceptAgg");
		colConfig3.setDisplayFormat(DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(AggregationType.MAX);
		colConfig3.setIncludeUnits(true);

		OutputColumnConfigurationEntity colConfig4 = new OutputColumnConfigurationEntity();
		colConfig4.setOutputConfig(config);
		colConfig4.setColumnOrder(4);
		colConfig4.setColumnName("Concept BAZ");
		colConfig4.setDisplayFormat(DisplayFormat.VALUE);
		colConfig4.setHowMany(1);
		colConfig4.setIncludeUnits(false);
		colConfig4.setIncludeTimeRange(false);

		OutputColumnConfigurationEntity colConfig5 = new OutputColumnConfigurationEntity();
		colConfig5.setOutputConfig(config);
		colConfig5.setColumnOrder(5);
		colConfig5.setColumnName("ConceptAgg2");
		colConfig5.setDisplayFormat(DisplayFormat.AGGREGATION);
		colConfig5.setAggregation(AggregationType.AVG);
		colConfig5.setIncludeUnits(false);

		OutputColumnConfigurationEntity colConfig6 = new OutputColumnConfigurationEntity();
		colConfig6.setOutputConfig(config);
		colConfig6.setColumnOrder(6);
		colConfig6.setColumnName("Concept QUUX");
		colConfig6.setDisplayFormat(DisplayFormat.VALUE);
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
				"ConceptAgg_max,ConceptAgg_units,Concept_BAZ_value,ConceptAgg2_avg,Concept_QUUX_value,Concept_QUUX_start,Concept_QUUX_end" + IOUtils.LINE_SEPARATOR, formatString(formatter));
	}
}

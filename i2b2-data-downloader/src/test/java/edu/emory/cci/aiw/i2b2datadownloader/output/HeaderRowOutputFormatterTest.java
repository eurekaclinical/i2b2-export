package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import junit.framework.Assert;
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
		config.setMissingValue("(NULL)");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		OutputColumnConfiguration colConfig1 = new OutputColumnConfiguration();
		colConfig1.setColumnOrder(1);
		colConfig1.setColumnName("Concept FOO");
		colConfig1.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.EXISTENCE);

		OutputColumnConfiguration colConfig2 = new OutputColumnConfiguration();
		colConfig2.setColumnOrder(2);
		colConfig2.setColumnName("Concept BAR 1");
		colConfig2.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig2.setHowMany(3);
		colConfig2.setIncludeTimeRange(true);
		colConfig2.setIncludeUnits(true);

		OutputColumnConfiguration colConfig3 = new OutputColumnConfiguration();
		colConfig3.setColumnOrder(3);
		colConfig3.setColumnName("ConceptAgg");
		colConfig3.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig3.setIncludeUnits(true);

		OutputColumnConfiguration colConfig4 = new OutputColumnConfiguration();
		colConfig4.setColumnOrder(4);
		colConfig4.setColumnName("Concept BAZ");
		colConfig4.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig4.setHowMany(1);
		colConfig4.setIncludeUnits(false);
		colConfig4.setIncludeTimeRange(false);

		OutputColumnConfiguration colConfig5 = new OutputColumnConfiguration();
		colConfig5.setColumnOrder(5);
		colConfig5.setColumnName("ConceptAgg2");
		colConfig5.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig5.setAggregation(OutputColumnConfiguration.AggregationType.AVG);
		colConfig5.setIncludeUnits(false);

		OutputColumnConfiguration colConfig6 = new OutputColumnConfiguration();
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
				"ConceptAgg_max,ConceptAgg_units,Concept_BAZ_value,ConceptAgg2_avg,Concept_QUUX_value,Concept_QUUX_start,Concept_QUUX_end", formatter.formatHeader());
	}
}

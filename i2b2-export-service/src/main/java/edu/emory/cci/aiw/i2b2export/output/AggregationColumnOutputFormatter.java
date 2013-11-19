package edu.emory.cci.aiw.i2b2export.output;

/*
 * #%L
 * i2b2 Patient Data Export Service
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
import edu.emory.cci.aiw.i2b2export.i2b2.pdo.Observation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Column formatter for aggregation columns.
 *
 * @author Michel Mansour
 */
public final class AggregationColumnOutputFormatter extends AbstractColumnOutputFormatter {

	/**
	 * Default constructor
	 *
	 * @param columnConfig the configuration of the column to format
	 * @param formatOptions the global format options to apply to the column
	 */
	public AggregationColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public List<String> format(Collection<Observation> data) {
		List<String> result = new ArrayList<>();
		String units = "";

		if (data == null || data.isEmpty()) {
			result.add(getFormatOptions().getMissingData());
			units = getFormatOptions().getMissingData();
		} else {
			switch (getColumnConfig().getAggregation()) {
				case MIN:
					BigDecimal minValue = new BigDecimal(Double.MAX_VALUE);
					for (Observation obx : data) {
						BigDecimal value = new BigDecimal(obx.getNval());
						if (value.compareTo(minValue) < 0) {
							minValue = value;
							units = obx.getUnits();
						}
					}
					result.add(minValue.toString());
					break;
				case MAX:
					BigDecimal maxValue = new BigDecimal(Double.MIN_VALUE);
					for (Observation obx : data) {
						BigDecimal value = new BigDecimal(obx.getNval());
						if (value.compareTo(maxValue) > 0) {
							maxValue = value;
							units = obx.getUnits();
						}
					}
					result.add(maxValue.toString());
					break;
				case AVG:
					BigDecimal sum = new BigDecimal(0.0);
					int scale = 0;
					for (Observation obx : data) {
						BigDecimal value = new BigDecimal(obx.getNval());
						sum = sum.add(value);
						units = obx.getUnits();
						int dotIdx = obx.getNval().indexOf('.');
						if (dotIdx > -1) {
							int tempScale = obx.getNval().length() - obx.getNval()
									.indexOf('.');
							if (tempScale > scale) {
								scale = tempScale;
							}
						}
					}
					BigDecimal avg = sum.divide(new BigDecimal(data.size()),
							scale);
					result.add(avg.toString());
					break;
				default:
					throw new RuntimeException("aggregation type not provided");
			}
		}
		if (getColumnConfig().getIncludeUnits()) {
			result.add(units);
		}

		return result;
	}
}

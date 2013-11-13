package edu.emory.cci.aiw.i2b2patientdataexport.output;

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

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ValueColumnOutputFormatter extends AbstractColumnOutputFormatter {

	private final DateFormat i2b2DateFormat;

	public ValueColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
		i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
	}

	private static final Comparator<Observation> obxComp = new Comparator<Observation>() {

		@Override
		public int compare(Observation o1, Observation o2) {
			final int startComp = -1 * o1.getStartDate().compareTo(o2.getStartDate());
			if (startComp == 0) {
				return -1 * o1.getEndDate().compareTo(o2.getEndDate());
			}
			return startComp;
		}
	};

	@Override
	public List<String> format(Collection<Observation> data) {
		List<String> result = new ArrayList<String>();
		List<Observation> dataList = new ArrayList<Observation>(data);
		Collections.sort(dataList, obxComp);

		int numCols = 1;
		if (getColumnConfig().getIncludeUnits()) {
			numCols++;
		}
		if (getColumnConfig().getIncludeTimeRange()) {
			numCols += 2;
		}

		for (int i = 0; i < getColumnConfig().getHowMany(); i++) {
			if (null == dataList || dataList.isEmpty() || i >= dataList.size()) {
				for (int j = 0; j < numCols; j++) {
					result.add(getFormatOptions().getMissingData());
				}
			} else {
				final Observation obx = dataList.get(i);
				if (obx.getValuetype() != null && obx.getValuetype().equals
						("N")) {
					result.add(obx.getNval());
				} else {
					result.add(obx.getTval());
				}
				if (getColumnConfig().getIncludeUnits()) {
					result.add(obx.getUnits());
				}
				if (getColumnConfig().getIncludeTimeRange()) {
					result.add(i2b2DateFormat.format(obx.getStartDate()));
					result.add(i2b2DateFormat.format(obx.getEndDate()));
				}
			}
		}

		return result;
	}
}

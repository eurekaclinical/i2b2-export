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

import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;
import java.io.BufferedWriter;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Column formatter for value columns.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class ValueColumnOutputFormatter extends AbstractColumnOutputFormatter {

	/*
	 * the i2b2 date format
	 */
	private final DateFormat i2b2DateFormat;

	/**
	 * Default constructor.
	 *
	 * @param columnConfig the configuration of the column to format
	 * @param formatOptions the global format options to apply to the column
	 */
	ValueColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
		i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
	}

	private static final Comparator<Observation> OBX_COMP = new Comparator<Observation>() {

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
	public int format(Collection<Observation> data, BufferedWriter writer, int colNum) throws IOException {
		List<Observation> dataList = new ArrayList<>(data);
		Collections.sort(dataList, OBX_COMP);

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
					write(getFormatOptions().getMissingData(), writer, colNum++);
				}
			} else {
				final Observation obx = dataList.get(i);
				if (obx.getValuetype() != null && obx.getValuetype().equals("N")) {
					write(obx.getNval(), writer, colNum++);
				} else {
					write(obx.getTval(), writer, colNum++);
				}
				if (getColumnConfig().getIncludeUnits()) {
					write(obx.getUnits(), writer, colNum++);
				}
				if (getColumnConfig().getIncludeTimeRange()) {
					if (null != obx.getStartDate()) {
						write(i2b2DateFormat.format(obx.getStartDate()), writer, colNum++);
					} else {
						write("", writer, colNum++);
					}
					if (null != obx.getEndDate()) {
						write(i2b2DateFormat.format(obx.getEndDate()), writer, colNum++);
					} else {
						write("", writer, colNum++);
					}
				}
			}
		}
		return colNum;
	}
}

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
import edu.emory.bmi.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Event;
import edu.emory.bmi.aiw.i2b2export.i2b2.pdo.Observation;
import java.io.BufferedWriter;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Output formatter for visit rows.
 *
 * @author Michel Mansour
 * @since 1.0
 */
final class VisitDataRowOutputFormatter extends DataRowOutputFormatter {

	private final Event visit;
	private final Map<String, List<Observation>> keyToObx;
	private final DateFormat fmt;

	VisitDataRowOutputFormatter(OutputConfiguration config, Event visit) {
		super(config);
		this.visit = visit;
		this.keyToObx = new HashMap<>();
		for (Observation o : this.visit.getObservations()) {
			org.arp.javautil.collections.Collections.putList(this.keyToObx, o.getConceptPath(), o);
		}
		this.fmt = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
	}

	@Override
	public int rowPrefix(BufferedWriter writer) throws IOException {
		int colNum = 0;
		write(visit.getPatient().getPatientId(), writer, colNum++);
		write(visit.getEventId(), writer, colNum++);
		if (null != visit.getStartDate()) {
			synchronized (this.fmt) {
				write(fmt.format(visit.getStartDate()), writer, colNum++);
			}
		} else {
			write("", writer, colNum++);
		}
		if (null != visit.getEndDate()) {
			synchronized (this.fmt) {
				write(fmt.format(visit.getEndDate()), writer, colNum++);
			}
		} else {
			write("", writer, colNum++);
		}

		return colNum;
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2Concept i2b2Concept) {
		List<Observation> get = this.keyToObx.get(i2b2Concept.getI2b2Key());
		if (get != null) {
			return Collections.unmodifiableCollection(get);
		} else {
			return Collections.emptyList();
		}
	}
}

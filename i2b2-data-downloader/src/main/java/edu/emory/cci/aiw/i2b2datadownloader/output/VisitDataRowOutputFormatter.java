package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class VisitDataRowOutputFormatter extends DataRowOutputFormatter {
	private final Event visit;

	public VisitDataRowOutputFormatter(OutputConfiguration config, Event visit) {
		super(config);
		this.visit = visit;

	}

	@Override
	public String rowPrefix() {
		List<String> result = new ArrayList<String>();
		DateFormat fmt = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

		result.add(visit.getPatient().getPatientId());
		result.add(visit.getEventId());
		result.add(fmt.format(visit.getStartDate()));
		result.add(fmt.format(visit.getEndDate()));

		return StringUtils.join(result, getConfig().getSeparator());
	}

	@Override
	protected Collection<Observation> matchingObservations(I2b2Concept i2b2Concept) {
		Collection<Observation> result = new ArrayList<Observation>();

		for (Observation o : visit.getObservations()) {
			if (o.getConceptPath().equals(i2b2Concept.getKey())) {
				result.add(o);
			}
		}

		return result;
	}
}

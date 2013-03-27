package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;

import java.util.Collection;

/**
 *
 */
public class AggregationColumnOutputFormatter extends AbstractColumnOutputFormatter {

	public AggregationColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public String format(Collection<Observation> data) {
		return null;
    }
}

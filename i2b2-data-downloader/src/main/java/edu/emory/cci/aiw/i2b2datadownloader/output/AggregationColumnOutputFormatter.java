package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class AggregationColumnOutputFormatter extends AbstractColumnOutputFormatter {

	public AggregationColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public String format(Collection<Observation> data) {
        List<String> result = new ArrayList<String>();

        if (data == null || data.isEmpty()) {
            result.add(getFormatOptions().getMissingData());
            if (getColumnConfig().getIncludeUnits()) {
                result.add(getFormatOptions().getMissingData());
            }
        }
        switch (getColumnConfig().getAggregation()) {
            case MIN:
                break;
            case MAX:
                break;
            case AVG:
                break;
            default:
                throw new RuntimeException("aggregation type not provided");
        }

		return null;
    }
}

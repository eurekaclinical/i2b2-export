package edu.emory.cci.aiw.i2b2patientdataexport.output;

import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.Observation;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class AggregationColumnOutputFormatter extends AbstractColumnOutputFormatter {

	public AggregationColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		super(columnConfig, formatOptions);
	}

	@Override
	public List<String> format(Collection<Observation> data) {
		List<String> result = new ArrayList<String>();
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

//		return StringUtils.join(result, getFormatOptions().getColumnSeparator());
        return result;
	}
}

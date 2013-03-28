package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
	public String format(Collection<Observation> data) {
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
            if (dataList == null || dataList.isEmpty() || i >= dataList.size()) {
                for (int j = 0; j < numCols; j++) {
                    result.add(getFormatOptions().getMissingData());
                }
            } else {
                final Observation obx = dataList.get(i);
                result.add(obx.getTval());
                if (getColumnConfig().getIncludeUnits()) {
                    result.add(obx.getUnits());
                }
                if (getColumnConfig().getIncludeTimeRange()) {
                    result.add(i2b2DateFormat.format(obx.getStartDate()));
                    result.add(i2b2DateFormat.format(obx.getEndDate()));
                }
            }
        }

        return StringUtils.join(result, getFormatOptions().getColumnSeparator());
    }
}

package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DataRowOutputFormatter {

    private final OutputConfiguration config;
    private final FormatOptions formatOptions;

    protected DataRowOutputFormatter(OutputConfiguration config) {
        this.config = config;
        this.formatOptions = new FormatOptions(config);
    }

    protected final OutputConfiguration getConfig() {
        return config;
    }

    protected final FormatOptions getFormatOptions() {
        return formatOptions;
    }

    /**
     * Finds the observations that match the given i2b2 concept path.
     *
     * @param i2b2ConceptPath the i2b2 concept path to match
     *
     * @return a {@link Collection} of {@link Observation}s that match the given concept path
     */
    protected abstract Collection<Observation> matchingObservations(String i2b2ConceptPath);

    /**
     * Generates the first fields of the row that depend on the row dimension rather than the data,
     * for example, patient id, visit id, etc. The return value is expected to be already joined
     * by the column separator specified by the output configuration.
     *
     * @return the first fields of the row, as a {@link String}, joined by the column separator specified in the
     * output configuration
     */
    protected abstract String rowPrefix();

    public final String format() {
        List<String> result = new ArrayList<String>();

        result.add(rowPrefix());
        for (OutputColumnConfiguration colConfig : getConfig().getColumnConfigs()) {
            Collection<Observation> obxs = matchingObservations(colConfig
					.getI2b2Concept().getKey());
            switch (colConfig.getDisplayFormat()) {
                case EXISTENCE:
                    result.add(new ExistenceColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                case VALUE:
                    result.add(new ValueColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                case AGGREGATION:
                    result.add(new AggregationColumnOutputFormatter(colConfig, getFormatOptions()).format(obxs));
                    break;
                default:
                    throw new RuntimeException("display format not provided");
            }
        }

        return StringUtils.join(result, getConfig().getSeparator());
    }
}

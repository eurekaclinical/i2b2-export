package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;

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

    public abstract String format();
}

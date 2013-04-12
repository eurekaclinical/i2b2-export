package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;

/**
 *
 */
public abstract class AbstractColumnOutputFormatter implements
		ColumnOutputFormatter {
	private final OutputColumnConfiguration columnConfig;
	private final FormatOptions formatOptions;

	protected AbstractColumnOutputFormatter(OutputColumnConfiguration columnConfig, FormatOptions formatOptions) {
		this.columnConfig = columnConfig;
		this.formatOptions = formatOptions;
	}

	protected OutputColumnConfiguration getColumnConfig() {
		return columnConfig;
	}

	protected FormatOptions getFormatOptions() {
		return formatOptions;
	}

}

package edu.emory.cci.aiw.i2b2datadownloader.output;

/**
 *
 */
public abstract class AbstractColumnOutputFormatter implements
		ColumnOutputFormatter {
	private final FormatOptions formatOptions;

	protected AbstractColumnOutputFormatter(FormatOptions formatOptions) {
		this.formatOptions = formatOptions;
	}

	protected FormatOptions getFormatOptions() {
		return formatOptions;
	}

}

package edu.emory.cci.aiw.i2b2datadownloader.output;

/**
 *
 */
public final class FormatOptions {
	private final String columnSeparator;

	public FormatOptions(String columnSeparator,
						 String whitespaceReplacement) {
		this.columnSeparator = columnSeparator;
	}

	public String getColumnSeparator() {
		return columnSeparator;
	}
}

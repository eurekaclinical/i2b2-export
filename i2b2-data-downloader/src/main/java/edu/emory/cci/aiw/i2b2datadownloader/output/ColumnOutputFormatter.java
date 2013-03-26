package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;

/**
 *
 */
public interface ColumnOutputFormatter {

	/**
	 * Generates output for patient data based on a column configuration
	 *
	 * @return The output
	 */
	public String format(Observation data);
}

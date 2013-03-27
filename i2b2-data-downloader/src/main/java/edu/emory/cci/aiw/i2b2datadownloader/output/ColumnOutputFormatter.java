package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;

import java.util.Collection;

/**
 *
 */
public interface ColumnOutputFormatter {

    /**
     * Generates output for patient data based on a column configuration.
     *
     * @param data The observations to format. This collection should include all of the observations matching a particular i2b2 concept.
     *
     * @return The formatted output
     */
    public String format(Collection<Observation> data);
}

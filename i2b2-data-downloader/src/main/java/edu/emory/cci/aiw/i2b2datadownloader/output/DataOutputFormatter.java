package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.I2b2PdoResults;

public final class DataOutputFormatter {

    private final OutputConfiguration config;
    private final I2b2PdoResults pdoResults;

    public DataOutputFormatter(OutputConfiguration config, I2b2PdoResults pdoResults) {
        this.config = config;
        this.pdoResults = pdoResults;
    }

    public String format() {
        StringBuilder result = new StringBuilder(new HeaderRowOutputFormatter(config).formatHeader());
        result.append("\n");
        switch (this.config.getRowDimension()) {
            case PATIENT:
                result.append(new PatientDataOutputFormatter(this.config, pdoResults.getPatients()).format());
                break;
            case VISIT:
                result.append(new VisitDataOutputFormatter(this.config, pdoResults.getPatients()).format());
                break;
            case PROVIDER:
                result.append(new ProviderDataOutputFormatter(this.config, pdoResults.getObservers()).format());
                break;
            default:
                throw new AssertionError("no row dimension provided");
        }
        return result.toString();
    }
}

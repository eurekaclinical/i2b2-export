package edu.emory.cci.aiw.i2b2patientdataexport.output;

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo.I2b2PdoResults;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class DataOutputFormatter {

	private final OutputConfiguration config;
	private final I2b2PdoResults pdoResults;

	public DataOutputFormatter(OutputConfiguration config, I2b2PdoResults pdoResults) {
		this.config = config;
		this.pdoResults = pdoResults;
	}

	public String format() {
        List<String[]> result = new ArrayList<String[]>();
//		StringBuilder result =
                result.add(new HeaderRowOutputFormatter(config).formatHeader());
//		result.append("\n");
		switch (this.config.getRowDimension()) {
			case PATIENT:
//				result.append
                result.addAll(new PatientDataOutputFormatter(this.config, pdoResults.getPatients()).format());
				break;
			case VISIT:
//				result.append
                result.addAll(new VisitDataOutputFormatter(this.config, pdoResults.getPatients()).format());
				break;
			case PROVIDER:
//				result.append
                result.addAll(new ProviderDataOutputFormatter(this.config, pdoResults.getObservers()).format());
				break;
			default:
				throw new AssertionError("no row dimension provided");
		}
//		return result.toString();
        StringWriter resultStr = new StringWriter();
        CSVWriter writer;
        if (null == config.getQuoteChar() || config.getQuoteChar().isEmpty()) {
            writer = new CSVWriter(resultStr, config.getSeparator().charAt(0));
        } else {
            writer = new CSVWriter(resultStr, config.getSeparator().charAt(0), config.getQuoteChar().charAt(0));
        }
        writer.writeAll(result);

        return resultStr.toString();
	}
}

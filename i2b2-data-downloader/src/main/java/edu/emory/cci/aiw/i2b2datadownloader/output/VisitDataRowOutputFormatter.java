package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;

public final class VisitDataRowOutputFormatter extends DataRowOutputFormatter {
    private final Event visit;

    public VisitDataRowOutputFormatter(OutputConfiguration config, Event visit) {
        super(config);
        this.visit = visit;

    }

    @Override
    public String format() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

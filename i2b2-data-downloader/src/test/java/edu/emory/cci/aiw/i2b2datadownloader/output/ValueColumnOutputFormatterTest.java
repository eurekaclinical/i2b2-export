package edu.emory.cci.aiw.i2b2datadownloader.output;

import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Event;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Observation;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo.Patient;
import junit.framework.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class ValueColumnOutputFormatterTest {

    private OutputConfiguration config;
    private OutputColumnConfiguration colConfig;
    private FormatOptions formatOptions;
    private Collection<Observation> obxs = new ArrayList<Observation>();

    public ValueColumnOutputFormatterTest() throws ParseException {
        final DateFormat i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);

        colConfig = new OutputColumnConfiguration();
        colConfig.setOrder(0);
        colConfig.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);

        config = new OutputConfiguration();
        config.setName("foo");
        config.setUserId(1L);
        config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
        config.setSeparator(",");
        config.setMissingValue("(NULL)");
        config.setWhitespaceReplacement("_");
        config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

        formatOptions = new FormatOptions(config);

        Patient p = new Patient.Builder("1").build();
        Event e = new Event.Builder("1", p).build();

        obxs.add(new Observation.Builder(e).concept("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-01-01T09:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-01-01T10:00:00.000-0500")).tval("100").units("U").build());
        obxs.add(new Observation.Builder(e).concept("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-02-02T10:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-02-02T11:00:00.000-0500")).tval("200").units("V").build());
        obxs.add(new Observation.Builder(e).concept("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-03-03T11:00:00.000-0500")).endDate(i2b2DateFormat.parse("2013-03-03T12:00:00.000-0500")).tval("300").units("W").build());
        obxs.add(new Observation.Builder(e).concept("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-04-04T12:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-04-04T13:00:00.000-0400")).tval("400").units("X").build());
        obxs.add(new Observation.Builder(e).concept("\\\\i2b2\\Concepts\\MyConcept").startDate(i2b2DateFormat.parse("2013-05-05T13:00:00.000-0400")).endDate(i2b2DateFormat.parse("2013-05-05T14:00:00.000-0400")).tval("500").units("Y").build());
    }

    @Test
    public void testFormatSimple() {
        colConfig.setHowMany(1);
        colConfig.setIncludeUnits(false);
        colConfig.setIncludeTimeRange(false);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500", formatter.format(obxs));
    }

    @Test
    public void testFormatUnits() {
        colConfig.setHowMany(1);
        colConfig.setIncludeUnits(true);
        colConfig.setIncludeTimeRange(false);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,Y", formatter.format(obxs));
    }

    @Test
    public void testFormatDateRange() {
        colConfig.setHowMany(1);
        colConfig.setIncludeUnits(false);
        colConfig.setIncludeTimeRange(true);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,2013-05-05T13:00:00.000-0400,2013-05-05T14:00:00.000-0400", formatter.format(obxs));
    }

    @Test
    public void testFormatUnitsDateRange() {
        colConfig.setHowMany(1);
        colConfig.setIncludeUnits(true);
        colConfig.setIncludeTimeRange(true);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,Y,2013-05-05T13:00:00.000-0400,2013-05-05T14:00:00.000-0400", formatter.format(obxs));
    }

    @Test
    public void testFormatMultEqual() {
        colConfig.setHowMany(5);
        colConfig.setIncludeUnits(false);
        colConfig.setIncludeTimeRange(false);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,400,300,200,100", formatter.format(obxs));
    }

    @Test
    public void testFormatMultLess() {
        colConfig.setHowMany(3);
        colConfig.setIncludeUnits(false);
        colConfig.setIncludeTimeRange(false);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,400,300", formatter.format(obxs));
    }

    @Test
    public void testFormatMultMore() {
        colConfig.setHowMany(7);
        colConfig.setIncludeUnits(false);
        colConfig.setIncludeTimeRange(false);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,400,300,200,100,(NULL),(NULL)", formatter.format(obxs));
    }

    @Test
    public void testFormatMultUnitsDateRange() {
        colConfig.setHowMany(7);
        colConfig.setIncludeUnits(true);
        colConfig.setIncludeTimeRange(true);

        ValueColumnOutputFormatter formatter = new ValueColumnOutputFormatter(colConfig, formatOptions);

        Assert.assertEquals("500,Y,2013-05-05T13:00:00.000-0400,2013-05-05T14:00:00.000-0400,400,X,2013-04-04T12:00:00.000-0400,2013-04-04T13:00:00.000-0400,300,W,2013-03-03T11:00:00.000-0500,2013-03-03T12:00:00.000-0500,200,V,2013-02-02T10:00:00.000-0500,2013-02-02T11:00:00.000-0500,100,U,2013-01-01T09:00:00.000-0500,2013-01-01T10:00:00.000-0500,(NULL),(NULL),(NULL),(NULL),(NULL),(NULL),(NULL),(NULL)", formatter.format(obxs));
    }
}

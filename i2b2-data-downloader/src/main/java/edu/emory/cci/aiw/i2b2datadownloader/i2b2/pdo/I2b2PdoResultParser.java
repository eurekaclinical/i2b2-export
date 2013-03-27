package edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo;

import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2CommUtil;
import edu.emory.cci.aiw.i2b2datadownloader.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class I2b2PdoResultParser {

    private final DateFormat i2b2DateFormat;

    private Map<String, Patient> patients;
    private Map<String, Event> events;
    private Set<Observation> observations;

    private Document d;

    private I2b2PdoResultParser() {
        i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
        patients = new HashMap<String, Patient>();
        events = new HashMap<String, Event>();
        observations = new HashSet<Observation>();
    }

    public static List<Patient> parse(Document xmlDoc) throws XPathExpressionException {
        I2b2PdoResultParser p = new I2b2PdoResultParser();
        p.d = xmlDoc;

        return p.parse();
    }

    private List<Patient> parse() throws XPathExpressionException {

        parsePatients();
        parseEvents();
        parseObservations();

        for (Observation o : observations) {
            o.getEvent().addObservation(o);
        }
        for (Event e : events.values()) {
            e.getPatient().addEvent(e);
        }
        for (Patient p : patients.values()) {
            p.sortEvents();
        }

        List<Patient> result = new ArrayList<Patient>(patients.values());
        Collections.sort(result);

        return result;
    }

    private NodeList pdoElement(String dataName)
            throws XPathExpressionException {
        return (NodeList) XmlUtil.evalXPath(d, "//" + dataName,
                XPathConstants.NODESET);
    }

    private void parsePatients() throws XPathExpressionException {
        NodeList patientElts = pdoElement("patient");
        for (int i = 0; i < patientElts.getLength(); i++) {
            Node child = patientElts.item(i);
            Patient p = parsePatient((Element) child);
            patients.put(p.getPatientId(), p);
        }
    }

    private void parseEvents() throws XPathExpressionException {
        NodeList eventElts = pdoElement("event");
        for (int i = 0; i < eventElts.getLength(); i++) {
            Node child = eventElts.item(i);
            Event e = parseEvent((Element) child);
            events.put(e.getEventId(), e);
        }
    }

    private void parseObservations() throws XPathExpressionException {
        NodeList obxElts = pdoElement("observation");
        for (int i = 0; i < obxElts.getLength(); i++) {
            Observation o = parseObservation((Element) obxElts.item(i));
            observations.add(o);
        }
    }

    private Patient parsePatient(Element patientXml) {
        String id = text(patientXml, "patient_id");
        NodeList params = patientXml.getElementsByTagName("param");
        Patient.Builder pb = new Patient.Builder(id);
        Map<String, String> pm = new CustomNullHashMap<String, String>("N/A");
        for (int i = 0; i < params.getLength(); i++) {
            Node paramNode = params.item(i);
            Node nameAttr = paramNode.getAttributes().getNamedItem("column");
            if (nameAttr != null) {
                if (paramNode.getChildNodes().getLength() > 0) {
                    pm.put(nameAttr.getNodeValue(), paramNode.getChildNodes()
                            .item(0).getNodeValue());
                } else {
                    pm.put(nameAttr.getNodeValue(), "");
                }
            }
        }
        return pb.ageInYears(pm.get("age_in_years_num"))
                .birthDate(pm.get("birth_date"))
                .language(pm.get("language_cd"))
                .maritalStatus(pm.get("marital_status_cd"))
                .race(pm.get("race_cd")).religion(pm.get("religion_cd"))
                .sex(pm.get("sex_cd"))
                .stateCityZip(pm.get("statecityzip_path_char"))
                .vitalStatus(pm.get("vital_status_cd"))
                .zipCode(pm.get("zipcode_char")).build();
    }

    private Event parseEvent(Element eventXml) {
        String id = text(eventXml, "event_id");
        String patientId = text(eventXml, "patient_id");
        Event.Builder eb = new Event.Builder(id, this.patients.get(patientId))
                .startDate(date(eventXml, "start_date")).endDate(
                        date(eventXml, "end_date"));
        NodeList params = eventXml.getElementsByTagName("param");
        Map<String, String> pm = new CustomNullHashMap<String, String>("N/A");
        for (int i = 0; i < params.getLength(); i++) {
            Node paramNode = params.item(i);
            Node nameAttr = paramNode.getAttributes().getNamedItem("column");
            if (nameAttr != null) {
                pm.put(nameAttr.getNodeValue(), paramNode.getTextContent());
            }
        }

        return eb.activeStatus(pm.get("active_status_cd"))
                .inOut(pm.get("inout_cd")).location(pm.get("location_cd"))
                .build();
    }

    private Observation parseObservation(Element obxXml) {
        String eventId = obxXml.getElementsByTagName("event_id").item(0)
                .getTextContent();
        String blobStr = text(obxXml, "observation_blob");
        String units = "", description = "";
        if (validBlob(blobStr)) {
//            ObservationType obxType = parseBlob(blobStr);

            String[] parts = blobStr.split("\\|");
            description = parts[1];
//            if (parts.length > 2 && obxType.hasUnits()) {
//                units = parts[parts.length - 1]; // not parts[2] in case the description has pipes in it
//                // needs a more robust solution
//            }
        } else {
            description = blobStr;
        }

        return new Observation.Builder(this.events.get(eventId))
                .concept(text(obxXml, "concept_cd"))
                .observer(text(obxXml, "observer_cd"))
                .startDate(date(obxXml, "start_date"))
                .modifier(text(obxXml, "modifier_cd"))
                .valuetype(text(obxXml, "valuetype_cd"))
                .tval(text(obxXml, "tval_char")).nval(text(obxXml, "nval_num"))
                .valueflag(text(obxXml, "valueflag_cd")).units(units)
                .endDate(date(obxXml, "end_date"))
                .location(text(obxXml, "location_cd")).blob(description)
                .build();
    }

    private boolean validBlob(String obxBlob) {
        return (null != obxBlob && !obxBlob.isEmpty() && obxBlob.contains("|"));
    }

    private String text(Element elt, String tagName) {
        try {
            return elt.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (Exception e) {
            return "";
        }
    }

    private Date date(Element elt, String tagName) {
        try {
            String dtTxt = text(elt, tagName);
            // remove the last colon (:)
            // we have to do this because Java's SimpleDateFormat does not directly
            // support the format i2b2 is using: 2001-07-04T12:08:56.235-07:00
            // the closest Java has is: 2001-07-04T12:08:56.235-0700 (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
            // the other alternative is write our own date parser
            int colonIdx = dtTxt.lastIndexOf(':');
            dtTxt = dtTxt.substring(0, colonIdx).concat(dtTxt.substring(colonIdx+1));
            System.out.println(dtTxt);
            return i2b2DateFormat.parse(dtTxt);
        } catch (ParseException e) {
            return null;
        }
    }
}

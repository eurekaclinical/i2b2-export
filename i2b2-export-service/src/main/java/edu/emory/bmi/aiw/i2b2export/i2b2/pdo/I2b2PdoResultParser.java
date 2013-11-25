package edu.emory.bmi.aiw.i2b2export.i2b2.pdo;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2CommUtil;
import edu.emory.bmi.aiw.i2b2export.xml.I2b2ExportServiceXmlException;
import edu.emory.bmi.aiw.i2b2export.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Parser for i2b2 PDO result XML.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class I2b2PdoResultParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(I2b2PdoResultParser.class);

	/*
	 * the i2b2 date format
	 */
	private final DateFormat i2b2DateFormat;

	/*
	 * maps from IDs to patients, events, and observers (providers)
	 */
	private final Map<String, Patient> patients;
	private final Map<String, Event> events;
	private final Map<String, Observer> observers;

	/*
	 * the set of observations in the result XML
	 */
	private final Set<Observation> observations;

	/*
	 * the XML document to parse
	 */
	private final Document d;

	/**
	 * Default constructor. Accepts an XML document for parsing.
	 *
	 * @param xmlDoc an XML {@link Document} to parse
	 */
	public I2b2PdoResultParser(Document xmlDoc) {
		d = xmlDoc;
		i2b2DateFormat = new SimpleDateFormat(I2b2CommUtil.I2B2_DATE_FMT);
		patients = new HashMap<>();
		events = new HashMap<>();
		observers = new HashMap<>();
		observations = new HashSet<>();
	}

	/**
	 * Parses the instance's XML document and returns the PDO results contained in it.
	 *
	 * @return an {@link I2b2PdoResults} containing all of the PDO results found in the XML
	 * @throws I2b2ExportServiceXmlException if there is a parsing error
	 */
	public I2b2PdoResults parse() throws I2b2ExportServiceXmlException {
		try {
			parseAll();
		} catch (XPathExpressionException e) {
			throw new I2b2ExportServiceXmlException("Unable to parse i2b2 PDO result XML", e);
		}

		return new I2b2PdoResults(patients.values(), events.values(), observers.values(), observations);
	}

	/**
	 * Parses all of the patients, events, observers and observations from the result XML and ties together all
	 * related entities.
	 *
	 * @throws XPathExpressionException if there is an XPath error
	 */
	private void parseAll() throws XPathExpressionException {
		parsePatients();
		parseEvents();
		parseObservers();
		parseObservations();

		for (Observation o : observations) {
			o.getEvent().addObservation(o);
			if (observers.containsKey(o.getObserver())) {
				observers.get(o.getObserver()).addObservation(o);
			}
		}
		for (Event e : events.values()) {
			e.getPatient().addEvent(e);
		}
		for (Patient p : patients.values()) {
			p.sortEvents();
		}
	}

	private NodeList pdoElement(String dataName) throws XPathExpressionException {
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

	private void parseObservers() throws XPathExpressionException {
		NodeList observerElts = pdoElement("observer");
		for (int i = 0; i < observerElts.getLength(); i++) {
			Node child = observerElts.item(i);
			Observer o = parseObserver((Element) child);
			observers.put(o.getObserverCode(), o);
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
		Map<String, String> pm = new CustomNullHashMap<>("N/A");
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
		Map<String, String> pm = new CustomNullHashMap<>("N/A");
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

	private Observer parseObserver(Element observerXml) {
		String path = observerXml.getElementsByTagName("observer_path").item(0).getTextContent();
		String code = observerXml.getElementsByTagName("observer_cd").item(0).getTextContent();
		String name = observerXml.getElementsByTagName("name_char").item(0).getTextContent();

		return new Observer.Builder(path, code).name(name).build();
	}

	private Observation parseObservation(Element obxXml) {
		String eventId = obxXml.getElementsByTagName("event_id").item(0)
				.getTextContent();
		String conceptPath = obxXml.getParentNode().getAttributes().getNamedItem("panel_name").getNodeValue();

		// some observations, like demographics, are timestamps and have no
		// end date, so we just set it to the start date
		Date startDate = date(obxXml, "start_date");
		Date endDate;
		if (obxXml.getElementsByTagName("end_date").getLength() == 1) {
			endDate = date(obxXml, "end_date");
		} else {
			endDate = new Date(startDate.getTime());
		}

		return new Observation.Builder(this.events.get(eventId))
				.conceptCode(text(obxXml, "concept_cd"))
				.conceptPath(conceptPath)
				.observer(text(obxXml, "observer_cd"))
				.startDate(startDate)
				.modifier(text(obxXml, "modifier_cd"))
				.valuetype(text(obxXml, "valuetype_cd"))
				.tval(text(obxXml, "tval_char")).nval(text(obxXml, "nval_num"))
				.valueflag(text(obxXml, "valueflag_cd")).units(text(obxXml, "units_cd"))
				.endDate(endDate)
				.location(text(obxXml, "location_cd")).blob(text(obxXml, "observation_blob"))
				.build();
	}

	private String text(Element elt, String tagName) {
		try {
			return elt.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (Exception e) {
			return "";
		}
	}

	private Date date(Element elt, String tagName) {
		String dtTxt = text(elt, tagName);
		// remove the last colon (:)
		// we have to do this because Java's SimpleDateFormat does not directly
		// support the format i2b2 is using: 2001-07-04T12:08:56.235-07:00
		// even though it is a valid ISO-8601 date
		// the closest Java has is: 2001-07-04T12:08:56.235-0700 (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
		// the other alternative is write our own date parser
		int colonIdx = dtTxt.lastIndexOf(':');
		if (colonIdx >= 0) {
			dtTxt = dtTxt.substring(0, colonIdx).concat(dtTxt.substring(colonIdx + 1));
		}

		try {
			return i2b2DateFormat.parse(dtTxt);
		} catch (ParseException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Unable to parse date: {}", dtTxt);
			} else if (null != dtTxt && !dtTxt.isEmpty()) {
				LOGGER.warn("Unable to parse date: {}", dtTxt);
			}
			return null;
		}
	}
}

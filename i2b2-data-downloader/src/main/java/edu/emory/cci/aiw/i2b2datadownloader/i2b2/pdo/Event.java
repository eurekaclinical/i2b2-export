package edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Event implements Comparable<Event> {
	private final String eventId;
	private final Patient patient;
	private final Date startDate;
	private final Date endDate;
	private final String inOut;
	private final String location;
	private final String activeStatus;

	private List<Observation> observations;

	private Event(Builder builder) {
		this.eventId = builder.eventId;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.inOut = builder.inOut;
		this.location = builder.location;
		this.activeStatus = builder.activeStatus;
		this.patient = builder.patient;

		this.observations = new ArrayList<Observation>();
	}

	public int compareTo(Event other) {
		return this.eventId.compareTo(other.eventId);
	}

	public String getEventId() {
		return eventId;
	}

	public Patient getPatient() {
		return patient;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getInOut() {
		return inOut;
	}

	public String getLocation() {
		return location;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public List<Observation> getObservations() {
		return Collections.unmodifiableList(observations);
	}

	public void addObservation(Observation o) {
		observations.add(o);
	}

	public static class Builder {
		private final String eventId;
		private final Patient patient;

		private Date startDate;
		private Date endDate;
		private String inOut;
		private String location;
		private String activeStatus;

		public Builder(String eventId, Patient patient) {
			this.eventId = eventId;
			this.patient = patient;
		}

		public Builder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		public Builder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		public Builder inOut(String inOut) {
			this.inOut = inOut;
			return this;
		}

		public Builder location(String location) {
			this.location = location;
			return this;
		}

		public Builder activeStatus(String activeStatus) {
			this.activeStatus = activeStatus;
			return this;
		}

		public Event build() {
			return new Event(this);
		}
	}
}

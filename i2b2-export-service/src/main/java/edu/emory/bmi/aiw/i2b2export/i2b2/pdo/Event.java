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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents an event in i2b2's Patient Data Object model. Follows the builder pattern for construction. Events are
 * ordered according their IDs.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class Event implements Comparable<Event> {

	/*
	 * the event ID (assigned by i2b2)
	 */
	private final String eventId;

	/*
	 * the patient who had this event
	 */
	private final Patient patient;

	/*
	 * start date of the event
	 */
	private final Date startDate;

	/*
	 * end date of the event
	 */
	private final Date endDate;

	/*
	 * the i2b2 inOut field of the event
	 */
	private final String inOut;

	/*
	 * the location of the event
	 */
	private final String location;

	/*
	 * the active status of the event
	 */
	private final String activeStatus;

	/*
	 * the i2b2 observations associated with the event
	 */
	private List<Observation> observations;

	private Event(Builder builder) {
		this.eventId = builder.eventId;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.inOut = builder.inOut;
		this.location = builder.location;
		this.activeStatus = builder.activeStatus;
		this.patient = builder.patient;

		this.observations = new ArrayList<>();
	}

	@Override
	public int compareTo(Event other) {
		return this.eventId.compareTo(other.eventId);
	}

	/**
	 * Gets the event ID.
	 *
	 * @return the event ID as a String
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Gets the patient who had this event.
	 *
	 * @return the {@link Patient}
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * Gets the start date of the event.
	 *
	 * @return the start date of the event as a {@link Date}
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Gets the end date of the event.
	 *
	 * @return the end date of the event as a {@link Date}
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Gets the in-out property of the event.
	 *
	 * @return the in-out property of the event as a String
	 */
	public String getInOut() {
		return inOut;
	}

	/**
	 * Gets the location of the event.
	 *
	 * @return the location of the event as a String
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Gets the active status property of the event.
	 *
	 * @return the active status as a String
	 */
	public String getActiveStatus() {
		return activeStatus;
	}

	/**
	 * Gets the list of i2b2 observations associated with this event
	 *
	 * @return a list of {@link Observation}s
	 */
	public List<Observation> getObservations() {
		return Collections.unmodifiableList(observations);
	}

	/**
	 * Adds an observation to this event's list of associated observations.
	 *
	 * @param o the {@link Observation} to add
	 */
	public void addObservation(Observation o) {
		observations.add(o);
	}

	/**
	 * The builder for Event
	 */
	public static class Builder {
		private final String eventId;
		private final Patient patient;

		private Date startDate;
		private Date endDate;
		private String inOut;
		private String location;
		private String activeStatus;

		/**
		 * Default constructor. Requires the event ID and the patient.
		 *
		 * @param eventId the ID of the event being built
		 * @param patient the patient who had the event
		 */
		public Builder(String eventId, Patient patient) {
			this.eventId = eventId;
			this.patient = patient;
		}

		/**
		 * Sets the start date of the event being built.
		 *
		 * @param startDate the start date of the event
		 * @return the builder
		 */
		public Builder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * Sets the end date of the event being built.
		 *
		 * @param endDate the end date of the event
		 * @return the builder
		 */
		public Builder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * Sets the in-out property of the event being built.
		 *
		 * @param inOut the in-out property of the event
		 * @return the builder
		 */
		public Builder inOut(String inOut) {
			this.inOut = inOut;
			return this;
		}

		/**
		 * Sets the location of the event being built.
		 *
		 * @param location the location of the event
		 * @return the builder
		 */
		public Builder location(String location) {
			this.location = location;
			return this;
		}

		/**
		 * Sets the active status property of the event being built.
		 *
		 * @param activeStatus the active status of the event
		 * @return the builder
		 */
		public Builder activeStatus(String activeStatus) {
			this.activeStatus = activeStatus;
			return this;
		}

		/**
		 * Builds and returns the event
		 *
		 * @return an {@link Event} created according to this builder
		 */
		public Event build() {
			return new Event(this);
		}
	}
}

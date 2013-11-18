package edu.emory.cci.aiw.i2b2export.i2b2.pdo;

/*
 * #%L
 * i2b2 Patient Data Export Service
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

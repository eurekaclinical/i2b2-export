package edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo;

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
import java.util.List;

public class Patient implements Comparable<Patient> {
	private final String patientId;
	private final String vitalStatus;
	private final String birthDate;
	private final String sex;
	private final String ageInYears;
	private final String language;
	private final String race;
	private final String religion;
	private final String maritalStatus;
	private final String zipCode;
	private final String stateCityZip;

	private List<Event> events;

	private Patient(Builder builder) {
		this.patientId = builder.patientId;
		this.vitalStatus = builder.vitalStatus;
		this.birthDate = builder.birthDate;
		this.sex = builder.sex;
		this.ageInYears = builder.ageInYears;
		this.language = builder.language;
		this.race = builder.race;
		this.religion = builder.religion;
		this.maritalStatus = builder.maritalStatus;
		this.zipCode = builder.zipCode;
		this.stateCityZip = builder.stateCityZip;

		this.events = new ArrayList<Event>();
	}

	public int compareTo(Patient other) {
		return this.patientId.compareTo(other.patientId);
	}

	public String getPatientId() {
		return patientId;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getSex() {
		return sex;
	}

	public String getAgeInYears() {
		return ageInYears;
	}

	public String getLanguage() {
		return language;
	}

	public String getRace() {
		return race;
	}

	public String getReligion() {
		return religion;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getStateCityZip() {
		return stateCityZip;
	}

	public List<Event> getEvents() {
		return Collections.unmodifiableList(events);
	}

	public void addEvent(Event e) {
		events.add(e);
	}

	public void sortEvents() {
		Collections.sort(events);
	}

	public static class Builder {
		private String patientId;

		private String vitalStatus;
		private String birthDate;
		private String sex;
		private String ageInYears;
		private String language;
		private String race;
		private String religion;
		private String maritalStatus;
		private String zipCode;
		private String stateCityZip;

		public Builder(String patientId) {
			this.patientId = patientId;
		}

		public Builder vitalStatus(String vitalStatus) {
			this.vitalStatus = vitalStatus;
			return this;
		}

		public Builder birthDate(String birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public Builder sex(String sex) {
			this.sex = sex;
			return this;
		}

		public Builder ageInYears(String ageInYears) {
			this.ageInYears = ageInYears;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public Builder race(String race) {
			this.race = race;
			return this;
		}

		public Builder religion(String religion) {
			this.religion = religion;
			return this;
		}

		public Builder maritalStatus(String maritalStatus) {
			this.maritalStatus = maritalStatus;
			return this;
		}

		public Builder zipCode(String zipCode) {
			this.zipCode = zipCode;
			return this;
		}

		public Builder stateCityZip(String stateCityZip) {
			this.stateCityZip = stateCityZip;
			return this;
		}

		public Patient build() {
			return new Patient(this);
		}
	}
}

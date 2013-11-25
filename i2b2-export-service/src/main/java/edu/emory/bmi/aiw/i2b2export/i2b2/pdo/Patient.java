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
import java.util.List;

/**
 * Represents a patient in i2b2's Patient Data Object model. Follows the builder pattern. Patients are ordered by ID.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class Patient implements Comparable<Patient> {

	/*
	 * the patient's ID
	 */
	private final String patientId;

	/*
	 * patient's vital status code
	 */
	private final String vitalStatus;

	/*
	 * birth date
	 */
	private final String birthDate;

	/*
	 * sex
	 */
	private final String sex;

	/*
	 * patient's age in years
	 */
	private final String ageInYears;

	/*
	 * language
	 */
	private final String language;

	/*
	 * race
	 */
	private final String race;

	/*
	 * religion
	 */
	private final String religion;

	/*
	 * marital status
	 */
	private final String maritalStatus;

	/*
	 * zip code
	 */
	private final String zipCode;

	/*
	 * address as state, city, and zip code
	 */
	private final String stateCityZip;

	/*
	 * list of events for this patient
	 */
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

		this.events = new ArrayList<>();
	}

	@Override
	public int compareTo(Patient other) {
		return this.patientId.compareTo(other.patientId);
	}

	/**
	 * Gets the patient's ID.
	 *
	 * @return the patient ID
	 */
	public String getPatientId() {
		return patientId;
	}

	/**
	 * Gets the patient's vital status.
	 *
	 * @return the vital status
	 */
	public String getVitalStatus() {
		return vitalStatus;
	}

	/**
	 * Gets the patient's birth date as a String.
	 *
	 * @return the birth date as a String
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * Gets the patient's sex.
	 *
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Gets the patient's age in years.
	 *
	 * @return the age in years
	 */
	public String getAgeInYears() {
		return ageInYears;
	}

	/**
	 * Gets the patient's language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Get's the patient's race.
	 *
	 * @return the race
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Gets the patient's religion
	 *
	 * @return the religion
	 */
	public String getReligion() {
		return religion;
	}

	/**
	 * Gets the patient's marital status
	 *
	 * @return the marital status
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * Gets the patient's zip code
	 *
	 * @return the zip code
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Gets the patient's state, city, and zip code
	 *
	 * @return the state, city, and zip code
	 */
	public String getStateCityZip() {
		return stateCityZip;
	}

	/**
	 * Gets the list of events for this patient.
	 *
	 * @return a list of {@link Event}s associated with this patient
	 */
	public List<Event> getEvents() {
		return Collections.unmodifiableList(events);
	}

	/**
	 * Adds an event to the patient's list of events.
	 *
	 * @param e the event to add
	 */
	public void addEvent(Event e) {
		events.add(e);
	}

	/**
	 * Sorts the patient's event according to {@link Event}'s natural ordering.
	 */
	public void sortEvents() {
		Collections.sort(events);
	}

	/**
	 * Builder for Patient
	 */
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

		/**
		 * Default constructor. Requires patient ID.
		 *
		 * @param patientId the ID of the patient being built
		 */
		public Builder(String patientId) {
			this.patientId = patientId;
		}

		/**
		 * Sets the vital status of the patient being built.
		 *
		 * @param vitalStatus the vital status
		 * @return the builder
		 */
		public Builder vitalStatus(String vitalStatus) {
			this.vitalStatus = vitalStatus;
			return this;
		}

		/**
		 * Sets the birth date of the patient being built.
		 *
		 * @param birthDate the birth date
		 * @return the builder
		 */
		public Builder birthDate(String birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		/**
		 * Sets the sex of the patient being built.
		 *
		 * @param sex the sex
		 * @return the builder
		 */
		public Builder sex(String sex) {
			this.sex = sex;
			return this;
		}

		/**
		 * Sets the age in years of the patient being built.
		 *
		 * @param ageInYears the age in years
		 * @return the builder
		 */
		public Builder ageInYears(String ageInYears) {
			this.ageInYears = ageInYears;
			return this;
		}

		/**
		 * Sets the language of the patient being built.
		 *
		 * @param language the language
		 * @return the builder
		 */
		public Builder language(String language) {
			this.language = language;
			return this;
		}

		/**
		 * Sets the race of the patient being built.
		 *
		 * @param race the race
		 * @return the builder
		 */
		public Builder race(String race) {
			this.race = race;
			return this;
		}

		/**
		 * Sets the religion of the patient being built.
		 *
		 * @param religion the religion
		 * @return the builder
		 */
		public Builder religion(String religion) {
			this.religion = religion;
			return this;
		}

		/**
		 * Sets the marital status of the patient being built.
		 *
		 * @param maritalStatus the marital status
		 * @return the builder
		 */
		public Builder maritalStatus(String maritalStatus) {
			this.maritalStatus = maritalStatus;
			return this;
		}

		/**
		 * Sets the zip code of the patient being built.
		 *
		 * @param zipCode the zip code
		 * @return the builder
		 */
		public Builder zipCode(String zipCode) {
			this.zipCode = zipCode;
			return this;
		}

		/**
		 * Sets the state, city, and zip code of the patient being built.
		 *
		 * @param stateCityZip the state, city, and zip code
		 * @return the builder
		 */
		public Builder stateCityZip(String stateCityZip) {
			this.stateCityZip = stateCityZip;
			return this;
		}

		/**
		 * Builds and returns the patient.
		 *
		 * @return a {@link Patient} created according to this builder
		 */
		public Patient build() {
			return new Patient(this);
		}
	}
}

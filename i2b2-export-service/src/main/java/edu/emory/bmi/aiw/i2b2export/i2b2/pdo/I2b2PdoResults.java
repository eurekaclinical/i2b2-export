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

import java.util.Collection;
import java.util.Collections;

/**
 * Container for i2b2 PDO results. Holds all of the patients, events, observers, and observations parsed from a PDO
 * result XML response.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class I2b2PdoResults {

	/*
	 * the patients
	 */
	private final Collection<Patient> patients;

	/*
	 * the events
	 */
	private final Collection<Event> events;

	/*
	 * the observers
	 */
	private final Collection<Observer> observers;

	/*
	 * the observations
	 */
	private final Collection<Observation> observations;

	/**
	 * Default constructor. Accepts the patients, events, observers, and observations.
	 *
	 * @param patients the patients
	 * @param events the events
	 * @param observers the observers
	 * @param observations the observations
	 */
	public I2b2PdoResults(Collection<Patient> patients, Collection<Event> events, Collection<Observer> observers, Collection<Observation> observations) {
		this.patients = patients;
		this.events = events;
		this.observers = observers;
		this.observations = observations;
	}

	/**
	 * Gets the patients from these PDO results.
	 *
	 * @return a collection of {@link Patient}s
	 */
	public Collection<Patient> getPatients() {
		return Collections.unmodifiableCollection(patients);
	}

	/**
	 * Gets the events from these PDO results.
	 *
	 * @return a collection of {@link Event}s
	 */
	public Collection<Event> getEvents() {
		return Collections.unmodifiableCollection(events);
	}

	/**
	 * Gets the observers from these PDO results.
	 *
	 * @return a collection of {@link Observer}s
	 */
	public Collection<Observer> getObservers() {
		return Collections.unmodifiableCollection(observers);
	}

	/**
	 * Gets the observations from these PDO results.
	 *
	 * @return a collection of {@link Observation}s
	 */
	public Collection<Observation> getObservations() {
		return Collections.unmodifiableCollection(observations);
	}
}

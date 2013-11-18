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

import java.util.Collection;
import java.util.Collections;

public class I2b2PdoResults {

	private final Collection<Patient> patients;
	private final Collection<Event> events;
	private final Collection<Observer> observers;
	private final Collection<Observation> observations;

	public I2b2PdoResults(Collection<Patient> patients, Collection<Event> events, Collection<Observer> observers, Collection<Observation> observations) {
		this.patients = patients;
		this.events = events;
		this.observers = observers;
		this.observations = observations;
	}

	public Collection<Patient> getPatients() {
		return Collections.unmodifiableCollection(patients);
	}

	public Collection<Event> getEvents() {
		return Collections.unmodifiableCollection(events);
	}

	public Collection<Observer> getObservers() {
		return Collections.unmodifiableCollection(observers);
	}

	public Collection<Observation> getObservations() {
		return Collections.unmodifiableCollection(observations);
	}
}

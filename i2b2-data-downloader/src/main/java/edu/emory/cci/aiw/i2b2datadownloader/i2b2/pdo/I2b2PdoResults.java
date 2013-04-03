package edu.emory.cci.aiw.i2b2datadownloader.i2b2.pdo;

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

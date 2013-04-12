package edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Observer {
	private final String observerPath;
	private final String observerCode;
	private final String name;

	private final Set<Observation> observations;

	private Observer(Builder builder) {
		this.observerPath = builder.observerPath;
		this.observerCode = builder.observerCode;
		this.name = builder.name;

		this.observations = new HashSet<Observation>();
	}

	public String getObserverCode() {
		return observerCode;
	}

	public String getObserverPath() {
		return observerPath;
	}

	public String getName() {
		return name;
	}

	public Set<Observation> getObservations() {
		return Collections.unmodifiableSet(observations);
	}

	public void addObservation(Observation observation) {
		this.observations.add(observation);
	}


	public static class Builder {
		private String observerPath;
		private String observerCode;
		private String name;

		public Builder(String observerPath, String observerCode) {
			this.observerPath = observerPath;
			this.observerCode = observerCode;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Observer build() {
			return new Observer(this);
		}
	}
}

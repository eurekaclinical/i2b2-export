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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an observer (or provider) in i2b2's Patient Data Object model. Follows the builder pattern.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class Observer {

	/*
	 * i2b2 observer path
	 */
	private final String observerPath;

	/*
	 * i2b2 observer code
	 */
	private final String observerCode;

	/*
	 * observer name
	 */
	private final String name;

	/*
	 * observations observed by this observer
	 */
	private final Set<Observation> observations;

	private Observer(Builder builder) {
		this.observerPath = builder.observerPath;
		this.observerCode = builder.observerCode;
		this.name = builder.name;

		this.observations = new HashSet<>();
	}

	/**
	 * Gets the i2b2 observer code.
	 *
	 * @return the observer code
	 */
	public String getObserverCode() {
		return observerCode;
	}

	/**
	 * Gets the i2b2 observer path.
	 *
	 * @return the observer path
	 */
	public String getObserverPath() {
		return observerPath;
	}

	/**
	 * Gets the observer name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the set of observations observed by this observer.
	 *
	 * @return an immutable {@link Set} of {@link Observation}s
	 */
	public Set<Observation> getObservations() {
		return Collections.unmodifiableSet(observations);
	}

	/**
	 * Adds an observation to this observer's set of observations.
	 *
	 * @param observation the {@link Observation} to add
	 */
	public void addObservation(Observation observation) {
		this.observations.add(observation);
	}

	/**
	 * Builder for Observer
	 */
	public static class Builder {
		private String observerPath;
		private String observerCode;
		private String name;

		/**
		 * Default constructor. Requires the i2b2 observer path and i2b2 observer code for the observer.
		 * @param observerPath the i2b2 observer path
		 * @param observerCode the i2b2 observer code
		 */
		public Builder(String observerPath, String observerCode) {
			this.observerPath = observerPath;
			this.observerCode = observerCode;
		}

		/**
		 * Sets the name of the observer being built
		 * @param name the name
		 * @return the builder
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Builds and returns the observer.
		 *
		 * @return a {@link Observer} created according to this builder
		 */
		public Observer build() {
			return new Observer(this);
		}
	}
}

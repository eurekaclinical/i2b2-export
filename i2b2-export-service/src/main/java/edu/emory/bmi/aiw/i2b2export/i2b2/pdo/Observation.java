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

import java.util.Date;

/**
 * Represents an observation in i2b2's Patient Data Object model. Follows the builder pattern for construction.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class Observation {

	/*
	 * the event this observation was recorded in
	 */
	private final Event event;

	/*
	 * i2b2 concept code
	 */
	private final String conceptCode;

	/*
	 * i2b2 concept path
	 */
	private final String conceptPath;

	/*
	 * observer string
	 */
	private final String observer;

	/*
	 * observation start date
	 */
	private final Date startDate;

	/*
	 * observation end date
	 */
	private final Date endDate;

	/*
	 * modifier code
	 */
	private final String modifier;

	/*
	 * type of the observation result (text or numerical)
	 */
	private final String valuetype;

	/*
	 * text result value
	 */
	private final String tval;

	/*
	 * numerical result value
	 */
	private final String nval;

	/*
	 * value flag
	 */
	private final String valueflag;

	/*
	 * result units
	 */
	private final String units;

	/*
	 * location of the observation
	 */
	private final String location;

	/*
	 * i2b2 observation blob
	 */
	private final String blob;

	/**
	 * Default constructor. Follows builder pattern.
	 *
	 * @param builder the {@link Builder} to create the observation from
	 */
	Observation(Builder builder) {
		this.event = builder.event;
		this.conceptCode = builder.conceptCode;
		this.conceptPath = builder.conceptPath;
		this.observer = builder.observer;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.modifier = builder.modifier;
		this.valuetype = builder.valuetype;
		this.tval = builder.tval;
		this.nval = builder.nval;
		this.valueflag = builder.valueflag;
		this.units = builder.units;
		this.location = builder.location;
		this.blob = builder.blob;
	}

	/**
	 * Gets the event this observation was recorded at.
	 *
	 * @return the {@link Event} this observation was recorded at
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Gets the i2b2 concept code for this observation.
	 *
	 * @return the i2b2 concept code as a String
	 */
	public String getConceptCode() {
		return conceptCode;
	}

	/**
	 * Gets the i2b2 concept path for this observation.
	 *
	 * @return the i2b2 concept path as a String
	 */
	public String getConceptPath() {
		return conceptPath;
	}

	/**
	 * Gets the observer string.
	 *
	 * @return the observer string
	 */
	public String getObserver() {
		return observer;
	}

	/**
	 * Gets the start date of this observation.
	 *
	 * @return the start date as a {@link Date}
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Gets the end date of this observation.
	 *
	 * @return the end date as a {@link Date}
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Gets the i2b2 modifier code.
	 *
	 * @return the modifier as a String
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * Gets the value type of the observation result (text or numerical).
	 *
	 * @return the value type as a String
	 */
	public String getValuetype() {
		return valuetype;
	}

	/**
	 * Gets the value of the text result of the observation.
	 *
	 * @return the text value as a String
	 */
	public String getTval() {
		return tval;
	}

	/**
	 * Gets the value of the numerical result of the observation.
	 *
	 * @return the numerical value as a String
	 */
	public String getNval() {
		return nval;
	}

	/**
	 * Gets the value flag of the observation.
	 *
	 * @return the value flag as a String
	 */
	public String getValueflag() {
		return valueflag;
	}

	/**
	 * Gets the units of the observation result.
	 *
	 * @return the units as a String
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Gets the location of the observation
	 *
	 * @return the location as a String
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Gets the i2b2 observation blob.
	 *
	 * @return the blob as a String
	 */
	public String getBlob() {
		return blob;
	}

	/**
	 * The builder for Observation
	 */
	public static class Builder {
		private final Event event;
		private String conceptCode;
		private String conceptPath;
		private String observer;
		private Date startDate;
		private Date endDate;
		private String modifier;
		private String valuetype;
		private String tval;
		private String nval;
		private String valueflag;
		private String units;
		private String location;
		private String blob;

		/**
		 * Default constructor. Requires the event where the observation was recorded.
		 *
		 * @param event the {@link Event} the observation belongs to
		 */
		public Builder(Event event) {
			this.event = event;
		}

		/**
		 * Sets the i2b2 concept code of the observation being built.
		 *
		 * @param conceptCode the concept code
		 * @return the builder
		 */
		public Builder conceptCode(String conceptCode) {
			this.conceptCode = conceptCode;
			return this;
		}

		/**
		 * Sets the i2b2 concept path of the observation being built.
		 *
		 * @param conceptPath the concept path
		 * @return the builder
		 */
		public Builder conceptPath(String conceptPath) {
			this.conceptPath = conceptPath;
			return this;
		}

		/**
		 * Sets the observer of the observation being built.
		 *
		 * @param observer the observer
		 * @return the builder
		 */
		public Builder observer(String observer) {
			this.observer = observer;
			return this;
		}

		/**
		 * Sets the start date of the observation being built.
		 *
		 * @param startDate the start date
		 * @return the builder
		 */
		public Builder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * Sets the end date of the observation being built.
		 *
		 * @param endDate the end date
		 * @return the builder
		 */
		public Builder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * Sets the modifier of the observation being built.
		 *
		 * @param modifier the modifier
		 * @return the builder
		 */
		public Builder modifier(String modifier) {
			this.modifier = modifier;
			return this;
		}

		/**
		 * Sets the value type of the observation being built.
		 *
		 * @param valuetype the value type
		 * @return the builder
		 */
		public Builder valuetype(String valuetype) {
			this.valuetype = valuetype;
			return this;
		}

		/**
		 * Sets the text value of the observation being built.
		 *
		 * @param tval the text value
		 * @return the builder
		 */
		public Builder tval(String tval) {
			this.tval = tval;
			return this;
		}

		/**
		 * Sets the numerical value of the observation being built.
		 *
		 * @param nval the numerical value
		 * @return the builder
		 */
		public Builder nval(String nval) {
			this.nval = nval;
			return this;
		}

		/**
		 * Sets the value flag of the observation being built.
		 *
		 * @param valueflag the value flag
		 * @return the builder
		 */
		public Builder valueflag(String valueflag) {
			this.valueflag = valueflag;
			return this;
		}

		/**
		 * Sets the units the observation being built.
		 *
		 * @param units the units
		 * @return the builder
		 */
		public Builder units(String units) {
			this.units = units;
			return this;
		}

		/**
		 * Sets the location of the observation being built.
		 *
		 * @param location the location
		 * @return the builder
		 */
		public Builder location(String location) {
			this.location = location;
			return this;
		}

		/**
		 * Sets the blob of the observation being built.
		 *
		 * @param blob the blob
		 * @return the builder
		 */
		public Builder blob(String blob) {
			this.blob = blob;
			return this;
		}

		/**
		 * Builds and returns the observation.
		 *
		 * @return an {@link Observation} created according to this builder
		 */
		public Observation build() {
			return new Observation(this);
		}
	}
}

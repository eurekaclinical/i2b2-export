package edu.emory.cci.aiw.i2b2patientdataexport.i2b2.pdo;

import java.util.Date;

public class Observation {
	private final Event event;
	private final String conceptCode;
    private final String conceptPath;
	private final String observer;
	private final Date startDate;
	private final Date endDate;
	private final String modifier;
	private final String valuetype;
	private final String tval;
	private final String nval;
	private final String valueflag;
	private final String units;
	private final String location;
	private final String blob;

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

	public Event getEvent() {
		return event;
	}

	public String getConceptCode() {
		return conceptCode;
	}

    public String getConceptPath() {
        return conceptPath;
    }

	public String getObserver() {
		return observer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getModifier() {
		return modifier;
	}

	public String getValuetype() {
		return valuetype;
	}

	public String getTval() {
		return tval;
	}

	public String getNval() {
		return nval;
	}

	public String getValueflag() {
		return valueflag;
	}

	public String getUnits() {
		return units;
	}

	public String getLocation() {
		return location;
	}

	public String getBlob() {
		return blob;
	}

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

		public Builder(Event event) {
			this.event = event;
		}

		public Builder conceptCode(String conceptCode) {
			this.conceptCode = conceptCode;
			return this;
		}

        public Builder conceptPath(String conceptPath) {
            this.conceptPath = conceptPath;
            return this;
        }

		public Builder observer(String observer) {
			this.observer = observer;
			return this;
		}

		public Builder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		public Builder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		public Builder modifier(String modifier) {
			this.modifier = modifier;
			return this;
		}

		public Builder valuetype(String valuetype) {
			this.valuetype = valuetype;
			return this;
		}

		public Builder tval(String tval) {
			this.tval = tval;
			return this;
		}

		public Builder nval(String nval) {
			this.nval = nval;
			return this;
		}

		public Builder valueflag(String valueflag) {
			this.valueflag = valueflag;
			return this;
		}

		public Builder units(String units) {
			this.units = units;
			return this;
		}

		public Builder location(String location) {
			this.location = location;
			return this;
		}

		public Builder blob(String blob) {
			this.blob = blob;
			return this;
		}

		public Observation build() {
			return new Observation(this);
		}
	}
}

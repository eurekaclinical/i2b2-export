package edu.emory.bmi.aiw.i2b2export.entity;

/**
 * Valid display formats for a column configuration
 */
public enum DisplayFormat {
	/**
	 * whether data exists for this concept
	 */
	EXISTENCE, /**
	 * the value of the data for this concept
	 */ VALUE, /**
	 * an aggregation of all of the data for this concept
	 */ AGGREGATION
    
}

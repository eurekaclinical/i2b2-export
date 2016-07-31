package edu.emory.bmi.aiw.i2b2export.entity;

/*-
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 - 2016 Emory University
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

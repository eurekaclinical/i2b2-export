package edu.emory.bmi.aiw.i2b2export.resource;

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

/**
 * General exception thrown from the application. It wraps any lower level exceptions.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class I2b2ExportServiceException extends Exception {

	/**
	 * Default no-arg constructor.
	 */
	public I2b2ExportServiceException() {

	}

	/**
	 * Creates an exception with the given message.
	 *
	 * @param message the exception's message
	 */
	public I2b2ExportServiceException(String message) {
		super(message);
	}

	/**
	 * Creates an exception wrapping the given cause.
	 *
	 * @param cause the {@link Throwable} cause to wrap
	 */
	public I2b2ExportServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates an exception with the given message wrapping the given cause.
	 *
	 * @param message the exception's message
	 * @param cause the {@link Throwable} cause to wrap
	 */
	public I2b2ExportServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}

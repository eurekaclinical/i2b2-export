package edu.emory.cci.aiw.i2b2patientdataexport;

public class DataDownloaderException extends Exception {

	public DataDownloaderException() {

	}

	public DataDownloaderException(String message) {
		super(message);
	}

	public DataDownloaderException(Throwable cause) {
		super(cause);
	}

	public DataDownloaderException(String message, Throwable cause) {
		super(message, cause);
	}

}

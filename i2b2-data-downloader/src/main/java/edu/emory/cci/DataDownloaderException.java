package edu.emory.cci;

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

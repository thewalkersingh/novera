package com.novera.noveraserver.exception;

public class InvalidInputDataException extends RuntimeException {
	
	public InvalidInputDataException(String message) {
		super(message);
	}
	
	public InvalidInputDataException(String resource, Object id) {
		super(resource + " is an invalid input data for id: " + id);
	}
	
}
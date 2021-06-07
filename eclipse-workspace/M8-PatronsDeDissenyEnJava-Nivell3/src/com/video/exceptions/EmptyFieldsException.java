package com.video.exceptions;

public class EmptyFieldsException extends Exception{
	
	private static final String EMPTY_FIELD_ERROR = "Some fields are empty";
	
	public EmptyFieldsException() {
		super(EMPTY_FIELD_ERROR);
	}

}

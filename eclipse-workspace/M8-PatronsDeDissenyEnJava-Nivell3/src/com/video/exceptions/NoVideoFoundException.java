package com.video.exceptions;

public class NoVideoFoundException extends Exception {

private static final String NO_VIDEO_FOUND_ERROR= "Impossible to find a video with that ID";
	
	public NoVideoFoundException() {
		super(NO_VIDEO_FOUND_ERROR);
	}
}

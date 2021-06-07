package com.video.exceptions;

public class NoVideoIncludedException extends Exception{
	
	private static final String NO_VIDEO_INCLUDED_ERROR = "Video could not be found";
	
	public NoVideoIncludedException() {
		super(NO_VIDEO_INCLUDED_ERROR);
	}

}

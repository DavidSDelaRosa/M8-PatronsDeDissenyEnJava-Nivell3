package com.video.domain;

public class Tag {
	
	private String tagText;
	
	public Tag(String tagText) {
		this.tagText = tagText;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	@Override
	public String toString() {
		return "Tag [tagText=" + tagText + "]";
	}

}

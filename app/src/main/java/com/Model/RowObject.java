package com.Model;

/**
 * Represents an individual row object that is stored in a list view.
 * 
 * @author kstorck
 */
public class RowObject {

	private String topText;
	private String bottomText;
	private String rightText; 
	private String thumbnailURL;
	
	/* 
	 * What the row object is representing ( category or video ). 
	 */
	private String fromType;
	
	/*
	 * The corresponding id for the object ( id for category or video ).
	 */
	private int ID;
	
	public String getTopText() {
		return topText;
	}
	public void setTopText(String topText) {
		this.topText = topText;
	}
	public String getBottomText() {
		return bottomText;
	}
	public void setBottomText(String bottomText) {
		this.bottomText = bottomText;
	}
	public String getRightText() {
		return rightText;
	}
	public void setRightText(String rightText) {
		this.rightText = rightText;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	public String getFromType() {
		return fromType;
	}
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public void setThumbnailURL(String url) {
		this.thumbnailURL = url;
	}
	public String getThumbnailURL(){
		return thumbnailURL;
	}
	
	
}

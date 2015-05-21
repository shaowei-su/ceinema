package com.Model;

import java.util.ArrayList;
import java.util.HashMap;

import com.CEInema.Configurator;

/**
 * Holds all the data needed for the MediaInfoView
 * 
 * @author kstorck
 */
public class MediaInfo {

	private String title;
	private String presenterFirst;
	private String presenterLast;
	private String presenterCredential;
	private String presenterTitle;
	private String presenterEmployer;
	private String sponsor;
	private String date;
	private String sourceType;
	private String description;
	private int presenterID;
	private int centerID;
	private String centerName;
	private String URL;
	private String category;
	private int categoryID;
	private int ID;
	private int sponsorID;
	private ArrayList<String> objectives;
	private String thumbnailPath;
	private HashMap<String, String> guidelines;
	private int format_quality;
	
	public int getFormat_quality() {
		return format_quality;
	}
	public void setFormat_quality(int format_quality) {
		this.format_quality = format_quality;
	}
	public String getPresenterCredential() {
		return presenterCredential;
	}
	public void setPresenterCredential(String presenterCredential) {
		this.presenterCredential = presenterCredential;
	}
	public String getPresenterTitle() {
		return presenterTitle;
	}
	public void setPresenterTitle(String presenterTitle) {
		this.presenterTitle = presenterTitle;
	}
	public String getPresenterEmployer() {
		return presenterEmployer;
	}
	public void setPresenterEmployer(String presenterEmployer) {
		this.presenterEmployer = presenterEmployer;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String URL) {
		this.URL = URL;
	}	
	public String getCenterName() {
		return this.centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public void setCenterID(int centerID) {
		this.centerID = centerID;
	}
	public int getCenterID(){
		return this.centerID;
	}
	public String getPresenterFirst() {
		return presenterFirst;
	}
	public void setPresenterFirst(String presenterFirst) {
		this.presenterFirst = presenterFirst;
	}
	public String getPresenterLast() {
		return presenterLast;
	}
	public void setPresenterLast(String presenterLast) {
		this.presenterLast = presenterLast;
	}
	public int getSponsorID() {
		return sponsorID;
	}
	public void setSponsorID(int sponsorID) {
		this.sponsorID = sponsorID;
	}
	public int getPresenterID() {
		return presenterID;
	}
	public void setPresenterID(int presenterID) {
		this.presenterID = presenterID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Concatenate first, last, and credential for Presenter name
	 * @return
	 */
	public String getPresenter() {
		String name = presenterFirst + " " + presenterLast + " " + presenterCredential; 
		if (name.contains(Configurator.PRESENTER_NA))
			name = "";
		return name;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public ArrayList<String> getObjectives() {
		return objectives;
	}
	public void setObjectives(ArrayList<String> objectives) {
		this.objectives = objectives;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public HashMap<String, String> getGuidelines() {
		return guidelines;
	}
	public void setGuidelines(HashMap<String, String> guidelines) {
		this.guidelines = guidelines;
	}
	
	
}

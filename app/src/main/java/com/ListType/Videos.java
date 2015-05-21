package com.ListType;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import com.CEInema.Configurator;

import android.net.ParseException;
import android.text.format.DateFormat;

/**
 * Holds logic for how to query for videos
 * @author kstorck
 */
public class Videos implements ListType{
	public HashMap<String, Integer> fieldIndices;
	private int notID;
	private int presenterID;
	private int sponsorID;
	private int centerID;
	private int categoryID;
	private String sortByName = "";
	private int sortByNameIndex = 0;
	private String sortByDate = "&sortBy=MEDIA_DATE_RELEASED&sortByOrder=DESC"; //default sort
	private int sortByDateIndex = 1;
	//private int format_qualityID;
	
	public Videos(int notID, int presenterID, int sponsorID, int centerID, int categoryID) {
		this.notID = notID;
		this.presenterID = presenterID;
		this.sponsorID = sponsorID;
		this.centerID = centerID;
		this.categoryID = categoryID;
		//this.format_qualityID = format_qualityID;
		
		// Populate HashMap with fields needed
		fieldIndices = new HashMap<String, Integer>();
		fieldIndices.put("MEDIAID", null);
		fieldIndices.put("MEDIA_TITLE", null);
		fieldIndices.put("MEDIA_DESCRIPTION", null);
		fieldIndices.put("PRESENTER_FIRST_NAME", null);
		fieldIndices.put("PRESENTER_LAST_NAME", null);
		fieldIndices.put("PRESENTER_CREDENTIAL", null);
		fieldIndices.put("MEDIA_DATE_RELEASED", null);
		fieldIndices.put("MEDIA_THUMB_PATH", null);
	}

	public String getURLAppendage() {
		String URL = "getMedia";
		if (presenterID > 0) {
			URL += "&presenterID=" + presenterID;
		} 
		/*
		if (format_qualityID != -1) {
			URL += "&format_qualityID=" + format_qualityID;
		} */
		if (sponsorID > 0) {
			URL += "&sponsorID=" + sponsorID;
		} 
		if (centerID > 0) {
			URL += "&centerID=" + centerID;
		} 
		if (notID > 0) {
			URL += "&notID=" + notID;
		}
		if (categoryID > 0) {
			URL += "&categoryID=" + categoryID;
		}
		if (!sortByName.equals("")) {
			URL += sortByName;
		}
		if (!sortByDate.equals("")) {
			URL += sortByDate;
		}
		return URL;
	}

	public HashMap<String, Integer> getColumns() {
		
		return fieldIndices;
	}

	public void setColumns(HashMap<String, Integer> columns) {
		this.fieldIndices = columns;
		
	}

	public String getTopText(JSONArray array) {
		String title = null;

		try {
			title = array.getString(fieldIndices.get("MEDIA_TITLE"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return title;
	}

	public String getBottomText(JSONArray array) {
		// Return formatted Presenter name
		// First name + last name + credential
		String full_name="";
		try {
			String first_name="";  
			if (!array.isNull(fieldIndices.get("PRESENTER_FIRST_NAME"))) {
				first_name = array.getString(fieldIndices.get("PRESENTER_FIRST_NAME"));
			}  
			String last_name="";
			if (!array.isNull(fieldIndices.get("PRESENTER_LAST_NAME"))) {
				last_name = array.getString(fieldIndices.get("PRESENTER_LAST_NAME"));
			}
			String presenter_credential="";
			if (!array.isNull(fieldIndices.get("PRESENTER_CREDENTIAL"))) {
				presenter_credential = array.getString(fieldIndices.get("PRESENTER_CREDENTIAL"));
			}
			String presentation_date="";
			if (!array.isNull(fieldIndices.get("MEDIA_DATE_RELEASED"))) {
				presentation_date = array.getString(fieldIndices.get("MEDIA_DATE_RELEASED"));
				presentation_date = presentation_date.substring(0, presentation_date.length()-8);
			}
				String newLine = "<br>";
			if (first_name.equals("") && last_name.equals("") && presenter_credential.equals("")) 
				newLine = "";
			full_name = first_name + " " +
						last_name 	+ " " + 
						presenter_credential + newLine +
						presentation_date + "";
			if (full_name.contains(Configurator.PRESENTER_NA))
				full_name = "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return full_name;
	}
	private Date ConvertToDate(String dateString){

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");

		Date convertedDate = null;
		try {
			try {
				convertedDate = (Date) dateFormat.parse(dateString);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return convertedDate;

	}
	
	public String getRightText(JSONArray array) {
		return "";
	}

	public String getFromType(JSONArray array) {
		return "VIDEO";
	}

	public int getFromID(JSONArray array) {
		int ID = 0;
		try {
			ID = array.getInt(fieldIndices.get("MEDIAID"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ID;
	}

	public String getThumbnailURL(JSONArray array) {
		String URL = null;

		try {
			URL = array.getString(fieldIndices.get("MEDIA_THUMB_PATH"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return URL;
	}

	public boolean sortableByName() {
		return true;
	}

	/**
	 * Videos have no count associated with them
	 */
	public boolean sortableByCount() {
		return false;
	}
	
	public boolean sortableByDate() {
		return true;
	}
	/**
	 * Creates the string appendage for implementing a sort.
	 * Uses the Interface's SortBy enumeration.
	 * Sort order is ASC, DESC, NULL(no sort).
	 */
	public void nextNameSort() {
		sortByDate = "";
		int index = sortByNameIndex % sortBy.length;
		if (!sortBy[index].equals("NULL")) {
			sortByName = "&sortBy=media_title&sortByOrder=" + sortBy[index];
		} else {
			sortByName = "";
		}
		
		sortByNameIndex++;
	}
	public void nextDateSort() {
		sortByName = "";
		int index = sortByDateIndex % sortBy.length;
		if (!sortBy[index].equals("NULL")) {
			sortByDate = "&sortBy=MEDIA_DATE_RELEASED&sortByOrder=" + sortBy[index];
		} else {
			sortByDate = "";
		}
		
		sortByNameIndex++;
	}
	/**
	 * Not used for videos
	 */
	public void nextCountSort() {
	}
	
	public String getNameSortLabel() {
		return "Media Title";
	}
	
	public String getDateSortLabel() {
		return "Released Date";
	}
	
	/**
	 * Not used for videos
	 */
	public String getCountSortLabel() {
		return null;
	}

	
	

}

package com.ListType;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Holds the logic for how to query the Sponsors.
 * 
 * @author kstorck
 */
public class Sponsors implements ListType {
	public HashMap<String, Integer> fieldIndices;
	
	public Sponsors() {
		// Populate HashMap with fields needed
		fieldIndices = new HashMap<String, Integer>();
		fieldIndices.put("SPONSOR_NAME", null);
		fieldIndices.put("SPONSORID", null);
		fieldIndices.put("COUNT", null);
	}

	/**
	 * Name of method in web service
	 */
	public String getURLAppendage() {
		return "getSponsor";
	}

	/**
	 * The HashMap of fields needed for row object.  Maps
	 * field name(String) to their index in the JSON query object
	 * @return
	 */
	public HashMap<String, Integer> getColumns() {
		return fieldIndices;
	}

	public void setColumns(HashMap<String, Integer> columns) {
		this.fieldIndices = columns;		
	}

	public String getTopText(JSONArray array) {
		String sponsor_name = null;;
		try {
			sponsor_name = array.getString(fieldIndices.get("SPONSOR_NAME"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return sponsor_name;
	}

	public String getBottomText(JSONArray array) {
		return "";
	}

	public String getRightText(JSONArray array) {
		int count = 0;
		try {
			count = array.getInt(fieldIndices.get("COUNT"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return count + " items";
	}

	public String getFromType(JSONArray array) {
		
		return "SPONSOR";
	}

	public int getFromID(JSONArray array) {
		int ID = 0;
		try {
			ID = array.getInt(fieldIndices.get("SPONSORID"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ID;
	}

	public String getThumbnailURL(JSONArray array) {
		return "";
	}

	public boolean sortableByName() {
		return true;
	}

	public boolean sortableByCount() {
		return true;
	}
	public boolean sortableByDate() {
		return true;
	}
	public void nextNameSort() {		
	}

	public void nextCountSort() {
	}
	
	public void nextDateSort() {
	}
	
	public String getNameSortLabel() {
		return "Sponsor Name";
	}

	public String getCountSortLabel() {
		return "Number of Items";
	}
	
	public String getDateSortLabel() {
		return "Released Date";
	}

	

}

package com.ListType;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Holds the logic for how to query the Centers.
 * 
 * @author kstorck
 */
public class Centers implements ListType{
	public HashMap<String, Integer> fieldIndices;
	
	public Centers() {
		// Populate HashMap with fields needed
		fieldIndices = new HashMap<String, Integer>();
		fieldIndices.put("PRODUCER_NAME", null);
		fieldIndices.put("PRODUCERID", null);
		fieldIndices.put("COUNT", null);
	}

	/**
	 * Name of method in web service
	 */
	public String getURLAppendage() {
		return "getCenter";
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
		String center_name = null;
		try {
			center_name = array.getString(fieldIndices.get("PRODUCER_NAME"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return center_name;
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
		return "CENTER";
	}

	public int getFromID(JSONArray array) {
		int ID = 0;
		try {
			ID = array.getInt(fieldIndices.get("PRODUCERID"));
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
	public boolean sortableByDate() {
		return true;
	}
	public boolean sortableByCount() {
		return true;
	}

	public void nextNameSort() {
	}

	public void nextCountSort() {
	}
	public void nextDateSort() {
	}
	public String getNameSortLabel() {
		return "Center Name";
	}

	public String getCountSortLabel() {
		return "Number of Items";
	}
	public String getDateSortLabel() {
		return "Released Date";
	}
}

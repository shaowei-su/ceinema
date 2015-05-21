package com.ListType;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Holds the logic for how to query the Presenters.
 * 
 * @author kstorck
 */
public class Presenters implements ListType{
	public HashMap<String, Integer> fieldIndices;
	private String sortByName = "";
	private int sortByNameIndex = 0;
	private String sortByCount = "";
	private int sortByCountIndex = 1;  // Have Count sort start on Desc
	private String sortByDate = "";
	private int sortByDateIndex = 2;
	
	public Presenters() {
		// Populate HashMap with fields needed
		fieldIndices = new HashMap<String, Integer>();
		fieldIndices.put("PRESENTER_FIRST_NAME", null);
		fieldIndices.put("PRESENTER_LAST_NAME", null);
		fieldIndices.put("PRESENTER_CREDENTIAL", null);
		fieldIndices.put("PRESENTER_TITLE", null);
		fieldIndices.put("PRESENTER_EMPLOYER", null);
		fieldIndices.put("PRESENTERID", null);
		fieldIndices.put("COUNT", null);
	}

	/**
	 * Name of method in web service
	 */
	public String getURLAppendage() {
		String URL = "getPresenter";
		if (!sortByName.equals("")) {
			URL += sortByName;
		}
		if (!sortByCount.equals("")) {
			URL += sortByCount;
		}
		return URL;
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
		String full_name = null;
		
		// Return formatted Presenter name
		// First + last name
		try {
			full_name = array.getString(fieldIndices.get("PRESENTER_FIRST_NAME")) + " " +
				array.getString(fieldIndices.get("PRESENTER_LAST_NAME"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return full_name;
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
		return "PRESENTER";
	}

	public int getFromID(JSONArray array) {
		int ID = 0;
		try {
			ID = array.getInt(fieldIndices.get("PRESENTERID"));
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
		return false;
	}
	/**
	 * Creates the string appendage for implementing a sort.
	 * Uses the Interface's SortBy enumeration.
	 * Sort order is ASC, DESC, NULL(no sort).
	 */
	public void nextNameSort() {
		sortByCount="";
		sortByDate = "";
		int index = sortByNameIndex % sortBy.length;
		if (!sortBy[index].equals("NULL")) {
			sortByName = "&sortBy=presenter_last_name&sortByOrder=" + sortBy[index];
		} else {
			sortByName = "";
		}
		
		sortByNameIndex++;
	}

	/**
	 * Creates the string appendage for implementing a sort.
	 * Uses the Interface's SortBy enumeration.
	 * Sort order is ASC, DESC, NULL(no sort).
	 */
	public void nextCountSort() {
		sortByName="";
		sortByDate = "";
		int index = sortByCountIndex % sortBy.length;
		if (!sortBy[index].equals("NULL")) {
			sortByCount = "&sortBy=count&sortByOrder=" + sortBy[index];
		} else {
			sortByCount = "";
		}
		sortByCountIndex++;
		
	}
	/**
	 * Creates the string appendage for implementing a sort.
	 * Uses the Interface's SortBy enumeration.
	 * Sort order is ASC, DESC, NULL(no sort).
	 */
	public void nextDateSort() {
		sortByName="";
		sortByCount="";
		int index = sortByDateIndex % sortBy.length;
		if (!sortBy[index].equals("NULL")) {
			sortByDate= "&sortBy=MEDIA_DATE_RELEASED&sortByOrder=" + sortBy[index];
		} else {
			sortByDate = "";
		}
		sortByDateIndex++;
	}

	public String getNameSortLabel() {
		return "Presenter Name";
	}

	public String getCountSortLabel() {
		return "Number of Items";
	}
	public String getDateSortLabel() {
		return "Released Date";
	}

}

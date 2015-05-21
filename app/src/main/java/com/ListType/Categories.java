package com.ListType;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Holds the logic for how to query the Categories
 * (New, PEP, Mental Health, Prevention...)
 * 
 * @author kstorck
 */
public class Categories implements ListType {
	
	public HashMap<String, Integer> fieldIndices;
	private String sortByName = "";
	private int sortByNameIndex = 0;
	private String sortByCount = "";
	private int sortByCountIndex = 1;  // Have Count sort start on Desc
	private String sortByDate = "";
	private int sortByDateIndex = 2;  // Have Count sort start on Desc
	
	public Categories() {
		// Populate HashMap with fields needed
		fieldIndices = new HashMap<String, Integer>();
		fieldIndices.put("CATEGORY", null);
		fieldIndices.put("CATEGORYID", null);
		fieldIndices.put("COUNT", null);
	}

	/**
	 * Name of method in web service
	 */
	public String getURLAppendage() {
		String URL = "getCategory";
		if (!sortByName.equals("")) {
			URL += sortByName;
		}
		if (!sortByCount.equals("")) {
			URL += sortByCount;
		}
		if (!sortByDate.equals("")) {
			URL += sortByDate;
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
		String category = null;
		try {
			category = array.getString(fieldIndices.get("CATEGORY"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return category;
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
		
		return "CATEGORY";
	}

	public int getFromID(JSONArray array) {
		int ID = 0;
		try {
			ID = array.getInt(fieldIndices.get("CATEGORYID"));
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
			sortByName = "&sortBy=category&sortByOrder=" + sortBy[index];
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
		return "Category Name";
	}

	public String getCountSortLabel() {
		return "Number of Items";
	}
	public String getDateSortLabel() {
		return "Released Date";
	}

}

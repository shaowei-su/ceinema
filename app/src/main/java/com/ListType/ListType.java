package com.ListType;

import java.util.HashMap;

import org.json.JSONArray;

/**
 * Interface for type of list that will be queried ( Presenters, 
 * Sponsors, Centers, Videos ). JSONParser will reference this class 
 * to populate row object fields.
 * 
 * @author kstorck
 */
public interface ListType {
	/*
	 * Enumeration for sort order
	 */
	String[] sortBy = {"ASC", "DESC", "NULL"};
	
	String getURLAppendage();
	
	/**
	 * The HashMap of fields needed for row object.  Maps
	 * field name(String) in the database to their index in the JSON query object
	 */
	HashMap<String, Integer> getColumns();
	void setColumns(HashMap<String, Integer> columns);
	
	String getTopText(JSONArray array);
	String getBottomText(JSONArray array);
	String getRightText(JSONArray array);
	String getFromType(JSONArray array);
	int getFromID(JSONArray array);
	String getThumbnailURL(JSONArray array);
	boolean sortableByName();
	boolean sortableByCount();
	boolean sortableByDate();
	void nextNameSort();
	void nextCountSort();
	void nextDateSort();
	String getNameSortLabel();
	String getCountSortLabel();
	String getDateSortLabel();

}

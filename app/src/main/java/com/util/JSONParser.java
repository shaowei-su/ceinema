package com.util;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.CEInema.Configurator;
import com.CEInema.CustomListView;
import com.CEInema.MediaInfoView;
import com.CEInema.CustomListView.RowAdapter;
import com.ListType.ListType;
import com.Model.MediaInfo;
import com.Model.RowObject;

/**
 * Parses JSON data from ColdFusion web service.  Performs the
 * JSONObject creation in an Async task, to separate from
 * UI thread.
 * 
 * Method are passed references so they can update the GUI by themselves
 * @author kstorck
 */
public class JSONParser {
						
	// Appropriate method needs to be appended to end of URL
	// along with other args
	//private String URL = "http://ceitraining.org/web_services/media.cfc?returnformat=json&method=";
	//private String URL = "http://172.18.155.175/web_services/media.cfc?returnformat=json&method=";
	private String URL = Configurator.SERVER_URL + "/web_services/media.cfc?returnformat=json&method=";
	
	private HttpClient httpClient;
	private HttpContext localContext;
	private RowAdapter m_adapter;  // List View reference
	private ListType listType;
	private MediaInfoView mediaInfoView;  // MediaInfoView reference
	private int format_qualityID = 1;

	public int getFormat_qualityID() {
		return format_qualityID;
	}
	public void setFormat_qualityID(int format_qualityID) {
		this.format_qualityID = format_qualityID;
	}
	public JSONParser(RowAdapter m_adapter) {
		this.m_adapter = m_adapter;
	}
	public JSONParser() {
		
	}
	/**
	 * Returns JSONObject for a given query
	 * @param URL
	 * @return jObject
	 */
	public JSONObject getJSON(String URL) {
		httpClient = new DefaultHttpClient();
		localContext = new BasicHttpContext();
		String sResponse = "";
		try {
			HttpGet httpGet = new HttpGet(URL);
			HttpResponse response;
			response = httpClient.execute(httpGet, localContext);
			
			if (response != null) {
				sResponse = EntityUtils.toString(response.getEntity());
			}
			
			JSONObject jObject = new JSONObject(sResponse);
			
			MyProgressDialog.stopDialog();
			
			return jObject;
		} catch (SocketException se) {
			// If network fails, normally comes here
			se.printStackTrace();
			//MyProgressDialog.stopDialog();
			return null;
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage(), e);
			//MyProgressDialog.stopDialog();
			return null;
		}	
	}

	/**
	 * Assigns network connection in getListType  method to Asynchronous task
	 * @param ListType
	 */
	public void getListTypeSafe(ListType listType) {
		GetListTypeTask task = new GetListTypeTask();
		this.listType = listType;
		String new_url = URL + listType.getURLAppendage();
		task.execute(new_url);
	}
	
	/**
	 * Gets JSON in an Async task and passes it on to getListType
	 * @author kstorck
	 */
	private class GetListTypeTask extends AsyncTask<String, Void, JSONObject> {
		
		/**
		 * Get the JSON object
		 */
		protected JSONObject doInBackground(String... urls) {
			String new_url = URL + listType.getURLAppendage();
			return getJSON(new_url);
		}
		
		@Override
		protected void onPostExecute(JSONObject jObject) {
			getListType(listType, jObject);
		}
		
	}

	/**
	 * Returns row objects for a given ListType
	 * 
	 * @param listType - type of List
	 * @param jObject
	 * @return row_object
	 */
	public ArrayList<RowObject> getListType(ListType listType, JSONObject jObject) {
		ArrayList<RowObject> row_objects = new ArrayList<RowObject>();
		
		try {			
			
			// Get the columns
			JSONArray JQueryColumns = jObject.getJSONArray("COLUMNS");
			
			// Get the data
			JSONArray jQueryData = jObject.getJSONArray("DATA");
			
			// Get the columns we want from the ListType object
			HashMap<String, Integer> fieldColumns = listType.getColumns();
			
			// Get the indices for the columns we want
			for (int i = 0; i < JQueryColumns.length(); i++) {
				
				Iterator it = fieldColumns.entrySet().iterator();
				// Find each columns index
				while(it.hasNext()){
					
					Map.Entry pairs = (Map.Entry)it.next();
					if (JQueryColumns.getString(i).equals(pairs.getKey())) {
						fieldColumns.put((String)pairs.getKey(), i);
					}
					
				}
				
			}
			
			// Pass the indicies back to the ListType object
			listType.setColumns(fieldColumns);
			
			// Populate the row objects
			for (int i=0; i < jQueryData.length(); i++) {
				RowObject o = new RowObject();
					
				o.setTopText(listType.getTopText(jQueryData.getJSONArray(i)));
				o.setID(listType.getFromID(jQueryData.getJSONArray(i)));
				o.setBottomText(listType.getBottomText(jQueryData.getJSONArray(i)));
				o.setRightText(listType.getRightText(jQueryData.getJSONArray(i)));
				o.setFromType(listType.getFromType(jQueryData.getJSONArray(i)));
				o.setThumbnailURL(listType.getThumbnailURL(jQueryData.getJSONArray(i)));
				
				
				if (m_adapter != null) {
					m_adapter.add(o);
				}
			}
			
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
		
		return row_objects;
	}
	
	/**
	 * Assigns the network connection in getMediaInfo to Asynchronous task
	 * @param mediaID
	 * @param mediaInfoView
	 */
	public void getMediaInfoSafe(int mediaID, MediaInfoView mediaInfoView) {
		GetMediaInfoTask task = new GetMediaInfoTask();
		checkNetworkSpeed(mediaInfoView.getApplicationContext());
		task.setFormat_qualityID(format_qualityID);
		this.mediaInfoView = mediaInfoView;
		Log.w("JSONParser MediaID", String.valueOf(mediaID));
		task.execute(mediaID);  
	}
	/*public void getMediaQuality(int mediaID, MediaInfoView mediaInfoView) {
		String p_url = URL + "getMediaQuality&mediaID=" + mediaID + "&format_qualityID=" + format_qualityID; 
		getJSON(p_url);
	}	*/
	/**
	 * Gets JSON in an Async task and passes it on to getMediaInfo
	 * @author kstorck
	 */
	private class GetMediaInfoTask extends AsyncTask<Integer, Void, ArrayList<JSONObject>> {
		
		/**
		 * Get all JSON objects needed in getMediaInfo
		 */
		public int format_qualityID = 1;
		
		protected ArrayList<JSONObject> doInBackground(Integer... urls) {
			String p_url = URL + "getAllMedia&mediaID=" + urls[0] + "&format_qualityID=" + format_qualityID;
			
			String obj_url = URL + "getObjectives&mediaID=" + urls[0];
			String guid_url = URL + "getGuidelines&mediaID=" + urls[0];
			Log.w("doInBackground MediaID: ", urls[0].toString());
			
			// For the 3 jObjects needed in getMediaInfo(Media Info, objectives, guidelines)
			ArrayList<JSONObject> jObjects = new ArrayList<JSONObject>();
			jObjects.add(getJSON(p_url));
			jObjects.add(getJSON(obj_url));
			jObjects.add(getJSON(guid_url));
			
			return jObjects;
		}
		
		@Override
		protected void onPostExecute(ArrayList<JSONObject> jObjects) {
			Log.w("JSONParser", "Even here3?");
			getMediaInfo(jObjects, -1, mediaInfoView);
		}

		public int getFormat_qualityID() {
			return format_qualityID;
		}

		public void setFormat_qualityID(int format_qualityID) {
			format_qualityID = format_qualityID;
		}
		
	}
	private boolean checkNetworkSpeed(Context context){
		if (NetworkConnection.isFastNetworkAvailable(context))
			setFormat_qualityID(1);
		else
			setFormat_qualityID(2);
		
		return true;
	}
	
	/**
	 * Returns media information
	 * 
	 * @param mediaID
	 * @return
	 */
	public MediaInfo getMediaInfo(ArrayList<JSONObject> jObjects, int mediaID, MediaInfoView mediaInfoView) {
		MediaInfo mediaInfo = new MediaInfo();
		this.mediaInfoView = mediaInfoView;
		
		checkNetworkSpeed(mediaInfoView.getApplicationContext());
		
		if (jObjects == null) {
			String p_url = URL + "getAllMedia&mediaID=" + mediaID + "&format_qualityID=" + format_qualityID; 
			String obj_url = URL + "getObjectives&mediaID=" + mediaID;
			String guid_url = URL + "getGuidelines&mediaID=" + mediaID;
			
			jObjects = new ArrayList<JSONObject>();
			jObjects.add(getJSON(p_url));
			jObjects.add(getJSON(obj_url));
			jObjects.add(getJSON(guid_url));
		}
		
		try {
			// Get the columns
			JSONObject jObject = jObjects.get(0);
			JSONArray JQueryColumns = jObject.getJSONArray("COLUMNS");
			
			// Get the data
			JSONArray jQueryData = jObject.getJSONArray("DATA");
			
			
			int MEDIAID_IDX = 0;
			int MEDIA_TITLE_IDX = 0;
			int MEDIA_DESCRIPTION_IDX = 0;
			int PRESENTER_FIRST_NAME_IDX = 0;
			int PRESENTER_LAST_NAME_IDX = 0;
			int PRESENTER_CREDENTIAL_IDX = 0;
			int PRESENTER_TITLE_IDX = 0;
			int PRESENTER_EMPLOYER_IDX = 0;
			int MEDIA_DATE_RELEASED_IDX = 0;
			int SPONSOR_NAME_IDX = 0;
			int MEDIA_SOURCE_TYPE_IDX = 0;
			int SPONSORID_IDX = 0;
			int PRESENTERID_IDX = 0;
			int CENTERID_IDX = 0;
			int PRODUCER_NAME_IDX=0; // == CENTER_NAME
			int FORMAT_FILE_NAME_IDX=0;
			int FORMAT_QUALITY_IDX=0;
			int CATEGORYID_IDX=0;
			int CATEGORY_IDX=0;
			int MEDIA_THUMB_PATH_IDX = 0;
			
			// Get the indices of the columns we want
			int len = JQueryColumns.length();
			for (int i = 0; i < len; i++) {
				if (JQueryColumns.getString(i).equals("MEDIAID")) {
					MEDIAID_IDX = i;
				} else if (JQueryColumns.getString(i).equals("MEDIA_TITLE")) {
					MEDIA_TITLE_IDX = i;
				} else if (JQueryColumns.getString(i).equals("MEDIA_DESCRIPTION")) {
					MEDIA_DESCRIPTION_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTER_FIRST_NAME")) {
					PRESENTER_FIRST_NAME_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTER_LAST_NAME")) {
					PRESENTER_LAST_NAME_IDX = i;
				} else if (JQueryColumns.getString(i).equals("MEDIA_DATE_RELEASED")) {
					MEDIA_DATE_RELEASED_IDX = i;
				} else if (JQueryColumns.getString(i).equals("SPONSOR_NAME")) {
					SPONSOR_NAME_IDX = i;
				} else if (JQueryColumns.getString(i).equals("MEDIA_SOURCE_TYPE")) {
					MEDIA_SOURCE_TYPE_IDX = i;
				} else if (JQueryColumns.getString(i).equals("SPONSORID")) {
					SPONSORID_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTERID")) {
					PRESENTERID_IDX = i;
				} else if (JQueryColumns.getString(i).equals("CENTERID")) {
					CENTERID_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRODUCER_NAME")) {
					PRODUCER_NAME_IDX = i;
				//} else if (JQueryColumns.getString(i).equals("MEDIA_PATH")) {
				//	MEDIA_PATH_IDX = i;
				} else if (JQueryColumns.getString(i).contains("FORMAT_FILE_NAME")) {
					FORMAT_FILE_NAME_IDX = i;
				} else if (JQueryColumns.getString(i).contains("FORMAT_QUALITYID")) {
					FORMAT_QUALITY_IDX = i;
				} else if (JQueryColumns.getString(i).equals("CATEGORYID")) {
					CATEGORYID_IDX = i;
				} else if (JQueryColumns.getString(i).equals("CATEGORY")) {
					CATEGORY_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTER_CREDENTIAL")) {
					PRESENTER_CREDENTIAL_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTER_TITLE")) {
					PRESENTER_TITLE_IDX = i;
				} else if (JQueryColumns.getString(i).equals("PRESENTER_EMPLOYER")) {
					PRESENTER_EMPLOYER_IDX = i;
				} else if (JQueryColumns.getString(i).equals("MEDIA_THUMB_PATH")) {
					MEDIA_THUMB_PATH_IDX = i;
				}
			}
			
			// Populate the row objects
			len = jQueryData.length();
			for (int i=0; i < len; i++) {
				
				String title = jQueryData.getJSONArray(i).getString(MEDIA_TITLE_IDX);
				String description = jQueryData.getJSONArray(i).getString(MEDIA_DESCRIPTION_IDX);
				
				String first_name="";
				if (!jQueryData.getJSONArray(i).isNull(PRESENTER_FIRST_NAME_IDX)) {
					first_name = jQueryData.getJSONArray(i).getString(PRESENTER_FIRST_NAME_IDX);
				}
				String last_name="";
				if (!jQueryData.getJSONArray(i).isNull(PRESENTER_LAST_NAME_IDX)) {
					last_name = jQueryData.getJSONArray(i).getString(PRESENTER_LAST_NAME_IDX);
				}
				String presenter_credential="";
				if (!jQueryData.getJSONArray(i).isNull(PRESENTER_CREDENTIAL_IDX)) {
					presenter_credential = jQueryData.getJSONArray(i).getString(PRESENTER_CREDENTIAL_IDX);
				}
				String presenter_title="";
				if (!jQueryData.getJSONArray(i).isNull(PRESENTER_TITLE_IDX)) {
					presenter_title = jQueryData.getJSONArray(i).getString(PRESENTER_TITLE_IDX);
				}
				String presenter_employer="";
				if (!jQueryData.getJSONArray(i).isNull(PRESENTER_EMPLOYER_IDX)) {
					presenter_employer = jQueryData.getJSONArray(i).getString(PRESENTER_EMPLOYER_IDX);
				}
				
				String sponsor = jQueryData.getJSONArray(i).getString(SPONSOR_NAME_IDX);
				String source_type = jQueryData.getJSONArray(i).getString(MEDIA_SOURCE_TYPE_IDX);
				String date = jQueryData.getJSONArray(i).getString(MEDIA_DATE_RELEASED_IDX);
				
				String center_name = jQueryData.getJSONArray(i).getString(PRODUCER_NAME_IDX);
				
				String url = jQueryData.getJSONArray(i).getString(FORMAT_FILE_NAME_IDX);
				int format_quality_id = jQueryData.getJSONArray(i).getInt(FORMAT_QUALITY_IDX);
				
				String category = jQueryData.getJSONArray(i).getString(CATEGORY_IDX);
				
				int sponsorID=-1;
				if (!jQueryData.getJSONArray(i).isNull(SPONSORID_IDX)) {
					sponsorID = jQueryData.getJSONArray(i).getInt(SPONSORID_IDX);
				}
				int presenterID=-1;
				if (!jQueryData.getJSONArray(i).isNull(PRESENTERID_IDX)) {
					presenterID = Integer.valueOf(jQueryData.getJSONArray(i).getInt(PRESENTERID_IDX));
				}
				int centerID=-1;
				if (!jQueryData.getJSONArray(i).isNull(CENTERID_IDX)) {
					centerID = Integer.valueOf(jQueryData.getJSONArray(i).getInt(CENTERID_IDX));
				}
				int categoryID=-1;
				if (!jQueryData.getJSONArray(i).isNull(CATEGORYID_IDX)) {
					categoryID = Integer.valueOf(jQueryData.getJSONArray(i).getInt(CATEGORYID_IDX));
				}
				int media_id = -1;
				if (!jQueryData.getJSONArray(i).isNull(MEDIAID_IDX)) {
					media_id = Integer.valueOf(jQueryData.getJSONArray(i).getInt(MEDIAID_IDX));
				}
				
				String thumbnailURL="";
				if (!jQueryData.getJSONArray(i).isNull(MEDIA_THUMB_PATH_IDX)) {
					thumbnailURL = jQueryData.getJSONArray(i).getString(MEDIA_THUMB_PATH_IDX);
				}
				
				ArrayList<String> objectives = getObjectives(jObjects.get(1));
				HashMap<String, String> guidelines = getGuidelines(jObjects.get(2));
				
				mediaInfo.setObjectives(objectives);
				mediaInfo.setTitle(title);
				mediaInfo.setID(media_id);
				mediaInfo.setDescription(description);
				mediaInfo.setPresenterFirst(first_name);
				mediaInfo.setPresenterLast(last_name);
				mediaInfo.setSponsor(sponsor);
				mediaInfo.setSourceType("source type");
				mediaInfo.setDate(date);
				mediaInfo.setSponsorID(sponsorID);
				mediaInfo.setPresenterID(presenterID);
				mediaInfo.setCenterID(centerID);
				mediaInfo.setCenterName(center_name);
				mediaInfo.setSourceType(source_type);
				mediaInfo.setURL(url);
				mediaInfo.setFormat_quality(format_quality_id);
				mediaInfo.setCategory(category);
				mediaInfo.setCategoryID(categoryID);
				mediaInfo.setPresenterCredential(presenter_credential);
				mediaInfo.setPresenterEmployer(presenter_employer);
				mediaInfo.setPresenterTitle(presenter_title);
				mediaInfo.setThumbnailPath(thumbnailURL);
				mediaInfo.setGuidelines(guidelines);
				
			}
			
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage(), e);
		}
		
		if (mediaInfoView != null) {
			Log.w("Bef MediaID: ", String.valueOf(mediaInfo.getID()));
			mediaInfoView.setMediaTexts(mediaInfo);
		}
		
		return mediaInfo;
	}
	
	/**
	 * Helper method. Returns list of objectives for a given media
	 * @param jObject
	 * @return
	 */
	public ArrayList<String> getObjectives(JSONObject jObject) {
		ArrayList<String> objectives = new ArrayList<String>();	
	
		try {
			// Get the columns
			JSONArray JQueryColumns = jObject.getJSONArray("COLUMNS");
			
			// Get the data
			JSONArray jQueryData = jObject.getJSONArray("DATA");
			
			int LEARNING_OBJECTIVE_IDX=-1;
			
			// Get the indices of the columns we want
			int len = JQueryColumns.length();
			for (int i = 0; i < len; i++) {
				if (JQueryColumns.getString(i).equals("LEARNING_OBJECTIVE")) {
					LEARNING_OBJECTIVE_IDX = i;
				}
			}
			
			// Populate the row objects
			len = jQueryData.length();
			for (int i=0; i < len; i++) {
				objectives.add(jQueryData.getJSONArray(i).getString(LEARNING_OBJECTIVE_IDX));
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return objectives;
	}
	
	/**
	 * Helper method. Returns a Hashmap of the guidelines for a given media.
	 * HashMap: Guideline name : guideline URL
	 * @param jObject
	 * @return
	 */
	public HashMap<String, String> getGuidelines(JSONObject jObject) {
		
		// Guideline(String) -> guideline URL(String)
		HashMap<String, String> guidelines = new HashMap<String, String>();
			
	
		try {
			// Get the columns
			JSONArray JQueryColumns = jObject.getJSONArray("COLUMNS");
			
			// Get the data
			JSONArray jQueryData = jObject.getJSONArray("DATA");
			
			int GUIDELINE_TITLE_IDX=-1;
			int GUIDELINE_URL_IDX=-1;
			
			// Get the indices of the columns we want
			int len = JQueryColumns.length();
			for (int i = 0; i < len; i++) {
				if (JQueryColumns.getString(i).equals("GUIDELINE_TITLE")) {
					GUIDELINE_TITLE_IDX = i;
				} else if (JQueryColumns.getString(i).equals("GUIDELINE_URL")) {
					GUIDELINE_URL_IDX = i;
				}
			}
			
			// Populate the row objects
			len = jQueryData.length();
			for (int i=0; i < len; i++) {
				String title="";
				if (!jQueryData.getJSONArray(i).isNull(GUIDELINE_TITLE_IDX)) {
					title = jQueryData.getJSONArray(i).getString(GUIDELINE_TITLE_IDX);
				}
				String url="";
				if (!jQueryData.getJSONArray(i).isNull(GUIDELINE_URL_IDX)) {
					url = jQueryData.getJSONArray(i).getString(GUIDELINE_URL_IDX);
				}
				
				guidelines.put(title, url);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return guidelines;
	}
}

package com.CEInema;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ListType.Categories;
import com.ListType.Centers;
import com.ListType.ListType;
import com.ListType.Presenters;
import com.ListType.Sponsors;
import com.ListType.Videos;
import com.Model.RowObject;
import com.util.ImageDownloader;
import com.util.JSONParser;
import com.util.MyProgressDialog;

/**
 * Represents a custom List View and Row Adapter for holding our 
 * custom row objects (relative_list_item.xml).  Row Objects are
 * media channels or videos. Handles clicks on a row object and 
 * starts next activity. 
 * 
 * @author kstorck
 */
public class CustomListView extends ListActivity{

    private ArrayList<RowObject> row_objects = null; // Elements of the List
    private RowAdapter m_adapter;
    private Bundle extras;
    
    private ListType listType = null;
    private JSONParser jsonParser;
    
    private final int SORT_BY = 0;
    
    private final int SORT_BY_NAME = Menu.FIRST;
    private final int SORT_BY_COUNT = SORT_BY_NAME + 1;
    private final int SORT_BY_DATE = SORT_BY_COUNT + 1;

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the extras passed to us
		extras = getIntent().getExtras();
		
		// Set title if found ( when not in tabs )
		if(extras != null && extras.containsKey("title")){
			
			//Create the custom title, because we're not a tab
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			
			setContentView(R.layout.list_view);
			
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
			
			TextView title=(TextView)findViewById(R.id.title);
		    title.setText(extras.getString("title"));
		    
		    ImageView icon = (ImageView)findViewById(R.id.icon);
		    icon.setBackgroundResource(R.drawable.ic_menu_archive);
		    
		// No title because we're in tabs view
		} else {
			setContentView(R.layout.list_view);
		}
		
		// Find out what type of list to make
		parseList(getListType());
		
	}
	
	/**
	 * Determines the right type of list to make
	 */
	public ListType getListType() {
		// Find what type of list to make
		if(extras != null && extras.containsKey("list_type")) {
			
			int notID = -1;
			int presenter_id = -1;
			int sponsor_id = -1;
			int center_id = -1;
			int category_id = -1;
			//int format_qualityID = -1;
			
			
			// Find any ID's as parameters
			if (extras.containsKey("videos")) {
				String videos = extras.getString("videos");
				if (videos.equals("PRESENTER")) {
					presenter_id = extras.getInt("from_id");	
				}
				if (videos.equals("SPONSOR")) {
					sponsor_id = extras.getInt("from_id");		
				}
				if (videos.equals("CENTER")) {
					center_id = extras.getInt("from_id");				
				}
				if (videos.equals("CATEGORY")) {
					category_id = extras.getInt("from_id");				
				}
				if (extras.containsKey("not_id")) {
					notID = extras.getInt("not_id");
				}
			}

			String type = extras.getString("list_type");
			// Create the appropriate category
			if(type.equals("PRESENTER")) {
				listType = new Presenters();
			}
			if(type.equals("SPONSOR")) {
				listType = new Sponsors();
			}
			if(type.equals("CENTER")) {
				listType = new Centers();
			}
			if(type.equals("CATEGORY")) {
				listType = new Categories();
			}
			if(type.equals("VIDEOS")) {
				listType = new Videos(notID, presenter_id, sponsor_id, center_id, category_id);
				Log.w("getListType notID, presenterIF",notID + "  "  + presenter_id);
			}
			
			return listType;
		}
		return null;
	}
	
	/**
	 * Starts creating the list. Instantiate the array of RowObjects
	 * and set the RowAdapter.  Then call the appropriate method on JSONParser to
	 * get the RowObject data.
	 * 
	 * Also handles clicks on the RowObjects.  If click was on a media channel, open up a new
	 * CustomListView by itself of its videos.  If click was on a video, open up
	 * MediaInfoView.
	 * 
	 * @param listType
	 */
	public void parseList(ListType listType) {
		
		row_objects = new ArrayList<RowObject>();
		this.m_adapter = new RowAdapter(this, R.layout.relative_list_item, row_objects);
		setListAdapter(this.m_adapter);	

		// Pass the parser a reference to the list adapter, so it can update it
		// itself
		jsonParser = new JSONParser(m_adapter);
		
		// Start the Async network loading
		jsonParser.getListTypeSafe(listType);
		
		// Start the dialog
		MyProgressDialog.startDialog(this, "Please Wait", "Loading");  
		
		ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        // List click listener
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
			      int position, long id) {	
	
				
				// Find what type of row object it is
				String from_type = ((TextView)view.findViewById(R.id.from_type)).getText().toString();
				
				// Get the ID associated with the object
				int from_id = Integer.parseInt(((TextView)view.findViewById(R.id.from_id)).getText().toString());
				
	      	  	Intent intent = new Intent();
	      	  	
	      	  	// Click came from a category
	      	  	if (!from_type.equals("VIDEO")) {
	      	  		Log.w("MediaID before", String.valueOf(from_id));
	      	  		
	      	  		// Creating another List, with the videos in the clicked category
	      	  		intent.setClass(view.getContext(), CustomListView.class);
	      	  		intent.putExtra("videos", from_type);
	      	  		intent.putExtra("list_type", "VIDEOS");
	      	  		intent.putExtra("from_id", from_id);
	      	  		intent.putExtra("title", ((TextView)view.findViewById(R.id.topText)).getText());
	      	  		startActivity(intent);
	      	  		
	      	  	// Click came from a video
	      	  	} else {
	      	  		
	      	  		// Create MediaInfoView
	      	  		intent.setClass(view.getContext(), MediaInfoView.class);
	      	  		intent.putExtra("from_id", from_id);
	      	  		startActivity(intent);
	      	  	}     	  	
			}
		});
	}

	/**
	 * Loads the options menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.menu, menu);
	    SubMenu fileMenu = menu.addSubMenu("Sort By");
	    fileMenu.setIcon(R.drawable.ic_menu_sort_by_size);
	    if (listType.sortableByName()) {
	    	fileMenu.add(SORT_BY, SORT_BY_NAME, 0, listType.getNameSortLabel());
	    }
        if (listType.sortableByCount()) {
        	fileMenu.add(SORT_BY, SORT_BY_COUNT, 0, listType.getCountSortLabel());
        }
        if (listType.sortableByDate()) {
        	fileMenu.add(SORT_BY, SORT_BY_DATE, 0, listType.getDateSortLabel());
        }
	    return true;
	}
	
	/**
	 * Handles Menu item click
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		
			// Load the information web page
	        case R.id.about:     
	        	intent = new Intent();
	    		intent.setClass(this, MyWebView.class);
	    		intent.putExtra("URL", Configurator.SERVER_URL + "/app/information.html");
	    		startActivity(intent);
	    		break;
	    		
	    	// Load home page
	        case R.id.home:
	        	intent = new Intent();
	    		intent.setClass(this, FrontPage.class);
	    		listType = new Categories();
	    		
	    		// If home is already open, use previous instance and clear stack
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		
	    		startActivity(intent);
	    		break;
	    		
	    		// Refresh the current ListView
	        case R.id.refresh:     
	        	parseList(listType);
	    		break;
	        case SORT_BY_NAME:     
	        	listType.nextNameSort();
	        	parseList(listType);
	    		break;
	        case SORT_BY_COUNT:     
	        	listType.nextCountSort();
	        	parseList(listType);
	        	break;
	        case SORT_BY_DATE:     
	        	listType.nextDateSort();
	        	parseList(listType);
	        	break;
	    }
	    return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Custom adapter for creating our custom row objects (relative_list_item).
	 * Relative list items have 3 text fields(top, bottom, and right) and 2 hidden fields
	 * (from_type & from_id) which store the type of row object they are (channel or video) 
	 * and the id associated with that row object.
	 * 
	 * @author kstorck
	 */
	public class RowAdapter extends ArrayAdapter<RowObject>{
		private ArrayList<RowObject> items; // List items ( row objects )
		
		private final ImageDownloader imageDownloader = new ImageDownloader();
		
		public RowAdapter(Context context, int textViewResourceId, ArrayList<RowObject> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;

	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.relative_list_item, null);
	            }
	            
	            RowObject o = items.get(position);
	            
	            // Populate components of relative_list_item from a row object.
	            if (o != null) {
	                    TextView tt = (TextView) v.findViewById(R.id.topText);
	                    TextView bt = (TextView) v.findViewById(R.id.bottomText);
	                    TextView rt = (TextView) v.findViewById(R.id.rightText);
	                    TextView ot = (TextView) v.findViewById(R.id.from_type);
	                    TextView idt = (TextView) v.findViewById(R.id.from_id);
	                    ImageView iv = (ImageView) v.findViewById(R.id.listIcon);
	                    if(tt != null) {
	                          tt.setText(o.getTopText());                            
	                    }
	                    if(bt != null){
	                          bt.setText(Html.fromHtml("<i>" + o.getBottomText() + "</i>"));
	                    }
	                    if(rt != null){
	                          rt.setText(o.getRightText());
	                    }
	                    if(ot != null){
	                          ot.setText(o.getFromType());
	                    }
	                    if(idt != null){
	                          idt.setText(Integer.toString(o.getID()));
	                    }
	                    //video list
	                    if (iv != null) {  
	                    	
	                    	// If video, load its thumbnail
	                    	if (o.getFromType().equals("VIDEO")) {						
	                    		String thumbnail = Configurator.SERVER_URL  + "/resources/" + o.getThumbnailURL().replace("_high.jpg","_low.gif");
	                    		if (!VideoPlayer.exists(thumbnail))
	                    			thumbnail = Configurator.SERVER_URL  + "/resources/images/no_image.gif";
	                    		imageDownloader.download(thumbnail, (ImageView)iv);
								
	                    		// Red
								tt.setTextColor(0xFFcf2e1a);
								
							// If category, just load folder icon
		                    } else {
	                    		iv.setBackgroundResource(R.drawable.ic_menu_archive);

	                    		// Blue
	                    		tt.setTextColor(0xFF000000);
	                    	}
	                   }
	            }
	            return v;
	    }
	}
}
	

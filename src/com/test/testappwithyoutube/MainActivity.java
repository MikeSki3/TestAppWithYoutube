package com.test.testappwithyoutube;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class MainActivity extends Activity {
	
	ListView videoList;
	ImageView videoThumb;
	TextView videoTitle;
	YouTubeThumbnailView thumbView;
	ArrayList<YouTubeThumbnailView> thumbViews = new ArrayList<YouTubeThumbnailView>();
	YouTubeThumbnailLoader thumbnailLoader;
	
	public final String DEV_KEY = "";
	public final String PLAYLIST_ID = "PLAC90ED58CDC8A748";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		videoList = (ListView) findViewById(R.id.video_list);
		videoThumb = (ImageView) findViewById(R.id.video_thumb);
		videoTitle = (TextView) findViewById(R.id.video_title);
		
		thumbView = new YouTubeThumbnailView(this);
		thumbView.initialize(DEV_KEY, new YouTubeThumbnailView.OnInitializedListener() {

			@Override
			public void onInitializationFailure(YouTubeThumbnailView thumbView, YouTubeInitializationResult result) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onInitializationSuccess(YouTubeThumbnailView thumbView, YouTubeThumbnailLoader thumbLoader) {
			    thumbnailLoader = thumbLoader;
			    thumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
					
					@Override
					public void onThumbnailLoaded(YouTubeThumbnailView thumbnail, String videoId) {
						thumbViews.add(thumbnail);
						if(thumbnailLoader.hasNext())
							thumbnailLoader.next();
						else {
							
						}
						
					}
					
					@Override
					public void onThumbnailError(YouTubeThumbnailView arg0, ErrorReason arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			    thumbnailLoader.setPlaylist(PLAYLIST_ID);
				
			}
			
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

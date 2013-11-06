package com.test.testappwithyoutube;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.test.testappwithyoutube.adapter.VideoListAdapter;

public class MainActivity extends Activity {
	
	ListView videoList;
	ImageView videoThumb;
	TextView videoTitle;
	YouTubeThumbnailView thumbView;
	ArrayList<YouTubeThumbnailView> thumbViews = new ArrayList<YouTubeThumbnailView>();
	VideoListAdapter videoAdapter;
	YouTubeThumbnailLoader thumbnailLoader;
	Activity mActivity;
	
	public final String DEV_KEY = "";
	public final String PLAYLIST_ID = "ECAE6B03CA849AD332";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		
		videoList = (ListView) findViewById(R.id.video_list);
		videoThumb = (ImageView) findViewById(R.id.video_thumb);
		videoTitle = (TextView) findViewById(R.id.video_title);
		
		thumbView = new YouTubeThumbnailView(this);
		thumbView.initialize(DEV_KEY, new YouTubeThumbnailView.OnInitializedListener() {

			@Override
			public void onInitializationFailure(YouTubeThumbnailView thumbView, YouTubeInitializationResult result) {
				// TODO Auto-generated method stub
				Log.i("initFail", result.toString());
				
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
							videoAdapter = new VideoListAdapter(mActivity, thumbViews);
							videoList.setAdapter(videoAdapter);
						}
						
					}
					
					@Override
					public void onThumbnailError(YouTubeThumbnailView arg0, ErrorReason arg1) {
						Log.i("initFail", arg1.toString());
						
					}
				});
			    thumbnailLoader.setPlaylist(PLAYLIST_ID);
			    thumbnailLoader.first();
				
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

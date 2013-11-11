package com.test.testappwithyoutube;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.test.testappwithyoutube.adapter.VideoListAdapter;

public class MainActivity extends Activity implements
YouTubeThumbnailView.OnInitializedListener {
	
	ListView videoList;
	ImageView videoThumb;
	TextView videoTitle;
	YouTubeThumbnailView thumbnailView;
	ArrayList<YouTubeThumbnailView> thumbViews = new ArrayList<YouTubeThumbnailView>();
	VideoListAdapter videoAdapter;
	YouTubeThumbnailLoader thumbnailLoader;
	Activity mActivity;
	private Dialog errorDialog;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private boolean activityResumed;
	private boolean nextThumbnailLoaded;

	
	public final String DEV_KEY = "AIzaSyCjTn28kwRwkv-f2-hN041rtfsoOZ6OT0o";
	public final String PLAYLIST_ID = "ECAE6B03CA849AD332";

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		mActivity = this;
//		
//		videoList = (ListView) findViewById(R.id.video_list);
//		videoThumb = (ImageView) findViewById(R.id.video_thumb);
//		videoTitle = (TextView) findViewById(R.id.video_title);
//		
//		thumbView = new YouTubeThumbnailView(mActivity);
//		thumbView.initialize(DEV_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//
//			@Override
//			public void onInitializationFailure(YouTubeThumbnailView thumbView, YouTubeInitializationResult result) {
//				// TODO Auto-generated method stub
//				Log.i("initFail", result.toString());
//				
//			}
//
//			@Override
//			public void onInitializationSuccess(YouTubeThumbnailView thumbView, YouTubeThumbnailLoader thumbLoader) {
//			    thumbnailLoader = thumbLoader;
//			    thumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//					
//					@Override
//					public void onThumbnailLoaded(YouTubeThumbnailView thumbnail, String videoId) {
//						thumbViews.add(thumbnail);
//						if(thumbnailLoader.hasNext())
//							thumbnailLoader.next();
//						else {
//							videoAdapter = new VideoListAdapter(mActivity, thumbViews);
//							videoList.setAdapter(videoAdapter);
//						}
//						
//					}
//					
//					@Override
//					public void onThumbnailError(YouTubeThumbnailView arg0, ErrorReason arg1) {
//						Log.i("thumbLoadErr", arg1.toString());
//						
//					}
//				});
//			    thumbnailLoader.setPlaylist(PLAYLIST_ID);
//			    //thumbnailLoader.first();
//				
//			}
//			
//		});
//		
//		
//		
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	
	/*
	 * Copyright 2012 Google Inc. All Rights Reserved.
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
	
	  private State state;

	  private enum State {
	    UNINITIALIZED,
	    LOADING_THUMBNAILS,
	    VIDEO_FLIPPED_OUT,
	    VIDEO_LOADING,
	    VIDEO_CUED,
	    VIDEO_PLAYING,
	    VIDEO_ENDED,
	    VIDEO_BEING_FLIPPED_OUT,
	  }

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    state = State.UNINITIALIZED;

	    thumbnailView = new YouTubeThumbnailView(this);
	    thumbnailView.initialize(DEV_KEY, this);

	    setContentView(R.layout.activity_main);
	  }

	  @Override
	  public void onInitializationSuccess(YouTubeThumbnailView thumbnailView,
	      YouTubeThumbnailLoader thumbnailLoader) {
	    this.thumbnailLoader = thumbnailLoader;
	    thumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailListener());
	    maybeStartDemo();
	  }

	  @Override
	  public void onInitializationFailure(
	      YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
	    if (errorReason.isUserRecoverableError()) {
	      if (errorDialog == null || !errorDialog.isShowing()) {
	        errorDialog = errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST);
	        errorDialog.show();
	      }
	    } else {
	      String errorMessage =
	          String.format("An error occured", errorReason.toString());
	      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
	    }
	  }
	  
	  private void maybeStartDemo() {
	    if (thumbnailLoader != null
	        && state.equals(State.UNINITIALIZED)) {
	      thumbnailLoader.setPlaylist(PLAYLIST_ID); // loading the first thumbnail will kick off demo
	      state = State.LOADING_THUMBNAILS;
	    }
	  }

	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == RECOVERY_DIALOG_REQUEST) {
	      // Retry initialization if user performed a recovery action
	      if (errorDialog != null && errorDialog.isShowing()) {
	        errorDialog.dismiss();
	      }
	      errorDialog = null;
	      thumbnailView.initialize(DEV_KEY, this);
	    }
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    activityResumed = true;
	    if (thumbnailLoader != null) {
	      if (state.equals(State.UNINITIALIZED)) {
	        maybeStartDemo();
	      } else if (state.equals(State.LOADING_THUMBNAILS)) {
	        loadNextThumbnail();
	      }
	    }
	  }

	  @Override
	  protected void onPause() {
	    activityResumed = false;
	    super.onPause();
	  }

	  @Override
	  protected void onDestroy() {
	    if (thumbnailLoader != null) {
	      thumbnailLoader.release();
	    }
	    super.onDestroy();
	  }

	  private void loadNextThumbnail() {
	    if (thumbnailLoader.hasNext()) {
	      thumbnailLoader.next();
	    } else {
	      thumbnailLoader.first();
	    }
	  }

	  /**
	   * An internal listener which listens to thumbnail loading events from the
	   * {@link YouTubeThumbnailView}.
	   */
	  private final class ThumbnailListener implements
	      YouTubeThumbnailLoader.OnThumbnailLoadedListener {

	    @Override
	    public void onThumbnailLoaded(YouTubeThumbnailView thumbnail, String videoId) {
	      nextThumbnailLoaded = true;

	      if (activityResumed) {
	        if (state.equals(State.LOADING_THUMBNAILS)) {
	          loadNextThumbnail();
	        }
	      }
	    }

	    @Override
	    public void onThumbnailError(YouTubeThumbnailView thumbnail,
	        YouTubeThumbnailLoader.ErrorReason reason) {
	      loadNextThumbnail();
	    }

	  }

	}

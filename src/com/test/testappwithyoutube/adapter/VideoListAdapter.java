package com.test.testappwithyoutube.adapter;

import java.util.ArrayList;

import com.test.testappwithyoutube.R;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.android.youtube.player.YouTubeThumbnailView;

public class VideoListAdapter extends ArrayAdapter<YouTubeThumbnailView> {

	public VideoListAdapter(Context context, ArrayList<YouTubeThumbnailView> objects) {
		super(context, R.layout.list_item_video, objects);
	}

}

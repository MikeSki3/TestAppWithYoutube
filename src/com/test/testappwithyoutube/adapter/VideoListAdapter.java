package com.test.testappwithyoutube.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.test.testappwithyoutube.R;

public class VideoListAdapter extends ArrayAdapter<YouTubeThumbnailView> {
	
	private final Context context;
	private final ArrayList<YouTubeThumbnailView> values;

	public VideoListAdapter(Context context, ArrayList<YouTubeThumbnailView> objects) {
		super(context, R.layout.list_item_video, objects);
		this.context = context;
		this.values = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item_video, parent, false);

		ImageView vidThumb = (ImageView) rowView.findViewById(R.id.video_thumb);
		vidThumb.setImageDrawable(values.get(position).getDrawable());
		
		return rowView;
		
	}
}

package com.givenjazz.android;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Integer> mResources;
    private Context mContext;
    private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();

    public ImageAdapter(Context c, ArrayList<Integer> resources) {
	mContext = c;
	mResources = resources;
    }

    @Override
    public int getCount() {
	return mResources.size();
    }

    @Override
    public Object getItem(int position) {
	return mResources.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    public void recycle() {
	RecycleUtils.recursiveRecycle(mRecycleList);
    }

    public void recycleHalf() {
	int halfSize = mRecycleList.size() / 2;
	List<WeakReference<View>> recycleHalfList = mRecycleList.subList(0,
		halfSize);
	RecycleUtils.recursiveRecycle(recycleHalfList);
	for (int i = 0; i < halfSize; i++)
	    mRecycleList.remove(0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ImageView i = new ImageView(mContext);

	try {
	    i.setImageResource(mResources.get(position));
	} catch (OutOfMemoryError e) {
	    if (mRecycleList.size() <= parent.getChildCount()) {
		throw e;
	    }
	    Log.w(this + "", e.toString());
	    recycleHalf();
	    System.gc();
	    return getView(position, convertView, parent);
	}
	i.setAdjustViewBounds(true);
	i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
		LayoutParams.MATCH_PARENT));
	mRecycleList.add(new WeakReference<View>(i));
	return i;
    }
}

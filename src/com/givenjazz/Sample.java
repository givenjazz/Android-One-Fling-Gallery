package com.givenjazz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.givenjazz.android.GalleryNavigator;
import com.givenjazz.android.ImageAdapter;
import com.givenjazz.android.OneFlingGallery;
import com.givenjazz.android.RecycleUtils;

public class Sample extends Activity {

	private ImageAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayList<Integer> mResourceList = createResourceList("image_%02d");
		mAdapter = new ImageAdapter(this, mResourceList);

		OneFlingGallery gallery = (OneFlingGallery) findViewById(R.id.gallery);
		final GalleryNavigator navi = (GalleryNavigator) findViewById(R.id.navi);
		navi.setSize(mResourceList.size());

		gallery.setAdapter(mAdapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				navi.setPosition(id);
				navi.invalidate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	protected void onDestroy() {
		mAdapter.recycle();
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}

	public ArrayList<Integer> createResourceList(String format) {
		ArrayList<Integer> resourceList = new ArrayList<Integer>();
		int index = 1;
		int resId;
		final Resources resources = getResources();
		final String packageName = getPackageName();

		while (true) {
			String name = String.format(format, index);
			resId = resources.getIdentifier(name, "drawable", packageName);
			if (resId != 0) {
				resourceList.add(resId);
			} else {
				break;
			}
			index++;
		}
		return resourceList;
	}
}

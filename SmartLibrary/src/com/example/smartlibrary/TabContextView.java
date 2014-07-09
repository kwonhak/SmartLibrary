package com.example.smartlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TabContextView extends android.support.v4.app.Fragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_presentation_viewer, container, false);

		return v;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}



}

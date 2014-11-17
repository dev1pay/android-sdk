package com.mwork.onepaysdk.models;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwork.onepaysdk.R;

public class LanguageAdapter extends ArrayAdapter<Language>{
	private Activity context;
	ArrayList<Language> lang;
	public LanguageAdapter(Activity context, int resource, ArrayList<Language> lang) {
		super(context, resource, lang);
		this.context = context;
		this.lang = lang;
	}
	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomView(position, cnvtView, prnt);
	} 
	@Override 
	public View getView(int pos, View cnvtView, ViewGroup prnt) { 
		return getCustomView(pos, cnvtView, prnt);
	}
	public View getCustomView(int position, View convertView, ViewGroup parent) { 
		LayoutInflater inflater =context.getLayoutInflater(); 
		View mySpinner = inflater.inflate(R.layout.spinner_row, parent, false);
		Language current = lang.get(position);
		TextView name = (TextView) mySpinner .findViewById(R.id.name); 
		name.setText(current.getName());
		if (current.getTf() != null) {
			name.setTypeface(current.getTf());
		}
		TextView valuename = (TextView) mySpinner .findViewById(R.id.valuename); 
		valuename.setText(current.getValuename()); 
		ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.flag); 
		left_icon.setImageResource(current.getResourceId());
		return mySpinner;
	}
}

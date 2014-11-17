package com.mwork.onepaysdk.models;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mwork.onepaysdk.R;

public class SmsPlusAdapter extends ArrayAdapter<ItemTypeSmsPlus>{
	private Activity context;
	ArrayList<ItemTypeSmsPlus> typeSMSPlus;
	public SmsPlusAdapter(Activity context, int resource, ArrayList<ItemTypeSmsPlus> typeSMSPlus) {
		super(context, resource, typeSMSPlus);
		this.context = context;
		this.typeSMSPlus = typeSMSPlus;
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
		View mySpinner = inflater.inflate(R.layout.item_type, parent, false);
		ItemTypeSmsPlus item = typeSMSPlus.get(position);
		TextView name = (TextView) mySpinner .findViewById(R.id.nametype); 
		name.setText(item.getName());
		if (item.getTf() != null) {
			name.setTypeface(item.getTf());
		}
		return mySpinner;
	}
}

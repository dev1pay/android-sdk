package com.mwork.onepaysdk.models;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mwork.onepaysdk.R;

public class TypeAdapter extends ArrayAdapter<ItemTypeCard>{
	private Activity context;
	ArrayList<ItemTypeCard> typeCard;
	public TypeAdapter(Activity context, int resource, ArrayList<ItemTypeCard> typeCard) {
		super(context, resource, typeCard);
		this.context = context;
		this.typeCard = typeCard;
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
		ItemTypeCard item = typeCard.get(position);
		TextView name = (TextView) mySpinner .findViewById(R.id.nametype); 
		name.setText(item.getName());
		if (item.getTf() != null) {
			name.setTypeface(item.getTf());
		}
		return mySpinner;
	}
}

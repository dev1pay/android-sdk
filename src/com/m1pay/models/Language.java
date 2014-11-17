package com.m1pay.models;

import android.graphics.Typeface;

public class Language {
	private String name;
	private int resourceId;
	private String valuename;
	private Typeface tf;


	public Language(String name, int resourceId, String valuename, Typeface tf) {
		super();
		this.name = name;
		this.resourceId = resourceId;
		this.valuename = valuename;
		this.tf = tf;
	}

	public String getValuename() {
		return valuename;
	}

	public String getName() {
		return name;
	}


	public Typeface getTf() {
		return tf;
	}

	public int getResourceId() {
		return resourceId;
	}

	@Override
	public String toString() {

		return getName();

	}
}

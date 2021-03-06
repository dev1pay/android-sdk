package com.m1pay.models;

import android.graphics.Typeface;

public class ItemTypeSms {
	private String name;
	private Typeface tf;

	public Typeface getTf() {
		return tf;
	}

	public void setTf(Typeface tf) {
		this.tf = tf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemTypeSms(String name, Typeface tf) {
		super();
		this.name = name;
		this.tf = tf;
	}
}

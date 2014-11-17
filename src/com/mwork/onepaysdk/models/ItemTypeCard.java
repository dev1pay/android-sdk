package com.mwork.onepaysdk.models;

import android.graphics.Typeface;

public class ItemTypeCard {
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

	public ItemTypeCard(String name, Typeface tf) {
		super();
		this.name = name;
		this.tf = tf;
	}
}

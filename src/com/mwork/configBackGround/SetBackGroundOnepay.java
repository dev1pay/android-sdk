package com.mwork.configBackGround;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class SetBackGroundOnepay {
	public static GradientDrawable backgroundButton(String hexbgcolor, float cornerRadius) {
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Color.parseColor(hexbgcolor));
		//gdDefault.setStroke(strokeWidth, strokeColor); mau vien
		gdDefault.setCornerRadius(cornerRadius);
		return gdDefault;
	}
	public static GradientDrawable backgroundEditText(String hexbgcolor, int strokeWidth, String strokeColor, float cornerRadius) {
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Color.parseColor(hexbgcolor));
		gdDefault.setStroke(strokeWidth,Color.parseColor(strokeColor)); 
		gdDefault.setCornerRadius(cornerRadius);
		return gdDefault;
	}
}

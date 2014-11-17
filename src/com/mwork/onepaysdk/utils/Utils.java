
package com.mwork.onepaysdk.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;

public class Utils {
	public static Bitmap getBitmapFromAsset(Context context, String strName) {
		AssetManager assetManager = context.getAssets();
		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			Log.e(Utils.class.getName(), "" + e.getMessage());
			return null;
		}
		return bitmap;
	}

	public static String readFileFromAssest(Context context, String path) throws IOException {
		String str = null;
		AssetManager am = context.getAssets();
		InputStream is = am.open(path);
		int length = is.available();
		byte[] data = new byte[length];
		is.read(data);
		str = new String(data);
		return str;
	}

	public static Bitmap getBitmapFromRaw(Context context, int rawId) {
		InputStream istr;
		Bitmap bitmap = null;
		istr = context.getResources().openRawResource(rawId);
		bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

	public static String readFileFromRaw(Context context, int rawId) throws IOException {
		String str = null;
		InputStream is = context.getResources().openRawResource(rawId);
		int length = is.available();
		byte[] data = new byte[length];
		is.read(data);
		str = new String(data);
		return str;
	}
	public static Typeface getTypeFace(Context context,String fontName){
		Typeface tf;
		try {          
			tf = Typeface.createFromAsset(context.getAssets(), "fonts/"+fontName+".ttf");                 
		}           
		catch (Exception e) {         
			return null;
		} 
		return tf;
	}
}


package com.m1pay.M1PaySDK;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import com.m1pay.models.Carrier;
import com.m1pay.models.Charging;

public class M1PaySDKUtils {
	final static  String SDK_DATA_CONFIG = "data_config";
	final static  String SDK_PREFERENCE = "1pay_sdk_prefs";
	public static String getPriceFromCode(Context context, String shortCode) {
		char c = shortCode.charAt(1);
		int index = 0;
		try {
			index = Integer.parseInt(c + "");
		} catch (NumberFormatException e) {

		} catch (Exception e) {

		}
		String[] prices = context.getResources().getStringArray(R.array.prices);
		if (index < prices.length) {
			String price = prices[index];
			return price;
		} else {
			return prices[0];
		}
	}

	public static boolean checkPriceFromCodeSms(Context context, String price){
		boolean priceCheck = false;
		final String[] priceCodeSms = context.getResources().getStringArray(R.array.prices);
		if (priceCodeSms.length >0 && priceCodeSms != null ) {
			for (int i = 0; i < priceCodeSms.length; i++) {
				if (priceCodeSms[i].equalsIgnoreCase(price)) {
					priceCheck = true;
				}
			}
		}
		return priceCheck;

	}

	public static boolean checkPriceFromCodeSmsPlus(Context context, String price){
		String base64String;

		Charging mCharging = null;

		final  String SDK_PREFERENCE_CHECK = "1pay_sdk_prefs";

		SharedPreferences mPrefs;

		boolean priceCheck = false;

		mPrefs = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE_CHECK,
				Context.MODE_PRIVATE);
		if (mPrefs.contains(SDK_DATA_CONFIG)) {
			base64String = mPrefs.getString(SDK_DATA_CONFIG, "");
			byte[] data = Base64.decode(base64String, Base64.DEFAULT);
			String jsonString;
			try {
				jsonString = new String(data, "UTF-8");
				mCharging = Charging.createFromJsonString(jsonString);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (JSONException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (Exception e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			}
		} 
		if (mCharging != null) {
			if (mCharging.smsPlusCharging.carriers.size() > 0){
				Carrier carrier = mCharging.smsPlusCharging.carriers.get(0);
				String[] stringPriceServer  =   carrier.prices;
				for (int i = 0; i < stringPriceServer.length; i++) {
					try {
						stringPriceServer[i] = ( Integer.parseInt(stringPriceServer[i]) * carrier.unit)+"";
					} catch (Exception e) {
						Log.e(" ERROR Convert int", M1PaySDKUtils.class.getName() + e);
					}
				}

				if (stringPriceServer != null || stringPriceServer.length > 0) {
					for (int i = 0; i < stringPriceServer.length; i++) {
						if (stringPriceServer[i].equalsIgnoreCase(price)) {
							priceCheck  = true ;
						}
					}
				}
			}
		}
		return priceCheck;
	}

	public static boolean checkTypeCard(Context context, String cardType){
		String base64String;

		Charging mCharging = null;

		final  String SDK_PREFERENCE_CHECK_TYPE_CARD = "1pay_sdk_prefs";

		SharedPreferences mPrefs;

		boolean cardTypeCheck = false;

		mPrefs = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE_CHECK_TYPE_CARD,
				Context.MODE_PRIVATE);
		if (mPrefs.contains(SDK_DATA_CONFIG)) {
			base64String = mPrefs.getString(SDK_DATA_CONFIG, "");
			byte[] data = Base64.decode(base64String, Base64.DEFAULT);
			String jsonString;
			try {
				jsonString = new String(data, "UTF-8");
				mCharging = Charging.createFromJsonString(jsonString);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (JSONException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (Exception e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			}
		} 
		if (mCharging != null) {
			if (mCharging != null) {
				if (mCharging.cardCharging.card_types != null){
					String[] CardTypeServer  =   mCharging.cardCharging.card_types.replace("[",  "").replace("]", "").replace("\"", "").split(",");
					if (CardTypeServer != null || CardTypeServer.length > 0) {
						for (int i = 0; i < CardTypeServer.length; i++) {
							if (CardTypeServer[i].equalsIgnoreCase(cardType)) {
								cardTypeCheck  = true ;
							}
						}
					}
				}
			}
		}
		return cardTypeCheck;
	}

	public static String getShortCodeFromCode(Context context, String Price) {
		String base64String;
		Charging mCharging = null;
		SharedPreferences mPrefs;
		int index = 0;
		String code = null;
		final String[] priceCode = context.getResources().getStringArray(R.array.prices);
		mPrefs = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE,
				Context.MODE_PRIVATE);
		if (mPrefs.contains(SDK_DATA_CONFIG)) {
			base64String = mPrefs.getString(SDK_DATA_CONFIG, "");
			byte[] data = Base64.decode(base64String, Base64.DEFAULT);
			String jsonString;
			try {
				jsonString = new String(data, "UTF-8");
				mCharging = Charging.createFromJsonString(jsonString);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (JSONException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (Exception e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			}
		} 
		if (mCharging != null) {
			final String shortCodeString = mCharging.smsCharging.shortCodes.replace("[", "").replace("]", "").replace("\"", "");
			final String[] shortCodes = shortCodeString.split(",");
			for (String strPrice : priceCode) {
				if (Price.equalsIgnoreCase(strPrice)) {	
					code = shortCodes[index];
				}
				index ++;
			}
		}
		return code;
	}

	public static String getDataFormAssetFile(Context context, String path) {
		String str = null;
		AssetManager am = context.getAssets();
		try {
			InputStream is = am.open(path);
			int length = is.available();
			byte[] data = new byte[length];
			is.read(data);
			str = new String(data);
		} catch (IOException e) {
			Log.e(M1PaySDKUtils.class.getName(), "" + e.getMessage());
		} catch (Exception e) {
			Log.e(M1PaySDKUtils.class.getName(), "" + e.getMessage());
		}
		return str;
	}
}

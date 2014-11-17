
package com.m1pay.M1PaySDK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.m1pay.configBackGround.SetBackGroundOnepay;
import com.m1pay.libsNetworks.HttpHelper;
import com.m1pay.libsNetworks.NetworkUtils;
import com.m1pay.m1payApiLibs.APIs;
import com.m1pay.m1payApiLibs.QueryBuilder;
import com.m1pay.m1payApiLibs.Utils;
import com.m1pay.models.Carrier;
import com.m1pay.models.Charging;
import com.m1pay.models.ItemTypeCard;
import com.m1pay.models.ItemTypeSms;
import com.m1pay.models.ItemTypeSmsPlus;
import com.m1pay.models.Language;
import com.m1pay.models.LanguageAdapter;
import com.m1pay.models.SmsAdapter;
import com.m1pay.models.SmsPlusAdapter;
import com.m1pay.models.TypeAdapter;
import com.m1pay.models.Charging.StatusCode;
import com.m1pay.views.MDialog;
import com.m1pay.views.MDialogAbout;
import com.m1pay.views.MDialogChangeLang;
import com.m1pay.views.MDialogNotify;
public class M1PaySDK {
	private final String SDK_PREFERENCE = "1pay_sdk_prefs";

	private final String SDK_PREFERENCE_CONFIG = "1pay_sdk_prefs_config";

	private final String SDK_DATA_CONFIG = "data_config";

	private final String SDK_LANG = "sdk_lang";

	private final String SDK_LANG_UPDATE = "sdk_lang_update";

	private final String SDK_LANG_POSITION = "sdk_position";

	private final String SDK_hexSolidColor = "hexSolidColor";

	private final String SDK_hexBoundColor = "hexBoundColor";

	private final String SDK_connerRadius = "connerRadius";

	private final String SDK_strokeWidth = "strokeWidth";

	private final String SDK_textColor = "textColor";

	private final String VIETNAMESE = "vi";

	private final String ENGLISH = "en_US";

	private final String SDK_ENABLE_LANG_CHANGE = "sdk_enable_lang_change";

	private final String CALLBACK_URL = "callback://";

	private final String ERROR_URL = "error://";

	private Context context;

	private TransactionCallBack mTransactionCallBack;

	private SharedPreferences mPrefs, mPrefsConfig;

	private Charging mCharging;

	private int sdk,exchangeRateBySms = 0, exchangeRateBySmsPlus = 0, exchangeRateByCard = 0;

	private String exchangeUnit = "", exchangeOtherInformationSms="developertuandv", exchangeOtherInformationSmsPlus="developertuandv";;

	private boolean enableButtonSms = true, enableButtonCard = true;

	private boolean  enableButtonSmsPlus = true, enablePaymentNote = false;

	private String titleCardCharging,colorButtonCardCharging, colorTextButtonCardCharging;

	private String titleSmsCharging, colorButtonSmsCharging, colorTextButtonSmsCharging;

	private String titleSmsPlusCharging, colorSmsPlusNumbers, colorTextButtonSmsPlusCharging;

	private Typeface textStyle, typefaceStyleTitleShowPayment;

	private int typefaceStyle;

	private String titlePayment, accountName, appName;

	private String backgroundColorBtnPayment, textColorBtnPayment;

	private String backgroundColorTitle, colorTextTitleShowPayment;

	private float cornerRadiusCard, cornerRadiusSms, cornerRadiusSmsPlus, cornerRadiusCancel, cornerRadiusPayment;

	private String hexSolidColorMainBackground;

	private String hexTextColorMainBackground;

	private String colorTvPaymentNote;

	private ArrayList<String> listPricesSms, listPricesSmsPlus, listTypeCard;

	private boolean enableCard = true, enableHelpCard = false;

	private boolean enableSms = true, enableHelpSms = false;

	private boolean enableSmsPlus = true, enableHelpSmsPlus = false;

	private View contentViewHelpCard, contentViewCard;

	private View contentViewHelpSmsPlus, contentViewSmsPlus;

	private View contentViewHelpSms, contentViewSms;

	private String type, selectItemPriceSms, selectItemPriceSmsPlus;

	private BroadcastReceiver messageSentSms, messageSentSmsPlus;

	private String errorNoPayment;

	/**
	 * Create M1PaySDK
	 * @param context
	 * @param access_key
	 * @param secret_key
	 * @param callBack
	 */
	public M1PaySDK(Activity context) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (context == null) {
			throw new RuntimeException("Android context invalid, please pass Activity");
		}

		this.context = context;
		mPrefs = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE,
				Context.MODE_PRIVATE);
		mPrefsConfig = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE_CONFIG,
				context.MODE_PRIVATE);

		if (mPrefsConfig.contains(SDK_hexSolidColor) && mPrefsConfig.contains(SDK_textColor)) {
			hexSolidColorMainBackground = mPrefsConfig.getString(SDK_hexSolidColor, "#ffffff");
			hexTextColorMainBackground = mPrefsConfig.getString(SDK_textColor, "#ffffff");
		}
		if (mPrefs.contains(SDK_LANG_UPDATE)) {
			String language = mPrefs.getString(SDK_LANG_UPDATE, VIETNAMESE);
			changeLanguage(language);
		}else{
			String language = mPrefs.getString(SDK_LANG, VIETNAMESE);
			changeLanguage(language);
		}
		//Init charging config
		initChargingConfig();

		//Auto load new config from server
		loadConfigFromServer();
	}

	//TODO
	public void initTheme(String hexSolidColorMain, String hexBoundColorMain, String hexTextColor, int connerRadiusMain, int strokeWidthMain) {
		Editor editor = mPrefsConfig.edit();
		editor.putString(SDK_hexSolidColor, hexSolidColorMain.trim());
		editor.putString(SDK_hexBoundColor, hexBoundColorMain.trim());
		editor.putString(SDK_textColor, hexTextColor.trim());
		editor.putInt(SDK_connerRadius, connerRadiusMain);
		editor.putInt(SDK_strokeWidth, strokeWidthMain);
		editor.commit();
		this.hexSolidColorMainBackground = hexSolidColorMain;
		this.hexTextColorMainBackground = hexTextColor;
	}


	public void removeInitThem(boolean bl) {
		if (bl && mPrefsConfig.contains(SDK_hexSolidColor)) {	
			mPrefsConfig.edit().clear().commit();
		}
		this.hexSolidColorMainBackground = null;
		this.hexTextColorMainBackground = null;
	}

	private void initChargingConfig() {
		String base64String = null;
		if (mPrefs.contains(SDK_DATA_CONFIG)) {
			base64String = mPrefs.getString(SDK_DATA_CONFIG, "");
		} else {
			try {
				base64String = Utils.readFileFromRaw(context, R.raw.charging_config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (base64String != null) {
			byte[] data = Base64.decode(base64String, Base64.DEFAULT);
			String jsonString;
			try {
				jsonString = new String(data, "UTF-8");
				mCharging = Charging.createFromJsonString(jsonString);
				if(mCharging != null){
					mPrefs.edit().putString(SDK_DATA_CONFIG, base64String).commit();
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (JSONException e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			} catch (Exception e) {
				throw new RuntimeException("" + e.getLocalizedMessage());
			}
		}
	}

	public void setEnableLangueChange(boolean enable) {
		mPrefs.edit().putBoolean(SDK_ENABLE_LANG_CHANGE, enable).commit();
	}

	/**
	 * Config display language for SDK
	 * @param lang - one of two values: vi for Vietnamese AND en_US for English
	 */
	public void setLanguage(String lang) {
		if (!mPrefs.contains(SDK_LANG_UPDATE)) {
			if (lang.equalsIgnoreCase(VIETNAMESE) || lang.equalsIgnoreCase(ENGLISH)) {
				mPrefs.edit().putString(SDK_LANG, lang).commit();
				changeLanguage(lang);
			}else{
				mPrefs.edit().putString(SDK_LANG, VIETNAMESE).commit();
				changeLanguage(VIETNAMESE);
			}
		}
	}

	/**
	 * @param  ArrayList<String> of shortcodes
	 */
	public void setListShortCodeSms(ArrayList<String> listPricesSms) {
		this.listPricesSms = listPricesSms;
	}

	/**
	 * @param  ArrayList<String> of shortcodes
	 */
	public void setListShortCodeSmsPlus(ArrayList<String> listPricesSmsPlush) {
		this.listPricesSmsPlus = listPricesSmsPlush;
	}

	/**
	 * @param ArrayList<String> type card 
	 */
	public void setListTypeCard(ArrayList<String> listTypeCard) {
		this.listTypeCard = listTypeCard;
	}

	/**
	 * @param String account name app  
	 */
	public void setAccountName(String name) {
		this.accountName = name;
	}

	/**
	 * @param String app name  
	 */
	public void setAppName(String name) {
		this.appName = name;
	}

	/**
	 * @param boolean true or false
	 * @return VISIBLE Payment note if true and GONE if false 
	 */
	public void setEnablePaymentNote(boolean enable) {
		this.enablePaymentNote = enable;
	}

	public void setexchangeRateBySms(int rate) {
		this.exchangeRateBySms = rate;
	}

	public void setexchangeRateBySmsPlus(int rate) {
		this.exchangeRateBySmsPlus = rate;
	}

	public void setExchangeRateByCard(int rate) {
		this.exchangeRateByCard = rate;
	}

	public void setExchangeUnit(String unit) {
		this.exchangeUnit = unit;
	}
	public void setExchangeOtherInformationSms(String information) {
		this.exchangeOtherInformationSms = information;
	}

	public void setExchangeOtherInformationSmsPlus(String information) {
		this.exchangeOtherInformationSmsPlus = information;
	}

	public void setEnableSmsCharging(boolean enable){
		this.enableButtonSms = enable;
	};

	public void setEnableSmsPlusCharging(boolean enable){
		this.enableButtonSmsPlus = enable;
	};

	public void setEnableCardCharging(boolean enable){
		this.enableButtonCard = enable;
	};

	public void setTitleCardCharging(String title) {
		this.titleCardCharging = title;
	}

	public void settitleSmsCharging(String title) {
		this.titleSmsCharging = title;
	}

	public void setTitleSmsPlusCharging(String title) {
		this.titleSmsPlusCharging = title;
	}

	public void setTitleButtonPayment(String title) {
		this.titlePayment = title;
	}

	public void setTextColorPaymentNote(String hexColorTextPaymentNote) {
		this.colorTvPaymentNote = hexColorTextPaymentNote;
	}

	public void setColorButtonCardCharging(String hexColorBackgroundButtonCARD, String hexColorTitleButtonCARD, float cornerRadiusCARD){
		this.colorButtonCardCharging = hexColorBackgroundButtonCARD;
		this.cornerRadiusCard = cornerRadiusCARD;
		this.colorTextButtonCardCharging = hexColorTitleButtonCARD;
	}

	public void setcolorButtonSmsCharging(String hexColorBackgroundButtonSMS, String hexColorTitleButtonSMS, float cornerRadiusSms) {
		this.colorButtonSmsCharging = hexColorBackgroundButtonSMS;
		this.cornerRadiusSms = cornerRadiusSms;
		this.colorTextButtonSmsCharging = hexColorTitleButtonSMS;
	}

	public void setColorButtonSmsPlusCharging(String hexColorBackgroundButtonSmsPlus, String hexColorTitleButtonSmsPlus, float cornerRadiusSmsPlus) {
		this.colorSmsPlusNumbers = hexColorBackgroundButtonSmsPlus;
		this.cornerRadiusSmsPlus = cornerRadiusSmsPlus;
		this.colorTextButtonSmsPlusCharging = hexColorTitleButtonSmsPlus;
	}

	public void setColorBackgroundTitle(String hexColorBackground, String hexColorTextTitle, Typeface typefaceintStyleTitleShowPayment) {
		this.backgroundColorTitle = hexColorBackground;
		this.colorTextTitleShowPayment = hexColorTextTitle;
		this.typefaceStyleTitleShowPayment = typefaceintStyleTitleShowPayment;
	}

	public void setBackgroundColorBtnPayment(String hexBackgroundColor, String hexTextColor, int cornerRadiusPayment) {
		this.backgroundColorBtnPayment = hexBackgroundColor;
		this.textColorBtnPayment = hexTextColor;
		this.cornerRadiusPayment = cornerRadiusPayment;
	}

	public void setTextStyle(Typeface fontStyle){
		this.textStyle = fontStyle;
	}

	private void loadConfigFromServer() { 
		if (NetworkUtils.isNetworkAvailable(context)) {
			new LoadConfigTask().execute();
		}
	}

	public void start(TransactionCallBack callBack) {
		this.mTransactionCallBack = callBack;
		showViewOptions();
	}

	public void registerSms() {
		String SENTSMS = "SMS_SENT";
		messageSentSms = new sentSms(); 
		context.getApplicationContext().registerReceiver(messageSentSms,new IntentFilter(SENTSMS));
	}
	public void registerSmsPlus() {
		String SENTSMSPLUS = "SMSPLUS_SENT";
		messageSentSmsPlus = new sentSmsPlus(); 
		context.getApplicationContext().registerReceiver(messageSentSmsPlus,new IntentFilter(SENTSMSPLUS));
	}

	@SuppressWarnings("deprecation")
	private void showViewOptions() {
		registerSms();
		registerSmsPlus();
		//		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		//		String number = tm.getLine1Number();
		//		Toast.makeText(context, number, Toast.LENGTH_LONG).show();
		sdk = android.os.Build.VERSION.SDK_INT;
		final MDialog dialogShowViewOption = new MDialog(this.context);
		dialogShowViewOption.setTitle(context.getResources().getString(R.string.title_show_view_option), textStyle);
		dialogShowViewOption.setEnableButtonsHelp(false);
		dialogShowViewOption.setEnableAccountName(true);
		dialogShowViewOption.setEnableButtonPay(false);
		if (accountName != null && !accountName.equalsIgnoreCase("")) {
			dialogShowViewOption.setAccountName(accountName, textStyle);
		}
		if (appName != null && !appName.equalsIgnoreCase("")) {
			dialogShowViewOption.setNameGame(appName, textStyle);
		}
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
			dialogShowViewOption.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialogShowViewOption.setCancelable(true);

		final View layout = LayoutInflater.from(context).inflate(R.layout.onpay_main_layout, null);
		if (!mCharging.status || !mCharging.smsCharging.status && !mCharging.smsPlusCharging.status && !mCharging.cardCharging.status || !enableButtonCard && !enableButtonSms && !enableButtonSmsPlus) {
			dialogShowViewOption.setContent(context.getResources().getString(R.string.errornopayment));
		}else{
			dialogShowViewOption.setContentLayout(layout);
		}

		ImageView imgOnePayLogo = (ImageView)layout.findViewById(R.id.img_one_pay);
		imgOnePayLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final MDialogAbout dialogAboutOnePay = new MDialogAbout(context);
				dialogAboutOnePay.setTitle(R.string.title_about);
				dialogAboutOnePay.setCancelable(true);
				if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
					dialogAboutOnePay.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
				}
				View viewLayoutAbout = null;
				viewLayoutAbout = LayoutInflater.from(context).inflate(R.layout.layout_about_onepay, null);
				dialogAboutOnePay.setContentLayout(viewLayoutAbout);
				dialogAboutOnePay.show(); 
				dialogAboutOnePay.setButtonClose(new OnClickListener() {
					@Override
					public void onClick(View v) {	
						dialogAboutOnePay.dismiss();
					}
				});  
			}
		});
		dialogShowViewOption.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationLeftRight;
		//Card Charging Button
		Button btnCardCharging = (Button) layout.findViewById(R.id.btnCardCharging);
		if (colorButtonCardCharging != null && colorTextButtonCardCharging != null && cornerRadiusCard > 0) {

			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				btnCardCharging.setBackgroundDrawable(SetBackGroundOnepay.backgroundButton(colorButtonCardCharging, cornerRadiusCard));
			} else {
				btnCardCharging.setBackground(SetBackGroundOnepay.backgroundButton(colorButtonCardCharging, cornerRadiusCard));
			}
			btnCardCharging.setTextColor(Color.parseColor(colorTextButtonCardCharging));
		}
		if (titleCardCharging != null) {
			btnCardCharging.setText(titleCardCharging);
		}

		if (textStyle != null) {
			btnCardCharging.setTypeface(textStyle);
		}
		btnCardCharging.setOnClickListener(mOnChargingChooseListener);

		//SMS Charging Button
		Button btnSMSCharging = (Button) layout.findViewById(R.id.btnSMSCharging);
		if (colorButtonSmsCharging != null && colorTextButtonSmsCharging != null && cornerRadiusSms > 0) {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				btnSMSCharging.setBackgroundDrawable(SetBackGroundOnepay.backgroundButton(colorButtonSmsCharging, cornerRadiusSms));
			}else{
				btnSMSCharging.setBackground(SetBackGroundOnepay.backgroundButton(colorButtonSmsCharging, cornerRadiusSms));	
			}
			btnSMSCharging.setTextColor(Color.parseColor(colorTextButtonSmsCharging));
		}
		if (titleSmsCharging != null) {
			btnSMSCharging.setText(titleSmsCharging);
		}

		if (textStyle != null) {
			btnSMSCharging.setTypeface(textStyle);
		}
		btnSMSCharging.setOnClickListener(mOnChargingChooseListener);

		//Sms Plus Charging Button
		Button btnSmsPlusCharging = (Button) layout.findViewById(R.id.btnSmsPlusCharging);
		if (colorSmsPlusNumbers != null && colorTextButtonSmsPlusCharging != null && cornerRadiusSmsPlus > 0) {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				btnSmsPlusCharging.setBackgroundDrawable(SetBackGroundOnepay.backgroundButton(colorSmsPlusNumbers, cornerRadiusSmsPlus));
			}else{
				btnSmsPlusCharging.setBackground(SetBackGroundOnepay.backgroundButton(colorSmsPlusNumbers, cornerRadiusSmsPlus));	
			}
			btnSmsPlusCharging.setTextColor(Color.parseColor(colorTextButtonSmsPlusCharging));
		}
		if (titleSmsPlusCharging != null) {
			btnSmsPlusCharging.setText(titleSmsPlusCharging);
		}

		if (textStyle != null) {
			btnSmsPlusCharging.setTypeface(textStyle);
		}
		btnSmsPlusCharging.setOnClickListener(mOnChargingChooseListener);

		if (mCharging.cardCharging == null || !mCharging.cardCharging.status || !enableButtonCard || !mCharging.status) {
			btnCardCharging.setVisibility(View.GONE);
		}

		if (mCharging.smsCharging == null || !mCharging.smsCharging.status  || !enableButtonSms || !mCharging.status) {
			btnSMSCharging.setVisibility(View.GONE);
		}

		if (mCharging.smsPlusCharging == null || !mCharging.smsPlusCharging.status  || !enableButtonSmsPlus || !mCharging.status) {
			btnSmsPlusCharging.setVisibility(View.GONE);
		}
		dialogShowViewOption.setBackButton(null);
		final Spinner splang = (Spinner)layout.findViewById(R.id.spLang);
		if (mPrefs.contains(SDK_ENABLE_LANG_CHANGE)) {
			boolean enableLang = mPrefs.getBoolean(SDK_ENABLE_LANG_CHANGE, true);
			if (!enableLang) {
				splang.setVisibility(View.GONE);
			}else{
				splang.setVisibility(View.VISIBLE);
			}
		}
		LanguageAdapter adapter = new LanguageAdapter((Activity) context, android.R.layout.simple_spinner_item,LanguageArray());        
		splang.setAdapter(adapter);
		String lang;
		if (mPrefs.contains(SDK_LANG_UPDATE)) {
			lang = mPrefs.getString(SDK_LANG_UPDATE, VIETNAMESE);
		}else{
			lang = mPrefs.getString(SDK_LANG, VIETNAMESE);
		}
		final int indexLang;
		if (lang.equalsIgnoreCase(ENGLISH)) {
			splang.setSelection(1);
			indexLang = 1;
		} else {
			splang.setSelection(0);
			indexLang = 0;
		}
		splang.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				TextView tvlang = ((TextView) view.findViewById(R.id.name));
				ImageView tvimage = ((ImageView) view.findViewById(R.id.flag));
				if (hexSolidColorMainBackground != null && hexTextColorMainBackground!= null) {
					tvlang.setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
					tvimage.setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
					tvlang.setTextColor(Color.parseColor(hexTextColorMainBackground));	
				}
				String valuename  = ((TextView) view.findViewById(R.id.valuename)).getText().toString();
				if (valuename.equals(ENGLISH)) {
					if (indexLang != position) {

						final MDialogChangeLang dialogLang = new MDialogChangeLang(context);
						dialogLang.setContent(context.getResources().getString(R.string.contentlang), textStyle);
						dialogLang.setTitle(context.getResources().getString(R.string.titlelang), textStyle);
						if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
							dialogLang.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
						}
						dialogLang.setRightButtonClick(context.getResources().getString(R.string.oke), new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								mPrefs.edit().putString(SDK_LANG_UPDATE, ENGLISH).commit();
								changeLanguage(ENGLISH);	
								dialogLang.dismiss();
								dialogShowViewOption.dismiss();
							}
						});
						dialogLang.setLeftButtonClick(context.getResources().getString(R.string.cancel), new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialogLang.dismiss();
								splang.setSelection(indexLang);
							}
						});
						dialogLang.setButtonTypeface(textStyle);
						dialogLang.show();
					}
				}else{

					if (valuename.equals(VIETNAMESE)) {
						if (indexLang != position) {
							final MDialogChangeLang dialogLangs = new MDialogChangeLang(context);
							dialogLangs.setContent(context.getResources().getString(R.string.contentlang), textStyle);
							dialogLangs.setTitle(context.getResources().getString(R.string.titlelang), textStyle);
							if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
								dialogLangs.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
							}
							dialogLangs.setRightButtonClick(context.getResources().getString(R.string.oke), new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									mPrefs.edit().putString(SDK_LANG_UPDATE, VIETNAMESE).commit();
									changeLanguage(VIETNAMESE);
									dialogLangs.dismiss();
									dialogShowViewOption.dismiss();

								}
							});
							dialogLangs.setLeftButtonClick(context.getResources().getString(R.string.cancel), new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialogLangs.dismiss();
									splang.setSelection(indexLang);
								}
							});
							dialogLangs.setButtonTypeface(textStyle);
							dialogLangs.show();
						}
					}
				}

				//showViewOptions();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});

		dialogShowViewOption.setAboutButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final MDialogAbout dialogAbout = new MDialogAbout(context);
				dialogAbout.setTitle(R.string.title_about);
				dialogAbout.setCancelable(true);
				if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
					dialogAbout.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
				}
				View viewLayoutAbout = null;
				viewLayoutAbout = LayoutInflater.from(context).inflate(R.layout.layout_about, null);
				dialogAbout.setContentLayout(viewLayoutAbout);
				dialogAbout.show(); 
				dialogAbout.setButtonClose(new OnClickListener() {
					@Override
					public void onClick(View v) {	
						dialogAbout.dismiss();
					}
				});  
			}
		});
		dialogShowViewOption.show();
	}

	private ArrayList<Language> LanguageArray(){
		final ArrayList<Language> lang = new ArrayList<Language>();
		lang.add(new Language(context.getResources().getString(R.string.vietnamese), R.drawable.flag_vi, "vi", textStyle));
		lang.add(new Language(context.getResources().getString(R.string.english), R.drawable.flag_en, "en_US", textStyle));		    
		return lang;

	}
	private void changeLanguage(String languageToLoad) {
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	private final OnClickListener mOnChargingChooseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnCardCharging) {
				showCardCharging();
			} else if (v.getId() == R.id.btnSMSCharging) {
				showSMSCharging();
			} else if(v.getId() == R.id.btnSmsPlusCharging){
				showSmsPlusCharging();
			}
		}
	};

	// ===================== CARD CHARGING =========================

	/**
	 * Show card charging default layout
	 */
	@SuppressWarnings("deprecation")
	private void showCardCharging() {
		final MDialog dialogShowCardCharging = new MDialog(context);
		dialogShowCardCharging.setEnableIcon(false);
		dialogShowCardCharging.setEnableAccountName(false);
		if (titleCardCharging != null) {
			dialogShowCardCharging.setTitle(titleCardCharging.toUpperCase(), textStyle);
		}else{
			String title = context.getResources().getString(R.string.card_charging);
			dialogShowCardCharging.setTitle(title.toUpperCase(), textStyle);
		}
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null ) {
			dialogShowCardCharging.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialogShowCardCharging.setCancelable(true);

		// set content lay out frame
		if (contentViewCard != null) {
			ViewGroup vg = (ViewGroup) contentViewCard.getParent();
			vg.removeView(contentViewCard);	 
		}else{
			contentViewCard = LayoutInflater.from(context).inflate(R.layout.layout_card_charging, null);
		}
		if (contentViewHelpCard != null) {
			ViewGroup vg = (ViewGroup) contentViewHelpCard.getParent();
			vg.removeView(contentViewHelpCard);
		}else{
			contentViewHelpCard = LayoutInflater.from(context).inflate(R.layout.layout_help_card, null);
		}
		dialogShowCardCharging.setContentLayout(contentViewCard);
		dialogShowCardCharging.setContentLayoutHelp(contentViewHelpCard);
		final EditText edtCardNumber = (EditText) contentViewCard.findViewById(R.id.edtCardNumber);
		final EditText edtCardSerial = (EditText) contentViewCard.findViewById(R.id.edtCardSerial);
		if(hexSolidColorMainBackground != null && hexTextColorMainBackground != null ){
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				edtCardNumber.setBackgroundDrawable(SetBackGroundOnepay.backgroundEditText(hexSolidColorMainBackground, 2, hexTextColorMainBackground, 8));
				edtCardSerial.setBackgroundDrawable(SetBackGroundOnepay.backgroundEditText(hexSolidColorMainBackground, 2, hexTextColorMainBackground, 8));
			} else {
				edtCardNumber.setBackground(SetBackGroundOnepay.backgroundEditText(hexSolidColorMainBackground, 2, hexTextColorMainBackground, 8));
				edtCardSerial.setBackground(SetBackGroundOnepay.backgroundEditText(hexSolidColorMainBackground, 2, hexTextColorMainBackground, 8));
			}

			edtCardNumber.setTextColor(Color.parseColor(hexTextColorMainBackground));

			edtCardSerial.setTextColor(Color.parseColor(hexTextColorMainBackground));
		}
		edtCardNumber.setHint(R.string.enter_card_number);
		edtCardSerial.setHint(R.string.enter_card_serial);
		final Spinner sp = (Spinner) contentViewCard.findViewById(R.id.spType);
		if (textStyle != null) {
			edtCardNumber.setTypeface(textStyle);
			edtCardSerial.setTypeface(textStyle);
		}


		//Exchange Rate
		TextView tvPaymentNote = (TextView) contentViewCard.findViewById(R.id.tvPaymentNote);
		if (this.exchangeRateByCard > 0 && enablePaymentNote == true) {
			tvPaymentNote.setVisibility(View.VISIBLE);
			String str = context.getString(R.string.currency_exchange_rate, "1000",
					exchangeRateByCard, exchangeUnit);
			tvPaymentNote.setText(str);
			if (textStyle != null) {
				tvPaymentNote.setTypeface(textStyle);		
			}
			tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
		}else if (!mCharging.description.equals("null") && enablePaymentNote == true) {
			tvPaymentNote.setVisibility(View.VISIBLE);
			tvPaymentNote.setText(mCharging.description);
			if (textStyle != null) {
				tvPaymentNote.setTypeface(textStyle);		
			}
			tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
		}

		/*
		 * get type card from json 
		 * 
		 */

		ArrayList<ItemTypeCard> typeCard = new ArrayList<ItemTypeCard>();
		typeCard.add(new ItemTypeCard(context.getResources().getString(R.string.choose_card_type).toString(), textStyle));
		if (listTypeCard != null && listTypeCard.size() > 0) {
			for (String typeStr : listTypeCard) {
				if (OnepayUtils.checkTypeCard(context, typeStr.toLowerCase()) && !typeStr.equalsIgnoreCase("")) {
					typeCard.add(new ItemTypeCard(upperCaseFistChar(typeStr.toLowerCase()), textStyle));
				}
			}
		}else{
			String[] cardType;
			String card_type = mCharging.cardCharging.card_types.replace("[",  "").replace("]", "").replace("\"", "");
			cardType = card_type.split(",");
			for (String strTypeCard : cardType){
				strTypeCard.trim();
				if (!strTypeCard.equals("")) {
					typeCard.add(new ItemTypeCard(upperCaseFistChar(strTypeCard.toLowerCase()), textStyle));
				}
			}
		}
		TypeAdapter adapter = new TypeAdapter((Activity) context, android.R.layout.simple_spinner_item,typeCard);        
		sp.setAdapter(adapter);
		//		sp.setAdapter(new ArrayAdapter<String>(context, R.layout.item_type, typeCard));
		/*
		 * get type card in xml.		
		 * sp.setAdapter(new ArrayAdapter<String>(context, R.layout.item_type, context.getResources()
				.getStringArray(R.array.card_type))); 
		 * 
		 */	

		sp.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				type  = ((TextView) view.findViewById(R.id.nametype)).getText().toString();
				if(hexSolidColorMainBackground != null && hexTextColorMainBackground != null ){
					//					((TextView)parent.getChildAt(0)).setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
					//					((TextView)parent.getChildAt(0)).setTextColor(Color.parseColor(hexTextColorMainBackground));
					TextView tvTypeCard = ((TextView) view.findViewById(R.id.nametype));
					tvTypeCard.setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
					tvTypeCard.setTextColor(Color.parseColor(hexTextColorMainBackground));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		// set color button payment card 
		if (titlePayment!= null) {
			dialogShowCardCharging.setTitle(titlePayment);
		}
		if (backgroundColorBtnPayment != null && textColorBtnPayment != null && cornerRadiusPayment >= 0) {
			dialogShowCardCharging.setBackGroundButttonPayment(backgroundColorBtnPayment, textColorBtnPayment, cornerRadiusPayment);
		}
		dialogShowCardCharging.setButtonTypeface(textStyle);
		dialogShowCardCharging.setPaymentButtonClick(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edtCardSerial.setSelectAllOnFocus(true);
				edtCardSerial.setTextIsSelectable(true);
				edtCardNumber.setTextIsSelectable(true);
				edtCardNumber.setSelectAllOnFocus(true);
				final int pos = sp.getSelectedItemPosition();
				if (pos > 0) {
					String pin = edtCardNumber.getText().toString();
					String serial = edtCardSerial.getText().toString();
					if (pin != null && !pin.equals("") && serial != null && !serial.equals("")) {
						if (NetworkUtils.isNetworkAvailableSocket()) {
							startCardCharging(pin, serial, type.toLowerCase());
							dialogShowCardCharging.dismiss();
						} else{// err network cardcharging  
							final MDialogNotify dialogErrNetWork = new MDialogNotify(context);
							dialogErrNetWork.setTitle(R.string.title_network_warning, textStyle);
							dialogErrNetWork.setCancelable(true);
							if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
								dialogErrNetWork.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
							}
							dialogErrNetWork.setContent(R.string.network_warning, textStyle);
							dialogErrNetWork.show();
							dialogErrNetWork.setButtonClose(new OnClickListener() {

								@Override
								public void onClick(View v) {	
									NetworkUtils.setMobileDataEnabled(context, true);
									dialogErrNetWork.dismiss();
								}
							});      

						}
					}else {// empty card
						final MDialogNotify dialogErrCard = new MDialogNotify(context);
						if (titleCardCharging != null) {
							dialogErrCard.setTitle(titleCardCharging, textStyle);
						}else{
							dialogErrCard.setTitle(R.string.card_charging, textStyle);
						}
						if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
							dialogErrCard.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
						}
						dialogErrCard.setContent(R.string.err_enter_card, textStyle);
						dialogErrCard.setCancelable(true);
						dialogErrCard.show();
						dialogErrCard.setButtonClose(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String pin = edtCardNumber.getText().toString().trim();
								String serial = edtCardSerial.getText().toString().trim();
								if (pin == null || pin.equalsIgnoreCase("")) {
									edtCardNumber.setFocusable(true);
									edtCardNumber.setFocusableInTouchMode(true);
									edtCardNumber.performClick();
								}else{
									edtCardNumber.setFocusable(false);
									edtCardNumber.setFocusableInTouchMode(false);
								}
								if (serial == null || serial.equalsIgnoreCase("")){
									edtCardSerial.setFocusable(true);
									edtCardSerial.setFocusableInTouchMode(true);
									edtCardSerial.performClick();
								}else {
									edtCardSerial.setFocusable(false);
									edtCardSerial.setFocusableInTouchMode(false);
								}
								dialogErrCard.dismiss();
							}
						}); 
					}
				}else {
					final MDialogNotify dialogErrCardFull = new MDialogNotify(context);
					if (titleCardCharging != null) {
						dialogErrCardFull.setTitle(titleCardCharging, textStyle);
					}else{
						dialogErrCardFull.setTitle(R.string.card_charging, textStyle);
					}
					if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
						dialogErrCardFull.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
					}
					dialogErrCardFull.setContent(R.string.err_enter_card, textStyle);
					dialogErrCardFull.setCancelable(true);
					dialogErrCardFull.show();
					dialogErrCardFull.setButtonClose(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (pos <= 0 ) {
								sp.setFocusable(true);
								sp.setFocusableInTouchMode(true);
								sp.performClick();
							}
							dialogErrCardFull.dismiss();
						}
					}); 
				}
			}
		});
		dialogShowCardCharging.setEnableButtonsAbout(false);
		dialogShowCardCharging.setBackButton(null);
		// set help button card click
		dialogShowCardCharging.setHelpButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (enableCard) {				
					dialogShowCardCharging.setEnableContentLayoutHelp(true);
				}else{
					dialogShowCardCharging.setEnableContentLayoutHelp(false);
				}
				enableCard = !enableCard;
			}
		});
		dialogShowCardCharging.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		dialogShowCardCharging.show();
	}

	private String upperCaseFistChar(String source) {
		StringBuffer res = new StringBuffer();
		String strUpcase = null ;
		String[] strArr = source.split(" ");
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);

			res.append(str).append(" ");
			strUpcase = res.toString().trim();
		}
		return strUpcase;
	}

	/**
	 * Start card charging
	 * @param pin
	 * @param serial
	 * @param type
	 */
	private void startCardCharging(String pin, String serial, String type) {
		QueryBuilder qBuilder = new QueryBuilder(mCharging.secret_key);
		qBuilder.put("access_key", mCharging.access_key);
		qBuilder.put("type", type);
		qBuilder.put("pin", pin);
		qBuilder.put("serial", serial);
		String queryStr = qBuilder.getQueryString();
		new CardChargingTask().execute(queryStr);
	}

	private class CardChargingTask extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (context != null) {
				pd = new ProgressDialog(context);
				if (pd != null) {
					pd.setMessage(context.getString(R.string.charging));
					pd.show();
				}
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String queryStr = params[0];
			// post
			//String responseStr = HttpHelper.post(APIs.CARD_CHARGING,queryStr); 
			//get 
			String responseStr = HttpHelper.getData(APIs.CARD_CHARGING+queryStr);
			return responseStr;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			boolean success = false;
			String msg = "";
			if (result != null) {
				try {
					JSONObject jObject = new JSONObject(result);
					String statusCode = jObject.getString("status");
					msg = jObject.getString("description");
					if (StatusCode.SUCCESS.equals(statusCode)) {
						success = true;
					} else{
						success = false;
					}
				} catch (JSONException e) {
					Log.w(M1PaySDK.class.getName(), "Transaction error: " + e.getMessage());
				}
			}else{
				msg = context.getResources().getString(R.string.load_data_err);
			}
			String title;
			if (titleSmsPlusCharging!= null) {
				title = titleCardCharging;
			}else{
				title = context.getResources().getString(R.string.card_charging);
			}
			showDialogErr(title, msg);
			if (mTransactionCallBack != null) {
				mTransactionCallBack.callBack(success, msg);
			}
		}
	}

	// ============================ Show Dialog err ==============================//

	private void  showDialogErr(String title, String content) {
		final MDialogNotify dialogErr = new MDialogNotify(context);
		dialogErr.setTitle(title, textStyle);
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
			dialogErr.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialogErr.setContent(content, textStyle);
		dialogErr.setCancelable(true);
		dialogErr.show();
		dialogErr.setButtonClose(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogErr.dismiss();
			}
		}); 
	}
	// ======================= SMSPlus CHARGING =========================
	/**
	 * Show SmsPlus Charging layout
	 */
	@SuppressWarnings("deprecation")
	private void showSmsPlusCharging() {
		//Show dialog
		final MDialog dialogShowSmsPlusCharging = new MDialog(context);
		dialogShowSmsPlusCharging.setEnableIcon(false);
		dialogShowSmsPlusCharging.setEnableAccountName(false);
		if (titleSmsPlusCharging != null) {
			dialogShowSmsPlusCharging.setTitle(titleSmsPlusCharging.toUpperCase(),textStyle);
		}else{
			String title = context.getResources().getString(R.string.smsplus_charging);
			dialogShowSmsPlusCharging.setTitle(title.toUpperCase(), textStyle);
		}
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
			dialogShowSmsPlusCharging.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialogShowSmsPlusCharging.setCancelable(true);

		// set content lay out frame
		if (contentViewSmsPlus != null) {
			ViewGroup vgSmsPlus = (ViewGroup) contentViewSmsPlus.getParent();
			vgSmsPlus.removeView(contentViewSmsPlus);	 
		}else{
			contentViewSmsPlus = LayoutInflater.from(context).inflate(R.layout.smsplus_charging_layout, null);
		}
		if (contentViewHelpSmsPlus != null) {
			ViewGroup vg = (ViewGroup) contentViewHelpSmsPlus.getParent();
			vg.removeView(contentViewHelpSmsPlus);
		}else{
			contentViewHelpSmsPlus = LayoutInflater.from(context).inflate(R.layout.layout_help_smsplus, null);
		}
		dialogShowSmsPlusCharging.setContentLayout(contentViewSmsPlus);
		dialogShowSmsPlusCharging.setContentLayoutHelp(contentViewHelpSmsPlus);

		final TextView tvPaymentNote = (TextView) contentViewSmsPlus.findViewById(R.id.tvPaymentNote);
		Carrier simsuport;
		String contentSimSuport = " ";
		final Spinner sp = (Spinner) contentViewSmsPlus.findViewById(R.id.spSmsPlusNumbers);
		for (int i = 0; i < mCharging.smsPlusCharging.carriers.size(); i++) {
			simsuport = mCharging.smsPlusCharging.carriers.get(i);
			contentSimSuport = contentSimSuport + upperCaseFistChar(simsuport.name) + " ";
		}		
		final Carrier carrier = mCharging.smsPlusCharging.getLocalCarrier(Utils.getCarrierCode(context));
		final ArrayList<ItemTypeSmsPlus> prices = new ArrayList<ItemTypeSmsPlus>();
		prices.add(new ItemTypeSmsPlus(context.getString(R.string.choose_price),textStyle));
		if (carrier != null) {
			if (listPricesSmsPlus != null && listPricesSmsPlus.size() > 0) {
				for (String str : listPricesSmsPlus) {
					str = str.trim();
					if (!str.equals("") && OnepayUtils.checkPriceFromCodeSmsPlus(context, str)) {	
						prices.add(new ItemTypeSmsPlus(str+ " VND", textStyle));
					}
				}
			}else{
				for (String str : carrier.prices) {
					str = str.trim();
					if (!str.equals("")) {
						prices.add(new ItemTypeSmsPlus(Integer.parseInt(str)*(carrier.unit) + " VND", textStyle));
					}
				}
			}
			SmsPlusAdapter adapter = new SmsPlusAdapter((Activity) context, android.R.layout.simple_spinner_item,prices);        
			sp.setAdapter(adapter);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					selectItemPriceSmsPlus = ((TextView) view.findViewById(R.id.nametype)).getText().toString();
					if(hexSolidColorMainBackground != null && hexTextColorMainBackground != null ){
						TextView tvSmsPlus = ((TextView) view.findViewById(R.id.nametype));
						tvSmsPlus.setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
						tvSmsPlus.setTextColor(Color.parseColor(hexTextColorMainBackground));
					}
					String str;
					if (exchangeRateBySmsPlus > 0 && enablePaymentNote == true) {
						tvPaymentNote.setVisibility(View.VISIBLE);
						if (pos == 0) {
							str = context.getString(R.string.currency_exchange_rate, "1000",
									exchangeRateBySmsPlus, exchangeUnit);
						} else {
							int price = Integer.parseInt(selectItemPriceSmsPlus.replace(" VND", ""));
							str = context.getString(R.string.currency_exchange_rate, price,
									exchangeRateBySmsPlus * price / 1000, exchangeUnit);
						}
						tvPaymentNote.setText(str);
						tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
						if (textStyle != null) {
							tvPaymentNote.setTypeface(textStyle);
						}
					}
					else if (!mCharging.description.equals("null") && enablePaymentNote == true) {
						tvPaymentNote.setVisibility(View.VISIBLE);
						tvPaymentNote.setText(mCharging.description);
						if (textStyle != null) {
							tvPaymentNote.setTypeface(textStyle);
						}
						tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
			dialogShowSmsPlusCharging.setEnableButtonsAbout(false);
			dialogShowSmsPlusCharging.setBackButton(null);
			// set help button smsplus  click
			dialogShowSmsPlusCharging.setHelpButton(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (enableSmsPlus) {
						dialogShowSmsPlusCharging.setEnableContentLayoutHelp(true);

					}else{
						dialogShowSmsPlusCharging.setEnableContentLayoutHelp(false);
					}
					enableSmsPlus = !enableSmsPlus;
				}
			});
			dialogShowSmsPlusCharging.show();
		} else { //Show alert dialog simcard not suport
			final MDialogNotify dialogNotSuport = new MDialogNotify(context);
			dialogNotSuport.setCancelable(true);
			String simcard = context.getResources().getString(R.string.sim_card_no_support);
			dialogNotSuport.setContent(simcard + contentSimSuport, textStyle);
			if (titleSmsPlusCharging != null) {
				dialogNotSuport.setTitle(titleSmsPlusCharging, textStyle);
			}else{
				dialogNotSuport.setTitle(R.string.smsplus_charging, textStyle);
			}
			if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
				dialogNotSuport.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
			}
			dialogNotSuport.show();
			dialogNotSuport.setButtonClose(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogNotSuport.dismiss();
				}
			});
		}
		// button send 
		if (titlePayment!= null) {
			dialogShowSmsPlusCharging.setTitle(titlePayment);
		}
		if (backgroundColorBtnPayment != null && textColorBtnPayment != null && cornerRadiusPayment >= 0) {
			dialogShowSmsPlusCharging.setBackGroundButttonPayment(backgroundColorBtnPayment, textColorBtnPayment, cornerRadiusPayment);
		}
		dialogShowSmsPlusCharging.setButtonTypeface(textStyle);
		dialogShowSmsPlusCharging.setPaymentButtonClick(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = sp.getSelectedItemPosition();
				if (pos > 0) {
					String priceSmsPlus = selectItemPriceSmsPlus.replace(" VND", "");			
					int pric = (Integer.parseInt(priceSmsPlus))/carrier.unit;
					String sms = carrier.sms;
					String contentSMS = sms.replace("$PRICE", ""+pric).replace("$THONGTINKHAC",exchangeOtherInformationSmsPlus);
					//+" "+UUID.randomUUID().toString()
					String shortCode = mCharging.smsPlusCharging.service_number;
					startSmsPlusCharging(context,contentSMS,shortCode);// start sms charging
					//	Log.e(" smsplus ", contentSMS  + "   dau so" + shortCode);
					dialogShowSmsPlusCharging.dismiss();
				}else {
					final MDialogNotify dialog = new MDialogNotify(context);
					if (titleSmsPlusCharging != null) {
						dialog.setTitle(titleSmsPlusCharging, textStyle);
					}else{
						dialog.setTitle(R.string.smsplus_charging, textStyle);
					}
					dialog.setContent(R.string.money_sms, textStyle);
					if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
						dialog.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
					}
					dialog.setCancelable(true);
					dialog.show();
					dialog.setButtonClose(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							sp.setFocusable(true);
							sp.setFocusableInTouchMode(true);
							sp.requestFocus();
							sp.performClick();
						}
					}); 
				}

			}
		});

	}


	// ======================= SMS CHARGING =========================
	/**
	 * Show SMS Charging layout
	 */
	@SuppressWarnings("deprecation")
	private void showSMSCharging() {
		final String[] shortCodes;
		final String[] shortCodesCustom;
		final ArrayList<ItemTypeSms> prices = new ArrayList<ItemTypeSms>();
		String priceString = null;	
		priceString = mCharging.smsCharging.shortCodes.replace("[", "");
		priceString = priceString.replace("]", "");
		priceString = priceString.replace("\"", "");					
		prices.add(new ItemTypeSms(context.getString(R.string.choose_price),textStyle));
		shortCodes = priceString.split(",");
		if (listPricesSms != null && listPricesSms.size() > 0) {
			for (String str : listPricesSms) {
				str = str.trim();
				if(!str.equalsIgnoreCase("") && OnepayUtils.checkPriceFromCodeSms(context, str)){
					prices.add( new ItemTypeSms(str + " VND", textStyle));
				}
			}
		}else{
			for (String str : shortCodes) {
				str = str.trim();
				if (!str.equals("")) {
					prices.add(new ItemTypeSms(OnepayUtils.getPriceFromCode(context, str) + " VND", textStyle));
				}
			}
		}
		//Show dialog
		final MDialog dialogShowSMSCharging = new MDialog(context);
		dialogShowSMSCharging.setEnableIcon(false);
		dialogShowSMSCharging.setEnableAccountName(false);
		if (titleSmsCharging != null) {
			dialogShowSMSCharging.setTitle(titleSmsCharging.toUpperCase(), textStyle);
		}else{
			String title = context.getResources().getString(R.string.sms_charging);
			dialogShowSMSCharging.setTitle(title.toUpperCase(), textStyle);
		}
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
			dialogShowSMSCharging.setBackGroundTitle(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialogShowSMSCharging.setCancelable(true);


		// set content lay out frame
		if (contentViewSms != null) {
			ViewGroup vgSmsPlus = (ViewGroup) contentViewSms.getParent();
			vgSmsPlus.removeView(contentViewSms);	 
		}else{
			contentViewSms = LayoutInflater.from(context).inflate(R.layout.sms_charging_layout, null);
		}
		if (contentViewHelpSms != null) {
			ViewGroup vg = (ViewGroup) contentViewHelpSms.getParent();
			vg.removeView(contentViewHelpSms);
		}else{
			contentViewHelpSms = LayoutInflater.from(context).inflate(R.layout.layout_help_sms, null);
		}
		dialogShowSMSCharging.setContentLayout(contentViewSms);
		dialogShowSMSCharging.setContentLayoutHelp(contentViewHelpSms);
		final TextView tvPaymentNote = (TextView) contentViewSms.findViewById(R.id.tvPaymentNote);
		final Spinner sp = (Spinner) contentViewSms.findViewById(R.id.spSMSNumbers);

		SmsAdapter adapter = new SmsAdapter((Activity) context, android.R.layout.simple_spinner_item,prices);        
		sp.setAdapter(adapter);
		//		sp.setAdapter(new ArrayAdapter<String>(context, R.layout.item_type, prices));
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				String str;
				selectItemPriceSms = ((TextView) view.findViewById(R.id.nametype)).getText().toString();
				if(hexSolidColorMainBackground != null && hexTextColorMainBackground != null ){
					TextView tvSms = ((TextView) view.findViewById(R.id.nametype));
					tvSms.setBackgroundColor(Color.parseColor(hexSolidColorMainBackground));
					tvSms.setTextColor(Color.parseColor(hexTextColorMainBackground));
				}
				if (exchangeRateBySms > 0  && enablePaymentNote == true) {
					tvPaymentNote.setVisibility(View.VISIBLE);
					if (pos == 0) {
						str = context.getString(R.string.currency_exchange_rate, "1000",
								exchangeRateBySms, exchangeUnit);
					} else {
						int price = Integer.parseInt(selectItemPriceSms.replace(" VND", ""));
						str = context.getString(R.string.currency_exchange_rate, price,
								exchangeRateBySms * price / 1000, exchangeUnit);
					}
					tvPaymentNote.setText(str);
					tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
					if (textStyle != null) {
						tvPaymentNote.setTypeface(textStyle);
					}
				}else if (!mCharging.description.equals("null") && enablePaymentNote == true) {
					tvPaymentNote.setVisibility(View.VISIBLE);
					tvPaymentNote.setText(mCharging.description);
					tvPaymentNote.setTextColor(Color.parseColor(colorTvPaymentNote));
					if (textStyle != null) {
						tvPaymentNote.setTypeface(textStyle);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		if (titlePayment!= null) {
			dialogShowSMSCharging.setTitle(titlePayment);
		}
		if (backgroundColorBtnPayment != null && textColorBtnPayment != null && cornerRadiusPayment >= 0) {
			dialogShowSMSCharging.setBackGroundButttonPayment(backgroundColorBtnPayment, textColorBtnPayment, cornerRadiusPayment);
		}
		dialogShowSMSCharging.setButtonTypeface(textStyle);
		dialogShowSMSCharging.setPaymentButtonClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = sp.getSelectedItemPosition();
				if (pos > 0) {
					String shortCode = null;
					if (listPricesSms != null) {
						shortCode = OnepayUtils.getShortCodeFromCode(context,selectItemPriceSms.replace(" VND", ""));
					}
					else{
						shortCode = shortCodes[pos - 1];
					}
					String contentSMS = mCharging.smsCharging.command+" "+exchangeOtherInformationSms;
					startSmsCharging(context, contentSMS, shortCode);// start sms charging
					//Log.e("tin nhan sms", contentSMS + " dau so: "+ shortCode);
					dialogShowSMSCharging.dismiss();
				}else {
					final MDialogNotify dialog = new MDialogNotify(context);
					if (titleSmsCharging != null) {
						dialog.setTitle(titleSmsCharging, textStyle);
					}else{
						dialog.setTitle(R.string.sms_charging, textStyle);
					}
					dialog.setContent(R.string.money_sms, textStyle);
					if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
						dialog.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
					}
					dialog.setCancelable(true);
					dialog.show();
					dialog.setButtonClose(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							sp.setFocusable(true);
							sp.setFocusableInTouchMode(true);
							sp.requestFocus();
							sp.performClick();
						}
					}); 
				}
			}
		});
		dialogShowSMSCharging.setEnableButtonsAbout(false);
		dialogShowSMSCharging.setBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogShowSMSCharging.dismiss();
				context.getApplicationContext().unregisterReceiver(messageSentSms);
				context.getApplicationContext().unregisterReceiver(messageSentSmsPlus);
			}
		});
		// set help button sms  click
		dialogShowSMSCharging.setHelpButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (enableSms) {
					dialogShowSMSCharging.setEnableContentLayoutHelp(true);
				}else{
					dialogShowSMSCharging.setEnableContentLayoutHelp(false);
				}
				enableSms = !enableSms;
			}
		});
		dialogShowSMSCharging.show();
	}

	private void sendSMSWithApp(Context context, String content, String phoneNumber) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", content);
		sendIntent.putExtra("address", phoneNumber);
		sendIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(sendIntent);
	}
	private void startSmsCharging(final Context context ,final String message, final String phoneNumber) {
		try {
			String SENT = "SMS_SENT";
			String title = null;
			if (titleSmsCharging != null) {
				title = titleSmsCharging;
			}else{
				title = context.getResources().getString(R.string.sms_charging);
			}
			Intent i = new Intent(SENT);
			i.putExtra("titleSms", title);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, i, 0);
			//			PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,new Intent(DELIVERED), 0);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
		} catch (Exception e) {
			Log.e("erro", e.toString());
		}
	}

	private void startSmsPlusCharging(final Context context ,final String message, final String phoneNumber) {
		try {
			String SENTSMSPLUS = "SMSPLUS_SENT";
			String title = null;
			if (titleSmsPlusCharging != null) {
				title = titleSmsPlusCharging;
			}else{
				title = context.getResources().getString(R.string.smsplus_charging);
			}
			Intent i = new Intent(SENTSMSPLUS);
			i.putExtra("titleSmsPlus", title);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, i, 0);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
		} catch (Exception e) {
			Log.e("erro", e.toString());
		}
	}

	//====================== SUB CHARGING ===========================
	private void subscriptionCharging(Context context) {
		this.context = context;
		new SubcriptionChargingTask().execute();
	}

	private String subscriptionCharging(String msisdn, String username, String service_id) {
		QueryBuilder qBuilder = new QueryBuilder(this.mCharging.secret_key);
		qBuilder.put("access_key", "my_key");
		qBuilder.put("msisdn", "my_mobile_number");
		qBuilder.put("provider", "3");
		qBuilder.put("refcode", "");
		qBuilder.put("service_id", "my_service_id");
		qBuilder.put("username", "dangnv");
		String queryStr = qBuilder.getQueryString();
		return HttpHelper.getData(APIs.SUBSCRIPTION_CHARGING + "?" + queryStr);
	}

	private class SubcriptionChargingTask extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage(context.getString(R.string.charging));
			pd.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			return subscriptionCharging("afdrer3232dfdf", "dangnv", "adfdf33");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			Log.e(M1PaySDK.class.getName(), "subcription charging: " + result);
		}
	}

	//========================== Loads config =============================

	private class LoadConfigTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String responseString = null;
			try {
				String url = String.format(
						"http://api.1pay.vn/smsgw/application/charging/config?access_key=%s",//can hoi
						mCharging.access_key);
				responseString = HttpHelper.getData(url);
			} catch (Exception e) {
				Log.e(M1PaySDK.class.getName(), "" + e.getMessage());
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null && !result.trim().equalsIgnoreCase("")) {
				parseData(result);
			}
		}
	}

	private void parseData(String base64String) {
		try {
			byte[] data = Base64.decode(base64String, Base64.DEFAULT);
			String jsonString = new String(data, "UTF-8");
			mCharging = Charging.createFromJsonString(jsonString);
			if (mCharging != null) { //success
				mPrefs.edit().putString(SDK_DATA_CONFIG, base64String).commit(); //Save lastest configuration data to local
			}
		} catch (IllegalArgumentException e) {
			Log.e(M1PaySDK.class.getName(), "" + e.getMessage());
		} catch (JSONException e) {
			Log.e(M1PaySDK.class.getName(), "" + e.getMessage());
		} catch (Exception e) {
			Log.e(M1PaySDK.class.getName(), "" + e.getMessage());
		}
	}
	public void showDialogSentSms(String title, String content) {
		final MDialogNotify dialognt = new MDialogNotify(context);
		dialognt.setTitle(title, textStyle);
		dialognt.setContent(content, textStyle);
		if (backgroundColorTitle != null && colorTextTitleShowPayment != null && typefaceStyleTitleShowPayment != null) {
			dialognt.setBackGroundTitleMDialog(backgroundColorTitle, colorTextTitleShowPayment, typefaceStyleTitleShowPayment);
		}
		dialognt.setCancelable(true);
		dialognt.setButtonClose(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialognt.dismiss();
			}
		}); 
		dialognt.show();
	}

	private class sentSms extends BroadcastReceiver{
		@Override
		public void onReceive(Context ctx, Intent intent) {
			try {
				String title = new String(intent.getStringExtra("titleSms"));
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					String contentSent = context.getResources().getString(R.string.sms_sent);
					showDialogSentSms(title,contentSent);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					String contentNoService = context.getResources().getString(R.string.sms_noservice);
					showDialogSentSms(title, contentNoService);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	private class sentSmsPlus extends BroadcastReceiver{
		@Override
		public void onReceive(Context ctx, Intent intent) {
			try {
				String title = new String(intent.getStringExtra("titleSmsPlus"));
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					String contentSent = context.getResources().getString(R.string.sms_sent);
					showDialogSentSms(title,contentSent);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					String contentNoService = context.getResources().getString(R.string.sms_noservice);
					showDialogSentSms(title, contentNoService);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

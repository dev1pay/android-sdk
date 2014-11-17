
package com.mwork.views;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mwork.configBackGround.SetBackGroundOnepay;
import com.mwork.onepaysdk.R;
import com.mwork.onepaysdk.utils.DrawableFactory;
import com.mwork.onepaysdk.utils.Utils;

public class MDialog extends Dialog {
	private TextView tvTitle;

	private TextView tvContent;

	private FrameLayout mContentFrame,mContentFrameHelp;

	private Context mContext;

	private TextView gameapp;

	private TextView accountname;

	private ImageView imgView;

	private ImageView imgBack;

	private ImageView imgViewAbout;

	private ImageView imgViewHelp;

	private View mainLayout;

	Button btnPayment;

	TextView tvPaymentNote;

	private RelativeLayout rltitle;

	private View vhorizontalone;

	private final String SDK_PREFERENCE_CONFIG = "1pay_sdk_prefs_config";

	private final String SDK_hexSolidColor = "hexSolidColor";

	private final String SDK_hexBoundColor = "hexBoundColor";

	private final String SDK_connerRadius = "connerRadius";

	private final String SDK_strokeWidth = "strokeWidth";

	private SharedPreferences mPrefsConfig;

	private int sdkVer;

	public MDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public MDialog(Context context) {
		super(context, R.style.MDialog);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		this.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.m_dialog);
		this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
		mContentFrame = (FrameLayout) findViewById(R.id.content_layout);
		mContentFrameHelp = (FrameLayout) findViewById(R.id.content_layout_help);
		imgView = (ImageView) findViewById(R.id.logo);
		imgBack = (ImageView)findViewById(R.id.back);
		imgViewAbout = (ImageView) findViewById(R.id.about);
		imgViewHelp = (ImageView) findViewById(R.id.help);
		mainLayout = findViewById(R.id.mainLayout);
		vhorizontalone =findViewById(R.id.vhorizontalone);
		sdkVer = android.os.Build.VERSION.SDK_INT;
		accountname = (TextView)findViewById(R.id.accountname);
		gameapp = (TextView)findViewById(R.id.gameapp);
		rltitle = (RelativeLayout)findViewById(R.id.rltitle);
		btnPayment = (Button)findViewById(R.id.btnPayment);
		tvPaymentNote = (TextView)findViewById(R.id.tvPaymentNote);
		customThem(context);
	}

	@SuppressWarnings("deprecation")
	public void customThem(Context context){
		imgView.setImageBitmap(Utils.getBitmapFromRaw(getContext(), R.drawable.logo));
		mPrefsConfig = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE_CONFIG,
				context.MODE_PRIVATE);		
		if (mPrefsConfig.contains(SDK_hexSolidColor) && mPrefsConfig.contains(SDK_hexBoundColor) && mPrefsConfig.contains(SDK_connerRadius) && mPrefsConfig.contains(SDK_strokeWidth)) {
			String hexSolidColor = mPrefsConfig.getString(SDK_hexSolidColor, "#ffffff");
			String hexBoundColor =  mPrefsConfig.getString(SDK_hexBoundColor, "#39b54a");;
			int connerRadius = mPrefsConfig.getInt(SDK_connerRadius, 8) ;
			int strokeWidth = mPrefsConfig.getInt(SDK_strokeWidth, 2);
			Drawable dthem = DrawableFactory.makeRoundedRectangeBox( hexSolidColor, hexBoundColor,
					connerRadius, strokeWidth);
			if(sdkVer < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				mainLayout.setBackgroundDrawable(dthem);
			} else {
				mainLayout.setBackground(dthem);
			}

		}else {
			loadTheme();
		}

	}



	@SuppressWarnings("deprecation")
	private void loadTheme() {
		imgView.setImageBitmap(Utils.getBitmapFromRaw(getContext(), R.drawable.logo));
		try {
			String themeString = Utils.readFileFromRaw(getContext(), R.raw.config_theme);
			JSONObject obj = new JSONObject(themeString);
			String bgColorString = obj.getString("main_background");
			if (bgColorString != null) {
				Drawable d = DrawableFactory.makeRoundedRectangeBox("#ffffff", bgColorString, 8,
						getContext().getResources().getDimensionPixelSize(R.dimen.stroke));
				if (d != null) {
					if(sdkVer < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						mainLayout.setBackgroundDrawable(d);
					} else {
						mainLayout.setBackground(d);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setEnableIcon(boolean enable) {
		if (enable) {
			imgView.setVisibility(View.VISIBLE);
		}else{
			imgView.setVisibility(View.GONE);
		}
	}

	public void setButtonTypeface( Typeface tf) {
		if (tf != null) {
			btnPayment.setTypeface(tf);
		}
	}

	public void setBackGroundButttonPayment(String backgroundColorBtnPayment, String textColorBtnPayment, float cornerRadiusPayment) {
		if(sdkVer < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			btnPayment.setBackgroundDrawable(SetBackGroundOnepay.backgroundButton(backgroundColorBtnPayment, cornerRadiusPayment));
		} else {
			btnPayment.setBackground(SetBackGroundOnepay.backgroundButton(backgroundColorBtnPayment, cornerRadiusPayment));
		}	
		btnPayment.setTextColor(Color.parseColor(textColorBtnPayment));
	}

	public void  setTitlebtnPayment(String title, Typeface tf) {
		if (title != null && title.trim().equalsIgnoreCase("")) {
			btnPayment.setText(title);
		}
		if (tf != null) {
			btnPayment.setTypeface(tf);
		}
	}
	public void setEnableBack(boolean enable) {
		if (enable) {
			imgBack.setVisibility(View.VISIBLE);
		}else{
			imgBack.setVisibility(View.GONE);
		}
	}

	public void setEnableButtonsHelp(boolean enable) {
		if (enable) {
			imgViewHelp.setVisibility(View.VISIBLE);
		}else{
			imgViewHelp.setVisibility(View.GONE);
		}
	}

	public void setEnableButtonsAbout(boolean enable) {
		if (enable) {
			imgViewAbout.setVisibility(View.VISIBLE);
		}else{
			imgViewAbout.setVisibility(View.GONE);
		}
	}

	public void  setEnableButtonPay(boolean enable) {
		if (enable) {
			btnPayment.setVisibility(View.VISIBLE);
		}else{
			btnPayment.setVisibility(View.GONE);
		}
	}
	public void setButtonPay(String name, Typeface tf) {
		if (name != null && !name.equalsIgnoreCase("")) {
			btnPayment.setVisibility(View.VISIBLE);
			if (tf != null) {
				btnPayment.setTypeface(tf);
			}
			btnPayment.setText(name);
		}
	} 

	public void setNameGame(String name, Typeface tf) {
		if (name != null && !name.equalsIgnoreCase("")) {
			gameapp.setVisibility(View.VISIBLE);
			if (tf != null) {
				gameapp.setTypeface(tf);
			}
			gameapp.setText(name);
		}
	} 

	public void setEnableAccountName(boolean enable) {
		if (enable) {
			accountname.setVisibility(View.VISIBLE);
			gameapp.setVisibility(View.VISIBLE);
		}else{
			accountname.setVisibility(View.GONE);
			gameapp.setVisibility(View.GONE);
		}
	}

	public void setAccountName(String name, Typeface tf) {
		if (name != null && !name.equalsIgnoreCase("")) {
			accountname.setVisibility(View.VISIBLE);
			if (tf != null) {
				accountname.setTypeface(tf);
			}
			accountname.setText(name);
		}
	}
	public void setIcon(int resId) {
		tvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
	}

	public MDialog setContentLayout(int resId) {// can hoi
		tvContent.setVisibility(View.GONE);
		View content_layout = LayoutInflater.from(mContext).inflate(resId, null);
		return setContentLayout(content_layout);
	}

	public MDialog setContentLayout(View content_layout) {
		tvContent.setVisibility(View.GONE);
		mContentFrame.addView(content_layout, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return this;
	}

	public MDialog setContentLayoutHelp(View content_layout) {
		mContentFrameHelp.addView(content_layout, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return this;
	}

	public void setEnableContentLayoutHelp(boolean enable) {
		if (enable) {
			mContentFrameHelp.setVisibility(View.VISIBLE);
			vhorizontalone.setVisibility(View.VISIBLE);
		}else{
			mContentFrameHelp.setVisibility(View.GONE);
			vhorizontalone.setVisibility(View.GONE);
		}
	}

	public void setEnableContentLayout(boolean enable) {
		if (!enable) {
			mContentFrame.setVisibility(View.GONE);	
		}else{
			mContentFrame.setVisibility(View.VISIBLE);
		}

	}

	public void setTitle(int resId, Typeface tf) {
		tvTitle.setText(mContext.getResources().getString(resId));
		if (tf != null) {
			tvTitle.setTypeface(tf);
		}
	}

	public void setBackGroundTitle(String colorBackground, String colorTextTitle, Typeface textStyle) {
		rltitle.setBackgroundColor(Color.parseColor(colorBackground));
		tvTitle.setTextColor(Color.parseColor(colorTextTitle));
		tvTitle.setTypeface(textStyle);
	}

	public void setTitle(String title, Typeface tf) {
		if (title != null && !title.trim().equals("")) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(title);
		}else{
			tvTitle.setVisibility(View.GONE);
		}
		if (tf != null) {
			tvTitle.setTypeface(tf);
		}
	}

	public void setContent(int resId) {
		tvContent.setText(mContext.getResources().getString(resId));
	}

	public void setContent(String content) {
		tvContent.setVisibility(View.VISIBLE);
		tvContent.setText(content);
	}

	public MDialog setBackButton(final android.view.View.OnClickListener listener) {
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(v);
				} else {
					dismiss();
				}
			}
		});
		return this;
	}

	public MDialog setPaymentButtonClick(final android.view.View.OnClickListener listener){
		btnPayment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(v);
				}else{
					dismiss();
				}
			}
		});
		return this;

	}

	public MDialog setAboutButton(final android.view.View.OnClickListener listener) {
		imgViewAbout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(v);
				} else {
					dismiss();
				}
			}
		});
		return this;
	}

	public MDialog setHelpButton(final android.view.View.OnClickListener listener) {
		imgViewHelp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick(v);
				} else {
					dismiss();
				}
			}
		});
		return this;
	}

	public void setLayoutPadding(int left, int top, int right, int bottom) {
		mContentFrame.setPadding(left, top, right, bottom);
	}

}

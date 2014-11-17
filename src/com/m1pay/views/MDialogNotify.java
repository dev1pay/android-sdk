
package com.m1pay.views;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.m1pay.M1PaySDK.R;
import com.m1pay.utils.DrawableFactory;
import com.m1pay.utils.Utils;

public class MDialogNotify extends Dialog {
	private TextView tvTitle;

	private TextView tvContent;

	private Context mContext;

	private ImageView imgViewClose;

	private View mainLayout;

	private final String SDK_PREFERENCE_CONFIG = "1pay_sdk_prefs_config";

	private final String SDK_hexSolidColor = "hexSolidColor";

	private final String SDK_hexBoundColor = "hexBoundColor";

	private final String SDK_connerRadius = "connerRadius";

	private final String SDK_strokeWidth = "strokeWidth";

	private SharedPreferences mPrefsConfig;
	
	private int sdkVerN;
	
	public MDialogNotify(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public MDialogNotify(Context context) {
		super(context, R.style.MDialog);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		this.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.m_dialog_notify);
		this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
		imgViewClose = (ImageView) findViewById(R.id.btnClose);
		mainLayout = findViewById(R.id.main_layout);
		sdkVerN = android.os.Build.VERSION.SDK_INT;
		customThem(context);
	}
	@SuppressWarnings("deprecation")
	public void customThem(Context context){
		mPrefsConfig = (SharedPreferences) context.getSharedPreferences(SDK_PREFERENCE_CONFIG,
				context.MODE_PRIVATE);		
		if (mPrefsConfig.contains(SDK_hexSolidColor) && mPrefsConfig.contains(SDK_hexBoundColor) && mPrefsConfig.contains(SDK_connerRadius) && mPrefsConfig.contains(SDK_strokeWidth)) {
			String hexSolidColor = mPrefsConfig.getString(SDK_hexSolidColor, "#ffffff");
			String hexBoundColor =  mPrefsConfig.getString(SDK_hexBoundColor, "#39b54a");;
			int connerRadius = mPrefsConfig.getInt(SDK_connerRadius, 8) ;
			int strokeWidth = mPrefsConfig.getInt(SDK_strokeWidth, 2);
			Drawable dthem = DrawableFactory.makeRoundedRectangeBox( hexSolidColor, hexBoundColor,
					connerRadius, strokeWidth);
			if(sdkVerN < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
		try {
			String themeString = Utils.readFileFromRaw(getContext(), R.raw.config_theme);
			JSONObject obj = new JSONObject(themeString);
			String bgColorString = obj.getString("main_background");
			if (bgColorString != null) {
				Drawable d = DrawableFactory.makeRoundedRectangeBox("#ffffff", bgColorString, 8,
						getContext().getResources().getDimensionPixelSize(R.dimen.stroke));
				if (d != null) {
					if(sdkVerN < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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

	public void setIcon(int resId) {
		tvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
	}

	public void setTitle(int resId, Typeface tf) {
		tvTitle.setText(mContext.getResources().getString(resId));
		if (tf != null) {
			tvTitle.setTypeface(tf);
		}
	}

	public void setTitle(String title, Typeface tf) {
		if (title != null && !title.trim().equals("")) {
			tvTitle.setText(title);
		} else {
			tvTitle.setVisibility(View.GONE);
		}
		if (tf != null) {
			tvTitle.setTypeface(tf);
		}
	}
	
	public void setBackGroundTitleMDialog(String colorBackground, String colorTextTitle, Typeface textStyle) {
		tvTitle.setBackgroundColor(Color.parseColor(colorBackground));
		tvTitle.setTextColor(Color.parseColor(colorTextTitle));
		tvTitle.setTypeface(textStyle);
	}

	public void setContent(int resId, Typeface tf) {
		tvContent.setText(mContext.getResources().getString(resId));
		if (tf != null) {
			tvContent.setTypeface(tf);
		}
	}

	public void setContent(String content, Typeface tf) {
		tvContent.setVisibility(View.VISIBLE);
		tvContent.setText(content);
		if (tf != null) {
			tvContent.setTypeface(tf);
		}
	}
	
	public MDialogNotify setButtonClose(final android.view.View.OnClickListener listener) {
		imgViewClose.setOnClickListener(new View.OnClickListener() {

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
}

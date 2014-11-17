
package com.m1pay.m1payApiLibs;

import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;

public class Utils {
    public static Dialog showOkDialog(Context context, String title, String message,
            OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", onClickListener);
        builder.show();
        return builder.create();
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

    public static String getCarrierCode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String mccmnc = telephonyManager.getSimOperator();
        if (mccmnc == null) {
            return null;
        }
        String code = null;
        if (mccmnc.equals("45204")) {
            code = "viettel";
        } else if (mccmnc.equals("45201")) {
            code = "mobifone";
        } else if (mccmnc.equals("45202")) {
            code = "vinaphone";
        } else if (mccmnc.equals("45205")) {
            code = "vietnamobile";
        } else if (mccmnc.equals("45203")) {
            code = "sfone";
        }
        return code;
    }

    
}

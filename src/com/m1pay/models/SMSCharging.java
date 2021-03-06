
package com.m1pay.models;

import org.json.JSONException;
import org.json.JSONObject;

public class SMSCharging {
    public boolean status = true;

    public String shortCodes;

    public String command;

    public String mt_type;

    public String default_mt;

    public SMSCharging() {
    }

    static public SMSCharging createFromJsonString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            SMSCharging sms = new SMSCharging();
            sms.command = obj.getString("command");
            sms.shortCodes = obj.getJSONArray("shortcodes").toString();
            sms.mt_type = obj.getString("mt_type");
            if (obj.get("status").equals("approved")) {
                sms.status = true;
            } else {
                sms.status = false;
            }
            return sms;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return null;
    }
}

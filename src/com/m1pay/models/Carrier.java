
package com.m1pay.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Carrier {
    public String name;

    public String[] prices;

    public String code;

    public String sms;

    public int unit;

    public static Carrier createCarrierFromJson(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            Carrier carrier = new Carrier();
            carrier.name = obj.getString("name");
            carrier.code = obj.getString("code");
            carrier.sms = obj.getString("sms");
            carrier.unit = obj.getInt("unit");
            carrier.prices = obj.getJSONArray("prices").toString().replace("[", "")
                    .replace("]", "").split(",");
            return carrier;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return null;
    }

}

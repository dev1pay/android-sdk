
package com.m1pay.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsPlusCharging {
	public boolean status = true;
	
    public String game_code;

    public String mo_vtm;

    public String notify_url;

    public String mo_vms;

    public String service_number;

    public ArrayList<Carrier> carriers;

    public SmsPlusCharging() {
       
        carriers = new ArrayList<Carrier>();
    }

    public Carrier getLocalCarrier(String code) {
        for (Carrier c : carriers) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        return null;
    }

    public static SmsPlusCharging createFromJsonString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            SmsPlusCharging iac = new SmsPlusCharging();
            iac.game_code = obj.getString("game_code");
            iac.mo_vms = obj.getString("mo_vms");
            iac.mo_vtm = obj.getString("mo_vtm");
            iac.notify_url = obj.getString("notify_url");
            iac.service_number = obj.getString("service_number");
            JSONArray array = obj.getJSONArray("charging");
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    Carrier carrier = Carrier.createCarrierFromJson(array.getString(i));
                    if (carrier != null) {
                        iac.carriers.add(carrier);
                    }
                }
            }

            if (obj.get("status").equals("approved")) {
                iac.status = true;
            } else {
                iac.status = false;
            }
            return iac;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

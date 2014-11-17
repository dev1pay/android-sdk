
package com.mwork.onepaysdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Charging {
    public String access_key;

    public String secret_key;

    public String id;

    public boolean status;
    
    public String description;

    public String app_id;

    public String app_code;

    public SmsCharging smsCharging;
    
    public SmsPlusCharging smsPlusCharging;

    public CardCharging cardCharging;

    static public Charging createFromJsonString(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
       
        Charging charging = new Charging();
        charging.access_key = obj.getString("access_key");
        charging.secret_key = obj.getString("secret");
        charging.id = obj.getString("id");
        charging.app_id = obj.getString("app_id");
        charging.app_code = obj.getString("app_code");
        charging.description = obj.getString("description");
        if (obj.get("status").equals("approved")) {
        	charging.status = true;
        } else {
        	charging.status = false;
        }
        if (!obj.isNull("iac_charging")) {
        	 charging.smsPlusCharging = SmsPlusCharging.createFromJsonString(obj.getString("iac_charging"));
		}
        if (!obj.isNull("sms_charging")) {
        	charging.smsCharging = SmsCharging.createFromJsonString(obj.getString("sms_charging"));
		}
        if (!obj.isNull("card_charging")) {
        	charging.cardCharging = CardCharging.createFromJsonString(obj.getString("card_charging"));
		}
        
        return charging;
    }

    public class StatusCode {
        public static final String SUCCESS = "00"; // Transaction success

        public static final String CARD_UNSUPPORT = "01"; // Unsupport card or
                                                          // wrong

        // value of card
        public static final String CARD_INVALID = "07"; // Invalid card or being
                                                        // used

        public static final String UNDEFIED = "10"; // Undefied

        public static final String SYSTEM_ERROR = "13"; // System error
    }

    public class Status {
        public static final int SUCCESS = 1;

        public static final int FAILED = 2;
    }

    public class ChargingType {
        public static final int SMS_CHARGING = 1;

        public static final int CARD_CHARGING = 2;

        public static final int SUBSCRIPTION = 3;

        public static final int WAP_CHARGING = 4;
    }
}


package com.mwork.onepaysdk.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CardCharging {
    public boolean status = true;
    
    public String card_types;

    static public CardCharging createFromJsonString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            CardCharging card = new CardCharging();
            card.card_types = obj.getJSONArray("card_types").toString();		
            if (obj.get("status").equals("approved")) {
                card.status = true;
            } else {
                card.status = false;
            }
            return card;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return null;
    }
}


package com.mwork.OnePayApiLibs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class QueryBuilder {

    private HashMap<String, String> params = new HashMap<String, String>();

    private String secret;

    public static QueryBuilder getInstance(String secret) {
        return new QueryBuilder(secret);
    }

    public QueryBuilder(String secret) {
        this.secret = secret;
    }

    public void put(String key, String value) {
        if (value != null && !value.trim().equals(""))
            params.put(key, value);
    }

    public void clear() {
        params.clear();
    }

    public String getQueryString() {
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        LinkedList<String> paramList = new LinkedList<String>();
        for (String key : keys) {
            paramList.add(String.format("%s=%s", key, params.get(key)));
        }

        String query = StringUtils.join(paramList.toArray(), "&");

        String signature = HmacSHA256.getInstance(secret).sign(query);

        return String.format("%s&signature=%s", query, signature);
    }

    public static void main(String[] args) {
        QueryBuilder builder = new QueryBuilder("my_sceret");
        // Call this if you want to reuse the QueryBuilder
        //builder.clear();
        builder.put("access_key", "my_key");
        builder.put("msisdn", "my_mobile_number");
        builder.put("provider", "3");
        builder.put("refcode", "");
        builder.put("service_id", "my_service_id");
        builder.put("username", "a");

        System.out.println(builder.getQueryString());
    }
}

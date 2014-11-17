
package com.mwork.libs.networks;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HttpHelper {
	
	private static final String TAG = "HttpHelper";

	
	public static String getData(String url) {
		StringBuilder builder = new StringBuilder();	
		InputStream is = null;
		try {

			HttpClient client = HttpUtils.getNewHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse httpResponse = client.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpResponse != null) {
				int responseCode = (httpResponse.getStatusLine() != null) ? (httpResponse.getStatusLine().getStatusCode()) : 1000;
				if (responseCode == HttpURLConnection.HTTP_OK) {
					is = httpEntity.getContent();
				}
			}
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			String line = "";
			if (buffer != null ) {
				while ((line = buffer.readLine()) != null) {
					builder.append(line);
				}
			}
			else {
				return null;
			}

		} catch (Exception e) {
			Log.e(TAG, "Failed to download data: " + e.toString());
			return null;
		}
		return builder.toString();
	}

	public static String getData(String url, List<NameValuePair> nameValuePairs) {
		String result = null;
		HttpResponse response;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(request);
			if (response != null) {
				int responseCode = (response.getStatusLine() != null) ? (response.getStatusLine().getStatusCode()) : 1000;
				if (responseCode == HttpURLConnection.HTTP_OK) {
					result = inputStreamToString(response.getEntity().getContent());
				}
			}

		} catch (UnsupportedEncodingException e) {
			result = null;
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			result = null;
			e.printStackTrace();
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		} catch (Exception e){
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	public static String post(String url, String params) {
		String result = null;
		HttpResponse response;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new StringEntity(params));
			response = client.execute(request);
			if (response != null) {
				int responeCode = (response.getStatusLine() != null) ? (response.getStatusLine().getStatusCode()) : 1000;
				if (responeCode == HttpURLConnection.HTTP_OK) {
					result = inputStreamToString(response.getEntity().getContent());
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result = null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
	public static String getData(String url, List<NameValuePair> nameValuePairs, String photoPath) {
		String result = null;
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			FileEntity photo = new FileEntity(new File(photoPath), "jpg");
			request.setEntity(photo);

			HttpResponse response = client.execute(request);
			result = inputStreamToString(response.getEntity().getContent());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	

	public static String inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "Error read data");
			return null;
		}
		// Return full string
		return total.toString();
	}
}

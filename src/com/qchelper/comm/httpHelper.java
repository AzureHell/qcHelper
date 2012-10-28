package com.qchelper.comm;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class httpHelper {
    final static String DEBUG_TAG = "httpHelper";
    //private final static String SERVER_URL = "http://116.211.12.192:8080/";
    //private String SERVER_URL;
    
    public static String invoke(String actionName, List<NameValuePair> params) {
        String result = null;
        try {       	
            //String url = SERVER_URL + actionName;
        	
        	String url = actionName;
        	
            Log.d(DEBUG_TAG, "url is: " + url);
            HttpPost httpPost = new HttpPost(url);
            if (params != null && params.size() > 0) {
                Log.d(DEBUG_TAG, "params: " + params.toString());
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity()).replaceAll("\r", "");
                Log.d(DEBUG_TAG, "result is ( " + result + " )");
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.toString());
        }
        Log.d(DEBUG_TAG, "over");
        return result;
    }
    public static String invoke(String actionName) {
        return invoke(actionName, null);
    }
}
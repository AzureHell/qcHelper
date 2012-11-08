package com.qchelper.comm;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class httpHelper {
    final static String DEBUG_TAG = "httpHelper";
    
    public static String invoke(String actionName, String json) {
        String result = null;
        try {       	
        	String url = actionName;
        	
            Log.d(DEBUG_TAG, "url is: " + url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json; charset="+HTTP.UTF_8);
            httpPost.addHeader("charset", HTTP.UTF_8);
            if (json != "") {
                Log.d(DEBUG_TAG, "params: " + json);
                httpPost.setEntity(new StringEntity(json, HTTP.UTF_8));
            }
            HttpParams httpParms = new BasicHttpParams();
            httpParms.setParameter("charset", HTTP.UTF_8);
            HttpClient httpClient = new DefaultHttpClient(httpParms);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
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
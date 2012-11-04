package com.qchelper.comm;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class httpHelper {
    final static String DEBUG_TAG = "httpHelper";
    
    public static String invoke(String actionName, List<NameValuePair> params) {
        String result = null;
        try {       	
        	String url = actionName;
        	
            Log.d(DEBUG_TAG, "url is: " + url);
            HttpPost httpPost = new HttpPost(url);
            if (params != null && params.size() > 0) {
                Log.d(DEBUG_TAG, "params: " + params.toString());
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            HttpParams httpParms = new BasicHttpParams();
            httpParms.setParameter("charset", HTTP.UTF_8);
            HttpResponse httpResponse = new DefaultHttpClient(httpParms).execute(httpPost);
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
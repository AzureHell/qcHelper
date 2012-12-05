package com.qchelper.comm;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
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
            if (json != "") {
                Log.d(DEBUG_TAG, "params: " + json);
                httpPost.setEntity(new StringEntity(json, HTTP.UTF_8));
            }
            HttpParams httpParms = new BasicHttpParams();
            httpParms.setParameter("charset", HTTP.UTF_8);
            HttpClient httpClient = new DefaultHttpClient(httpParms);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d(DEBUG_TAG, "finished");
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                Log.d(DEBUG_TAG, "result is ( " + result + " )");
            } else {
            	return null;
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
    
    public static String invokePic(String actionName, String fileName, byte[] pic) {
        String result = null;
        try {           
            String url = actionName;
            Log.d(DEBUG_TAG, "url is: " + url);
            HttpPost httpPost = new HttpPost(url);
            
//            httpPost.addHeader("Content-Type", "multipart/form-data");
            if (pic != null) {
                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                mpEntity.addPart("fileName",  new StringBody(fileName));
                try{
                    ByteArrayBody bab = new ByteArrayBody(pic, fileName);
                    mpEntity.addPart("data", bab);
                }
                catch(Exception e){
                    Log.e(DEBUG_TAG, e.toString());
                    mpEntity.addPart("data", new StringBody(""));
                }
                httpPost.setEntity(mpEntity);
            }
            HttpParams httpParms = new BasicHttpParams();
            httpParms.setParameter("charset", HTTP.UTF_8);
            HttpClient httpClient = new DefaultHttpClient(httpParms);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d(DEBUG_TAG, "finished");
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                Log.d(DEBUG_TAG, "result is ( " + result + " )");
            } else {
            	result = null;
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.toString());
        }
        Log.d(DEBUG_TAG, "over");
        return result;
    }
}
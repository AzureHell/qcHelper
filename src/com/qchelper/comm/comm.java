package com.qchelper.comm;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class comm {

//    public final static String webServiceURL = "http://116.211.12.192:8080/";
    /* 
    * MD5加密 
    */  
    public static String getMD5Str(String str) {
          MessageDigest messageDigest = null;       
         
          try {       
              messageDigest = MessageDigest.getInstance("MD5");       
         
              messageDigest.reset();
         
              messageDigest.update(str.getBytes("UTF-8"));
          } catch (NoSuchAlgorithmException e) {       
              System.out.println("NoSuchAlgorithmException caught!");
              System.exit(-1);
          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }
          
          byte[] byteArray = messageDigest.digest();
          
          StringBuffer md5StrBuff = new StringBuffer();
          
          for (int i = 0; i < byteArray.length; i++) {                   
              if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)       
                  md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));       
              else
                  md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));       
          }
          //16位加密，从第9位到25位
          // 修改成32位加密
          //return md5StrBuff.substring(8, 24).toString().toUpperCase();
          return md5StrBuff.substring(0, 32).toString();
      }
    
    public static String getMD5DStr(String str) {
        return getMD5Str(getMD5Str(str));
    }

    public static String getResourceString(Context con, int R_id) {
        Resources res = con.getResources();
        return res.getString(R_id);
    }
    
    public static void showMsg(Context con, String msg) {
        showMsg(con, msg, 2000);
    }
    
    public static void showMsg(Context con, String msg, int duration) {
        Toast.makeText(con, msg, duration).show();
    }
    
    public static void showMsg(Context con, int R_id) {
        showMsg(con, R_id, 2000);
    }
    
    public static void showMsg(Context con, int R_id, int duration) {
        showMsg(con, getResourceString(con, R_id), 2000);
    }    
    
    public static void showErrorMsg(Context con, String msg) {
        showErrorMsg(con, msg, 2000);
    }
      
    public static void showErrorMsg(Context con, String msg, int duration) {
        Toast.makeText(con, msg, duration).show();
    }
    
    public static void showErrorMsg(Context con, int R_id) {
        showErrorMsg(con, R_id, 2000);
    }
      
    public static void showErrorMsg(Context con, int R_id, int duration) {
        showErrorMsg(con, getResourceString(con, R_id), 2000);
    }         

    public static String getNowStringDateTime() {
        return getStringDateTime(new Date());
     }        
    
    public static String getStringDateTime(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dt);
        return dateString;
     }    
    
    public static Date getDateTime(Date dt, String type, Integer i) {
        Calendar canlandar = Calendar.getInstance();
        canlandar.setTime(dt);
        
        if (type.equalsIgnoreCase("day"))
            canlandar.add(Calendar.DATE, i);
        else if (type.equalsIgnoreCase("week"))
            canlandar.add(Calendar.DATE, i*7);
        else if (type.equalsIgnoreCase("month"))
            canlandar.add(Calendar.MONTH, i);
        
        return canlandar.getTime();
     }    

    public static String getNowStringDate() {
        return getNowStringDateTime().substring(0, 10);
     }    
    
    public static String getNowStringTime() {
        return getNowStringDateTime().substring(11, 19);
     }    
    
    public static boolean isWiFiActive(Context context) {
        WifiManager mWifiManager = (WifiManager) context
        .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            return true;
        };
        return false; 
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
                return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null) {
                return false;
            } else {
                if (info.isAvailable()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static String getHttpUrl(Context con, String actionName) {
        String url = "";
        dbHelper dbhlp = new dbHelper(con);
        Cursor dbcur = dbhlp.select("ServerCon");
        if (dbcur.getCount() > 0) {
            dbcur.moveToFirst();
            if ((dbcur.getString(dbcur.getColumnIndex("server_ip")).length() > 0) && (dbcur.getString(dbcur.getColumnIndex("server_port")).length() > 0)) {
                url = "http://" + dbcur.getString(dbcur.getColumnIndex("server_ip")) 
                  + ":" + dbcur.getString(dbcur.getColumnIndex("server_port")) + "/" + actionName;
            }
        }
        return url;
    }
    
    public static String invokeHttp(Context con, String actionName, String params) {
        String url = "";
        url = getHttpUrl(con, actionName);
        if (url == ""){
            return null;
        }
        try {
            return httpHelper.invoke(url, params);
        } catch (Exception e) {
            return null;
        }
    }    
    
    public static String invokeHttp(Context con, String actionName, String fileName, byte [] pic) {
        String url = "";
        url = getHttpUrl(con, actionName);
        if (url == ""){
            return null;
        }
        try {
            return httpHelper.invokePic(url, fileName, pic);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String fmtHttpParams(String status, String  error, String data) {
        return "{\"status\":\""+ status +"\", \"error\":\""+ error 
                +"\", \"data\":["+ data.replace("'", "\"") +"]}";
    }
    
    public static String fmtHttpParams(String data) {
        return fmtHttpParams("succeed", "", data);
    }
    
    //图片压缩
    public static byte[] bitmapToBytes(Bitmap bitmap){
        if (bitmap == null) {
                return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);//除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }
    
    //图片解压
    public static Bitmap bytesTobitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }    
}

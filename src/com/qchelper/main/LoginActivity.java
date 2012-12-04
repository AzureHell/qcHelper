package com.qchelper.main;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qchelper.comm.comm;

public class LoginActivity extends Activity implements OnClickListener {
    final static String DEBUG_TAG = "LoginActivity";

    TextView vlogin_info;
    EditText edtuser_id, edtPassword;
    Button btnOk;    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "create");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        vlogin_info = (TextView)findViewById(R.id.login_hint);
        edtuser_id = (EditText)findViewById(R.id.user_id);
        edtPassword = (EditText)findViewById(R.id.password);
        btnOk = (Button)findViewById(R.id.login_ok);
        btnOk.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_ok: {
                Log.d(DEBUG_TAG, "click ok");
                if (!edtuser_id.getText().toString().equals("") && !edtPassword.getText().toString().equals("")) {
                    new loginAsyncTask().execute(edtuser_id.getText().toString(), edtPassword.getText().toString());
                } else {
                	if (edtuser_id.getText().toString().equals("")) {
                		comm.showMsg(this, R.string.login_user_name_lost);
                	} else if (edtPassword.getText().toString().equals("")) {
                		comm.showMsg(this, R.string.login_password_lost);
                	} else {                	
                		comm.showMsg(this, R.string.information_incomplete);
                	}
                }
                break;
            }
        }
    }
    
    public class loginAsyncTask extends AsyncTask<String, Void, String> {
        final static String DEBUG_TAG = "loginAsyncTask";
        private ProgressDialog progressDialog;
        
        protected String doInBackground(String... strJson) {
            Log.d(DEBUG_TAG, "doInBackground");
            
            String user_id = strJson[0];
            String password = strJson[1];
            
            // 为了与环思框架同步，密码增加后缀HUANSI
            String params = comm.fmtHttpParams("{'user_id':'"+user_id+"','password':'"+comm.getMD5DStr(password+"HUANSI")+"'}");
            try {
            	return comm.invokeHttp(LoginActivity.this, "checkuser", params);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, e.toString());
                return null;
            }                
        }

        @Override
        protected void onPreExecute() {
            Log.d(DEBUG_TAG, "onPreExecute");
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle(comm.getResourceString(LoginActivity.this, R.string.process_wait));
            progressDialog.show();
        }

        protected void onPostExecute(String result) {
            Log.d(DEBUG_TAG, "onPostExecute");
            progressDialog.cancel();
            if (result != null && result != "") {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("status").equals("succeed")) {
                        JSONObject jsonData = json.getJSONArray("data").getJSONObject(0);
                        
                        Log.d(DEBUG_TAG, jsonData.toString());
                        SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_WRITEABLE);
                        setting.edit()
                        .putString("user_id", jsonData.getString("user_id"))
                        .putString("user_name", jsonData.getString("user_name"))
                        .commit();
                        comm.showMsg(LoginActivity.this, R.string.login_succeed);
                        LoginActivity.this.finish();
                    } else if (json.getString("status").equals("failed")) {
                    	if (json.getString("error").equals("not record!")) {
                    		comm.showErrorMsg(LoginActivity.this, R.string.login_user_or_pass_error);
                    	} else if (json.getString("error").equals("p record!")) {
                    		comm.showErrorMsg(LoginActivity.this, R.string.login_user_or_pass_error);
                    	}
                    	else comm.showErrorMsg(LoginActivity.this, json.getString("error"));
                    }
                } catch (JSONException e) {
                    Log.e(DEBUG_TAG, e.toString());
                }
            } else {
                comm.showErrorMsg(LoginActivity.this, R.string.login_failed);
            }
        }
    }
}
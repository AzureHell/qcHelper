package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qchelper.comm.comm;
import com.qchelper.comm.httpHelper;

public class LoginActivity extends Activity implements OnClickListener {
    final static String DEBUG_TAG = "LoginActivity";

    TextView vlogin_info;
    EditText edtUserName, edtPassword;
    Button btnOk;    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "create");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        vlogin_info = (TextView)findViewById(R.id.login_hint);
        edtUserName = (EditText)findViewById(R.id.user_id);
        edtPassword = (EditText)findViewById(R.id.password);
        btnOk = (Button)findViewById(R.id.login_ok);
        btnOk.setOnClickListener(this);
    }
    
    @SuppressLint("WorldWriteableFiles") 
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_ok: {
                Log.d(DEBUG_TAG, "click ok");
                if (edtUserName.getText().toString() != "" && edtPassword.getText().toString() != "") {
                    new loginAsyncTask().execute(edtUserName.getText().toString(), edtPassword.getText().toString());
                } else {
                    comm.showMsg(this, R.string.information_incomplete);
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
            
            String username = strJson[0];
            String password = strJson[1];
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password",  comm.getMD5DStr(password)));
            
        	/* LJF */
        	String SERVER_URL = ""; 
        	dbHelper dbhlp = new dbHelper(LoginActivity.this);
        	Cursor dbcur = dbhlp.querySQL("select id, server_ip, server_port "
            		+ " from server_con "); 
            if (dbcur.getCount() > 0) {
            	dbcur.moveToFirst();
            	if ((dbcur.getString(dbcur.getColumnIndex("server_ip")).length() > 0) && (dbcur.getString(dbcur.getColumnIndex("server_port")).length() > 0)) {
                	SERVER_URL = "http://" + dbcur.getString(dbcur.getColumnIndex("server_ip")) 
                	  + ":" + dbcur.getString(dbcur.getColumnIndex("server_port")) + "/checkuser";                	
            	}

            }                
            if (SERVER_URL == ""){
                return null;
            } 
            
            try {
                //return  httpHelper.invoke("checkuser", params);
            	return  httpHelper.invoke(SERVER_URL, params);

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
            if (result != "") {
                try {
                    JSONObject json = new JSONObject(result);
                    SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_WRITEABLE);
                    setting.edit()
                    .putString("user_id", json.getString("user_id"))
                    .putString("user_name", json.getString("username"))
                    .commit();
                    comm.showMsg(LoginActivity.this, R.string.login_succeed);
                    LoginActivity.this.finish();
                } catch (JSONException e) {
                    Log.e(DEBUG_TAG, e.toString());
                }
            } else {
                comm.showMsg(LoginActivity.this, R.string.login_failed);
            }
        }
    }
}
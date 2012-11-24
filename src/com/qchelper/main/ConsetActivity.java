package com.qchelper.main;

import com.qchelper.comm.dbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConsetActivity extends Activity {
	final static String TAG = "SettingActivity";	
	Button btnOk;
	Button btnCancel;
	EditText edtIp;
	EditText edtPort;
	int currid;
    private dbHelper dbhlp;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.activity_conset);
        Log.d(TAG, "Create_1");
        edtIp = (EditText) findViewById(R.id.edt_server_ip);
        edtPort = (EditText) findViewById(R.id.edt_server_port);
        dbhlp = new dbHelper(this);
        Cursor dbcur = dbhlp.querySQL("select iID, server_ip, server_port "
        		+ " from ServerCon ");
        Log.d(TAG, "Create_2");
        if (dbcur.getCount() > 0) {
        	Log.d(TAG, "Create_3");
        	dbcur.moveToFirst();
        	currid = dbcur.getInt(dbcur.getColumnIndex("iID"));
        	edtIp.setText(dbcur.getString(dbcur.getColumnIndex("server_ip")));
        	edtPort.setText(dbcur.getString(dbcur.getColumnIndex("server_port")));
        }
        Log.d(TAG, "currid: "+Integer.toString(currid));
        Log.d(TAG, "Create_5");
        btnOk = (Button) findViewById(R.id.setting_ok);
        btnOk.setOnClickListener(new ClickEvent());
        btnCancel = (Button) findViewById(R.id.setting_cancel);
        btnCancel.setOnClickListener(new ClickEvent());
        
        edtIp.setText("192.168.100.200");
        edtPort.setText("8080");
    }
    
    class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
	      	  //if (v == btnOk)
			switch(v.getId()) {
    			case R.id.setting_ok: {
    	      		    if (edtIp.getText().length() <= 0){
    	      		    	PromptDialog(1);
    	      		    	edtIp.requestFocus();
    	      		    	break;
    	      		    }
    	      		    if (edtPort.getText().length() <= 0){
    	      		    	PromptDialog(2);
    	      		    	edtPort.requestFocus();
    	      		    	break;
    	      		    }
    	      		    
	      		    	if (currid > 0) {
                            if (dbhlp.update("ServerCon", currid, edtIp.getText().toString() + "," + edtPort.getText().toString()) > 0) {
                                Toast.makeText(ConsetActivity.this, R.string.conset_success, 1500).show();
                                finish();
                            }
	  	      		    }
	  	      		    else {
	  		      		    dbhlp.delete("ServerCon", currid);
	  		      		    if (dbhlp.insert("ServerCon", edtIp.getText().toString() + "," + edtPort.getText().toString()) > 0) {
	  		      		    	finish();	      		     	
	  		      		    }		      		    	
    	      		    }     		    
    	      		    break;
    			}
    	        //else if (v == btnCancel)
    			case R.id.setting_cancel: {
            		finish();
            		break;
    			}
    		}	  
		}
      }  
   
    public void PromptDialog(int infotype) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ConsetActivity.this);
    	if (infotype == 1){
        	builder.setMessage(R.string.prompt_ipnotnull);    		
    	}
    	else if (infotype == 2) {
    		builder.setMessage(R.string.prompt_protnotnull);
    	}

        builder.setTitle(R.string.dialog_titile);

        builder.setPositiveButton(R.string.btn_Ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {                
		    	dialog.dismiss();	
			}

    	});
    	builder.create().show();
     }
}

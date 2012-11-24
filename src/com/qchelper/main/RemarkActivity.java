package com.qchelper.main;

import com.qchelper.comm.dbHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RemarkActivity extends Activity {
    int KeyID;
    Button btnRemarkOk;
    Button btnRemarkCancel;
    EditText edtRemark;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        KeyID = bundle.getInt("KeyID");
        
        btnRemarkOk = (Button) findViewById(R.id.btnRemarkOk);
        btnRemarkOk.setOnClickListener(new ButtonClickEvent());
        btnRemarkCancel = (Button) findViewById(R.id.btnRemarkBack);
        btnRemarkCancel.setOnClickListener(new ButtonClickEvent());
        edtRemark = (EditText) findViewById(R.id.edtRemark);
        edtRemark.setText(GetRemarkData());
    }
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remark, menu);
        return true;
    }
    */
    
    private String GetRemarkData(){
    	String Data=null;
    	dbHelper dbhlp = new dbHelper(this);
    	Cursor cursor = dbhlp.querySQL("select sRemark from qmCheckRecordMst where iID=" + Integer.toString(KeyID));
    	if (cursor.getCount() > 0){
    		cursor.moveToFirst();
    		Data = cursor.getString(0);
    	}
    	dbhlp.close();    	
    	cursor.close();
    	return Data;
    }
    
    class ButtonClickEvent implements View.OnClickListener {

		public void onClick(View v) {
    		switch(v.getId()){
    		case R.id.btnRemarkOk: {
    			dbHelper dbhlp = new dbHelper(RemarkActivity.this);
    			dbhlp.updateRemark("qmCheckRecordMst", KeyID, edtRemark.getText().toString());
    			dbhlp.close();
    			finish();
    		}
    		case R.id.btnRemarkBack: {
    			finish();
    		}
    		}
			
		}
    }
}

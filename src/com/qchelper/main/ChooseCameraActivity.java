package com.qchelper.main;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;

import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import com.qchelper.comm.comm;
import com.qchelper.comm.dbHelper;

import android.widget.Button;
import android.view.View;


public class ChooseCameraActivity extends Activity {
	
	RadioGroup rgChooseCameraGroup;
	int ChooseParamValue = 0;
	int ParamID = -1;
	
	Button btnOk, btnCancel;
	
	private dbHelper dbhlp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_camera);
        
        rgChooseCameraGroup = (RadioGroup) findViewById(R.id.rg_ChooseCamera);
        dbhlp = new dbHelper(this);
        Cursor dbcur = dbhlp.querySQL("select iID, ParamValue "
        		+ " from HelperParam ");
        if (dbcur.getCount() > 0) {
        	dbcur.moveToFirst();
        	ParamID = dbcur.getInt(dbcur.getColumnIndex("iID")); 
        	ChooseParamValue = dbcur.getInt(dbcur.getColumnIndex("ParamValue"));
        }
        dbcur.close();
        dbhlp.close();
        
        if (ChooseParamValue == 0) {
        	rgChooseCameraGroup.check(R.id.rb_UsingInCamera);
        } else {
        	rgChooseCameraGroup.check(R.id.rb_UsingAppCamera);
        }
                
        btnOk = (Button) findViewById(R.id.btnChooseOk);
        btnOk.setOnClickListener(new ClickEvent());
        
        btnCancel = (Button) findViewById(R.id.btnChooseCancel);
        btnCancel.setOnClickListener(new ClickEvent());
        
        rgChooseCameraGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup rGroup, int chkID) {
				// TODO Auto-generated method stub
				if (chkID != -1) {
					RadioButton rb = (RadioButton) findViewById(chkID);
					if (rb != null) {
						if (rb.getId() == R.id.rb_UsingInCamera){
							ChooseParamValue = 0;
						} else {
							ChooseParamValue = 1;
						}
						
					}else {
						ChooseParamValue = 0;
					}
				}
			}
		});
    }

    class ClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.btnChooseOk: {
				if (ParamID > 0) {
					if (dbhlp.update("HelperParam", Integer.toString(ParamID),
							"ParamValue", new String[] {Integer.toString(ChooseParamValue) }) > 0) {
//						comm.showMsg(ChooseCameraActivity.this,
//								R.string.conset_success);
						finish();
					}					
				}else {
					if (dbhlp.insert("HelperParam", "iID, ParamName, ParamValue",
							new String[] {"1", ChooseCameraActivity.this.getString(R.string.choose_camera), "0" }) > 0) {
//						comm.showMsg(ChooseCameraActivity.this,
//								R.string.conset_success);
						finish();
						
					}					
				}
			}
			case R.id.btnChooseCancel: {
				ChooseCameraActivity.this.finish();
				break;
			}
			}
			
		}

    }
	
}

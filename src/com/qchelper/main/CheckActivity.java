package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import com.example.qchelper.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CheckActivity extends Activity {
	final static String DEBUG_TAG = "CheckActivity";
	
	public final static int REQUEST_CODE_IMAGE= 1;//图片显示  
	public final static int REQUEST_CODE_RESULT= 2;//检验结果
	
    int MstID;
    Intent intent;
    Bundle bundle;
    
    TextView txtOrderNo;
    TextView txtStyleNo;
    
    ListView CheckList;
    CheckAdapter checkAdapter;
    
    int[] ItemKeyList;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        
        txtOrderNo = (TextView) findViewById(R.id.txtCheckOrderNo);
        txtStyleNo = (TextView) findViewById(R.id.txtCheckStyleNo);
        
        intent = this.getIntent();
        bundle = intent.getExtras();
        MstID = bundle.getInt("KeyID");
        
        txtOrderNo.setText("订单号:" + bundle.getString("OrderNo"));
        txtStyleNo.setText("款号:" + bundle.getString("StyleNo"));
        
        CheckList = (ListView) findViewById(R.id.check_list);
        checkAdapter = new CheckAdapter(this, R.layout.qccheckitem, getCheckData());
        CheckList.setAdapter(checkAdapter);

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_check, menu);
        return true;
    }
    */
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        checkAdapter = new CheckAdapter(this, R.layout.qccheckitem, getCheckData());
        CheckList.setAdapter(checkAdapter);
        super.onActivityResult(requestCode, resultCode, data);
    }    
    
    private List<String> getCheckData() {
        List<String> data = new ArrayList<String>();  
        String[] WhereParam = new String[1];
        WhereParam[0]=String.valueOf(MstID);
        dbHelper dbhlp = new dbHelper(this);
        Cursor cursor = dbhlp.querySQL("select a.iID, a.iItemID, a.dChecdedDate "
                + " from qmCheckRecordMst a "
                + " left join qmCheckPlan b on a.iFactoryID=b.iFactoryID and a.sProductID=b.sProductID "
                + " where b.iID=? "
                + " order by a.iItemID asc ", WhereParam);

        if (cursor.getCount() > 0) {
        	ItemKeyList = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
            	ItemKeyList[cursor.getPosition()] = cursor.getInt(0);
                data.add(cursor.getString(0)
                        + "※" + cursor.getString(1)
                        + "※" + cursor.getString(2)
                        );
            }
        }
        dbhlp.close();
        cursor.close();
        return data;
    }
    
    class CheckAdapter extends ArrayAdapter<String> {
        public CheckAdapter(Context context,  int textViewResourceId, List<String> objects) {
            super(context,  textViewResourceId, objects);
        }
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            View view = convertView;
            Log.d(DEBUG_TAG, "getView:" + Integer.toString(position));
            view = LayoutInflater.from(getContext()).inflate(R.layout.qccheckitem, null);
            //显示名称
            TextView tv_iItemID = (TextView) view.findViewById(R.id.CheckItemID); 
            TextView tv_iCheckState = (TextView) view.findViewById(R.id.CheckState);           
            Button btn_CheckPic = (Button) view.findViewById(R.id.CheckPic);
            Button btn_CheckResult = (Button) view.findViewById(R.id.CheckResult);
            btn_CheckPic.setTag(position);
            btn_CheckResult.setTag(position);
            btn_CheckPic.setOnClickListener(new ItemButtonClickEvent());
            btn_CheckResult.setOnClickListener(new ItemButtonClickEvent());
            /*
            btn_CheckPic.setOnClickListener(new OnClickListener(){  
            	
            	public void onClick(View v) {
            		int FPosition = Integer.parseInt(v.getTag().toString());
            		Toast.makeText(CheckActivity.this, v.getTag().toString(), 1500).show();
            	}});
            */
            
            String[] strarray = getItem(position).split("※");            
            if (strarray.length == 3) {
            	tv_iItemID.setText("第" + strarray[1] + "道检验");
            	Log.d(DEBUG_TAG, "getView:" + Integer.toString(strarray[2].length()));
            	if (strarray[2] == null || strarray[2].equals("") || strarray[2].equals("null") || strarray[2].length() <= 0) {
                	tv_iCheckState.setText("-"); 
                	Log.d(DEBUG_TAG, "getView_1");
            	}
            	else {
                	tv_iCheckState.setText("√");
                	Log.d(DEBUG_TAG, "getView_2");
            	}
            	Log.d(DEBUG_TAG, strarray[2]);
            	
            }
            else if (strarray.length == 2) {
            	tv_iItemID.setText("第" + strarray[1] + "道检验");
            	tv_iCheckState.setText("-");
            }
            
            //返回重写的view
            return view;
        }       

    }
    
    class ItemButtonClickEvent implements View.OnClickListener {
	
    	public void onClick(View v) {
    		int FPosition = Integer.parseInt(v.getTag().toString());
    		if (FPosition >= 0) {
      		    switch (v.getId()) {
      		    case R.id.CheckPic: {
      		    	Intent pIntent = new Intent(CheckActivity.this, PicActivity.class);
      		    	pIntent.putExtra("KeyID", ItemKeyList[FPosition]);
      		    	startActivityForResult(pIntent, REQUEST_CODE_IMAGE);  
      		    	
      			    break;  			  
      		    }
      		    case R.id.CheckResult: {
      		    	Intent rIntent = new Intent(CheckActivity.this, RemarkActivity.class);
      		    	rIntent.putExtra("KeyID", ItemKeyList[FPosition]);
      		    	startActivityForResult(rIntent, REQUEST_CODE_RESULT);

      			    break;
      		    }
      		    }   			
    		}  		  
  	  }
     }     
     
}

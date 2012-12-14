package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qchelper.comm.comm;
import com.qchelper.comm.dbHelper;

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
    
    String[] ItemKeyList;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        
        //获取登录信息
        SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_READABLE);
        String login_user_id = setting.getString("user_id", "");
        if (login_user_id == "") {
            comm.showErrorMsg(this, R.string.main_need_login);
            finish();
        }        
        
        txtOrderNo = (TextView) findViewById(R.id.txtCheckOrderNo);
        txtStyleNo = (TextView) findViewById(R.id.txtCheckStyleNo);
        
        intent = this.getIntent();
        bundle = intent.getExtras();
        MstID = bundle.getInt("KeyID");
        
        txtOrderNo.setText("订单号:" + bundle.getString("OrderNo"));
        txtStyleNo.setText("款号:" + bundle.getString("StyleNo"));

        InitqmCheckRecordMstData(12, bundle.getString("OrderNo"), bundle.getString("StyleNo"), bundle.getString("ProductID"), login_user_id);
        
        CheckList = (ListView) findViewById(R.id.check_list);
        checkAdapter = new CheckAdapter(this, R.layout.qccheckitem, getCheckData());
        CheckList.setAdapter(checkAdapter);
    }
    
    public void InitqmCheckRecordMstData(int FactoryID, String OrderNo, String StyleNo, String ProductID, String login_user_id) {
        Log.d(DEBUG_TAG, "insert into qmCheckRecordMst");
        dbHelper dbhlp = new dbHelper(this);
        Cursor curCheckMst = dbhlp.querySQL("select uID from qmCheckRecordMst "
                + " where iFactoryID = ? and sOrderNo = ? and sStyleNo = ? and sProductID = ?"
                , new String[] {Integer.toString(FactoryID), OrderNo, StyleNo, ProductID});
        if (curCheckMst.getCount() <= 0) {
        	Cursor curItem = dbhlp.select("qmCheckItem");
        	while (curItem.moveToNext()) {
        		dbhlp.insert("qmCheckRecordMst", "iFactoryID,sOrderNo,sStyleNo,sProductID,iItemID,sUserID", new String[] {Integer.toString(FactoryID), OrderNo, StyleNo, ProductID, curItem.getString(curItem.getColumnIndex("iID")), login_user_id});        		
        	}
            curItem.close();
        }
        curCheckMst.close();
        dbhlp.close();
    }
    
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
        Cursor cursor = dbhlp.querySQL("select a.uID, a.iItemID, c.sItemName, a.dCheckedDate "
                + " from qmCheckRecordMst a "
                + " inner join qmCheckPlan b on a.iFactoryID=b.iFactoryID and a.sProductID=b.sProductID and a.sOrderNo = b.sOrderNo and a.sStyleNo = b.sStyleNo "
                + " inner join qmCheckItem c on c.iID = a.iItemID "
                + " where b.iID = ? "
                + " order by a.iItemID asc ", WhereParam);

        if (cursor.getCount() > 0) {
        	ItemKeyList = new String[cursor.getCount()];
            while (cursor.moveToNext()) {
            	ItemKeyList[cursor.getPosition()] = cursor.getString(0);
                data.add(cursor.getString(0)
                        + "※" + cursor.getString(1)
                        + "※" + cursor.getString(2)
                        + "※" + cursor.getString(3)
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
            if (strarray.length == 4) {
            	tv_iItemID.setText(strarray[1] + "." + strarray[2]);
            	Log.d(DEBUG_TAG, "getView:" + Integer.toString(strarray[2].length()));
            	if (strarray[3] == null || strarray[3].equals("") || strarray[3].equals("null") || strarray[3].length() <= 0) {
                	tv_iCheckState.setText("-"); 
                	Log.d(DEBUG_TAG, "getView_1");
            	}
            	else {
                	tv_iCheckState.setText("√");
                	Log.d(DEBUG_TAG, "getView_2");
            	}
            	Log.d(DEBUG_TAG, strarray[2]);
            	
            }
            // 好像没有长度等于3的数据集？
//            else if (strarray.length == 3) {
//            	tv_iItemID.setText("第" + strarray[1] + "道检验");
//            	tv_iCheckState.setText("-");
//            }
            
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

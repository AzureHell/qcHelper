package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.qchelper.R;
import com.qchelper.main.CheckActivity;
import com.qchelper.main.ConsetActivity;
import com.qchelper.main.dbHelper;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	final static String DEBUG_TAG = "MainActivity";
	
    private static final int LOGIN_ID = Menu.FIRST;
    private static final int CONSETTING_ID = Menu.FIRST + 1;  
    private static final int EXIT_ID = Menu.FIRST + 2;  	
	
	ListView PlanList;
	PlanAdapter planAdapter;
	Button MainSearch;
	EditText edtSearch;
	
	int[] ItemKeyList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "MainActivity_CREATE");
        setContentView(R.layout.activity_main);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        this.setTitle(R.string.title_activity_main);
        InitInsertData();
        
        MainSearch = (Button) findViewById(R.id.MainSearch);
        MainSearch.setOnClickListener(new ButtonClickEvent());
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        edtSearch.setHint(R.string.Search_Hint);
        
        PlanList = (ListView) findViewById(R.id.Plan_list);        
        planAdapter = new PlanAdapter(this, R.layout.qcplanitem, getPlanData(null));
        Log.d(DEBUG_TAG, "MainActivity_CREATE_1");
        PlanList.setAdapter(planAdapter);
        PlanList.setOnItemClickListener(new ItemClickEvent());
    }

    public void InitInsertData() {
    	Log.d(DEBUG_TAG, "InitInsertData");
    	dbHelper dbhlp = new dbHelper(this);
    	Cursor cursor = dbhlp.querySQL("select iID from qmCheckPlan ");
    	Log.d(DEBUG_TAG, "InitInsertData1:" + Integer.toString(cursor.getCount()));
    	if (cursor.getCount() <= 0) {
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
    				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {1, 12, "SC010", "QX7886", "P0000001", "2012-11-22", "1、面料、辅料品质优良，符合客户要求; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {2, 12, "SC011", "TX7001", "P0000002", "2012-11-28", "1、面料、辅料品质优良，符合客户要求; 2、水洗色牢度; 3、包装美观、配比正确.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {3, 18, "SC011", "YW0006", "P0000003", "2012-11-30", "1、对色准确，大货布的颜色和确认色的色差至少应在3.5级之内，并需经客户确认; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {4, 12, "SC012", "QX1111", "P0000004", "2012-12-08", "1、产品干净、整洁、卖相好; 2、款式配色准确无误; 3、包装美观、配比正确; 4、面料、辅料品质优良，符合客户要求.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {5, 20, "SC012", "UK1256", "P0000005", "2012-12-12", "1、产品干净、整洁、卖相好; 2、款式配色准确无误; 3、包装美观、配比正确; 4、水洗色牢度; 5、面料、辅料品质优良，符合客户要求.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {6, 22, "SC022", "LP6589", "P0000006", "2012-12-22", "1、面料、辅料品质优良，符合客户要求; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});    		
    	}
    	
    	cursor = dbhlp.querySQL("select iID from qmCheckRecordMst ");
    	Log.d(DEBUG_TAG, "InitInsertData2:" + Integer.toString(cursor.getCount()));
    	if (cursor.getCount() <= 0) {
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
    				  + "values(?,?,?,?,?,?)", new Object[] {1, 12, "SC010", "QX7886", "P0000001", 1});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
  				  + "values(?,?,?,?,?,?)", new Object[] {2, 12, "SC010", "QX7886", "P0000001", 2});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
  				  + "values(?,?,?,?,?,?)", new Object[] {3, 12, "SC010", "QX7886", "P0000001", 3}); 
    		
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
  				  + "values(?,?,?,?,?,?)", new Object[] {4, 12, "SC011", "TX7001", "P0000002", 1});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
				  + "values(?,?,?,?,?,?)", new Object[] {5, 12, "SC011", "TX7001", "P0000002", 2});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
				  + "values(?,?,?,?,?,?)", new Object[] {6, 12, "SC011", "TX7001", "P0000002", 3});    		
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
    			  + "values(?,?,?,?,?,?)", new Object[] {7, 12, "SC011", "TX7001", "P0000002", 4});    		
 
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
      			  + "values(?,?,?,?,?,?)", new Object[] {8, 18, "SC011", "YW0006", "P0000003", 1});     		
    		
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
    			  + "values(?,?,?,?,?,?)", new Object[] {9, 12, "SC012", "QX1111", "P0000004", 1}); 
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
  				  + "values(?,?,?,?,?,?)", new Object[] {10, 12, "SC012", "QX1111", "P0000004", 2});      		
    		
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
    			  + "values(?,?,?,?,?,?)", new Object[] {11, 20, "SC012", "UK1256", "P0000005", 1});		
    		
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
    			  + "values(?,?,?,?,?,?)", new Object[] {12, 22, "SC022", "LP6589", "P0000006", 1});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
  				  + "values(?,?,?,?,?,?)", new Object[] {13, 22, "SC022", "LP6589", "P0000006", 2});       		
    	}
    	cursor.close();
    	dbhlp.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, LOGIN_ID, 0, R.string.menu_login).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, CONSETTING_ID, 0, R.string.menu_setting).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, EXIT_ID, 0, R.string.menu_exit).setIcon(android.R.drawable.ic_menu_info_details);           
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      switch (item.getItemId()) {
        case LOGIN_ID: {
        	Intent intent = new Intent(this, LoginActivity.class);
        	startActivity(intent);        	
        	break;
        }
        case CONSETTING_ID: {        	
        	Intent intent = new Intent(this, ConsetActivity.class);
        	startActivity(intent);        	
        	break;
        }
        case EXIT_ID: {
        	finish();
        	break;
        }
      }	
      return super.onOptionsItemSelected(item);
    }    
    
    private List<String> getPlanData(String ParamValue) {
        Log.d(DEBUG_TAG, "getPlanList_1");
        List<String> data = new ArrayList<String>();
        
        String SqlText;
        

//        String[] WhereParam = new String[1];
//        WhereParam[0]=OrderNo;
        
        dbHelper dbhlp = new dbHelper(this);
//        Cursor cursor = dbhlp.querySQL(SqlText);
//        Cursor cursor;
        if ((ParamValue == null) || (ParamValue == "")) {
        	SqlText = "select iID, dRequestCheck, sOrderNo, sStyleNo "
                + ", sProductID, sCheckItemDesc "
                + " from qmCheckPlan "
                + " order by dRequestCheck asc "
                + " limit 20 ";
        	Log.d(DEBUG_TAG, "getPlanList_2_1");
        }
        else {
        	SqlText = "select iID, dRequestCheck, sOrderNo, sStyleNo "
                + ", sProductID, sCheckItemDesc "
                + " from qmCheckPlan "
                + " where (sOrderNo like '%" + ParamValue + "%') or (sStyleNo like '%" + ParamValue + "%') "
                + " order by dRequestCheck asc "
                + " limit 20 ";
        	Log.d(DEBUG_TAG, "getPlanList_2_2:" + ParamValue);
        }
        Log.d(DEBUG_TAG, "getPlanList_3");
        Cursor cursor = dbhlp.querySQL(SqlText);
        Log.d(DEBUG_TAG, "getPlanList_4");

        if (cursor.getCount() > 0) {
        	ItemKeyList = new int[cursor.getCount()];
        	Log.d(DEBUG_TAG, "getPlanList_5");
            while (cursor.moveToNext()) {
            	Log.d(DEBUG_TAG, "getPlanList_6");
            	ItemKeyList[cursor.getPosition()] = cursor.getInt(0);
            	Log.d(DEBUG_TAG, "getPlanList_7");
                data.add(cursor.getString(1)
                        + "※" + cursor.getString(2)
                        + "※" + cursor.getString(3)
                        + "※" + cursor.getString(4)
                        + "※" + cursor.getString(5)
                        );
            }
            
            if (1 == 1) { //((ParamValue != null) && (ParamValue != ""))
            	Toast.makeText(this, "找到" + Integer.toString(cursor.getCount()) + "条记录", 1500).show();            	
            }
            
        }
        else {
        	if (1 == 1) { //((ParamValue != null) && (ParamValue != ""))
        		Toast.makeText(this, "没有找到记录", 1500).show();        		
        	}
        	
        }
        
        dbhlp.close();
        cursor.close();
        return data;
    }
    
    class PlanAdapter extends ArrayAdapter<String> {
        public PlanAdapter(Context context,  int textViewResourceId, List<String> objects) {
            super(context,  textViewResourceId, objects);
        }
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            View view = convertView;  
            view = LayoutInflater.from(getContext()).inflate(R.layout.qcplanitem, null);
            //显示名称  
            TextView tv_dRequestCheck = (TextView) view.findViewById(R.id.PlanCheckDate);
            TextView tv_sOrderNo = (TextView) view.findViewById(R.id.PlanOrderNo);
            TextView tv_sStyleNo = (TextView) view.findViewById(R.id.PlanStyleNo);
            TextView tv_sProductID = (TextView) view.findViewById(R.id.PlanProductID);
            TextView tv_sCheckItemDesc = (TextView) view.findViewById(R.id.PlanCheckDesc);
            
            String[] strarray = getItem(position).split("※");
            if (strarray.length == 5) {
            	tv_dRequestCheck.setText(strarray[0]);
            	tv_sOrderNo.setText(strarray[1]);
            	tv_sStyleNo.setText(strarray[2]);
            	tv_sProductID.setText(strarray[3]);
            	tv_sCheckItemDesc.setText(strarray[4]);
            }
            //返回重写的view
            return view;
        }
    }
    
    class ItemClickEvent implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
    		Intent intent = new Intent(MainActivity.this, CheckActivity.class);   		
            TextView tv_sOrderNo = (TextView) arg1.findViewById(R.id.PlanOrderNo);
            TextView tv_sStyleNo = (TextView) arg1.findViewById(R.id.PlanStyleNo);

    		intent.putExtra("KeyID", ItemKeyList[arg2]);
    		intent.putExtra("OrderNo", tv_sOrderNo.getText());
    		intent.putExtra("StyleNo", tv_sStyleNo.getText());	
    		startActivity(intent);
    		
    	}    	
    }
    
    class ButtonClickEvent implements View.OnClickListener {
    	
    	public void onClick(View v) {
    		switch(v.getId()){
    		case R.id.MainSearch: {
    			String FParamValue;
    			if (edtSearch.getText().toString() == "" || edtSearch.getText().length() <= 0) {
    				FParamValue=null;
    			}
    			else {
    				FParamValue = edtSearch.getText().toString();    				
    			}
    	        planAdapter = new PlanAdapter(MainActivity.this, R.layout.qcplanitem, getPlanData(FParamValue));
    	        PlanList.setAdapter(planAdapter);    			
    			break;    			
    		}
    		}
    	}
    }
    
}

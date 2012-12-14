package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qchelper.comm.comm;
import com.qchelper.comm.dbHelper;

public class MainActivity extends Activity {
	final static String DEBUG_TAG = "MainActivity";
	
    private static final int LOGIN_ID = Menu.FIRST;
    private static final int CONSETTING_ID = Menu.FIRST + 1;  
    private static final int EXIT_ID = Menu.FIRST + 2;  	
    
    final int SYNC_ACTIVITY_REQUESTCODE = 1;
    final int LOGIN_ACTIVITY_REQUESTCODE = 2;    

    private boolean DialogReturnType;    
    
	ListView PlanList;
	PlanAdapter planAdapter;
	Button MainSearch, btnSync;
	EditText edtSearch;
	
	int[] ItemKeyList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        this.setTitle(R.string.title_activity_main);
        // 保留，用于初始化检验项目
        InitInsertData();
        
        MainSearch = (Button) findViewById(R.id.MainSearch);
        MainSearch.setOnClickListener(new ButtonClickEvent());
        btnSync = (Button) findViewById(R.id.main_sync);
        btnSync.setOnClickListener(new ButtonClickEvent());        
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        edtSearch.setHint(R.string.Search_Hint);
        
        refreshList();
    }

    public void refreshList() {
        PlanList = (ListView) findViewById(R.id.Plan_list);
        planAdapter = new PlanAdapter(this, R.layout.qcplanitem, getPlanData(null));
        PlanList.setAdapter(planAdapter);
        PlanList.setOnItemClickListener(new ItemClickEvent());
    }
    
    public void InitInsertData() {
    	Log.d(DEBUG_TAG, "InitInsertData");
    	dbHelper dbhlp = new dbHelper(this);
//    	Cursor cursor = dbhlp.querySQL("select iID from qmCheckPlan");
//    	Log.d(DEBUG_TAG, "InitInsertData1:" + Integer.toString(cursor.getCount()));
    	
    	Cursor cursor = dbhlp.select("qmCheckItem");
    	if (cursor.getCount() <= 0) {
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"辅料检测报告"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"产前会议记录"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"首件样报告"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"中期查货报告"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"尾期查货报告"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"包装检验流程标准"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"干湿度测试报告"});
	    	dbhlp.insert("qmCheckItem", "sItemName", new String[]{"生产进度表"});
    	}
    	
//    	if (cursor.getCount() <= 0) {
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//    				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {1, 12, "SC010", "QX7886", "P0000001", "2012-11-22", "1、面料、辅料品质优良，符合客户要求; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {2, 12, "SC011", "TX7001", "P0000002", "2012-11-28", "1、面料、辅料品质优良，符合客户要求; 2、水洗色牢度; 3、包装美观、配比正确.", "U001", "U001"});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {3, 18, "SC011", "YW0006", "P0000003", "2012-11-30", "1、对色准确，大货布的颜色和确认色的色差至少应在3.5级之内，并需经客户确认; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {4, 12, "SC012", "QX1111", "P0000004", "2012-12-08", "1、产品干净、整洁、卖相好; 2、款式配色准确无误; 3、包装美观、配比正确; 4、面料、辅料品质优良，符合客户要求.", "U001", "U001"});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {5, 20, "SC012", "UK1256", "P0000005", "2012-12-12", "1、产品干净、整洁、卖相好; 2、款式配色准确无误; 3、包装美观、配比正确; 4、水洗色牢度; 5、面料、辅料品质优良，符合客户要求.", "U001", "U001"});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
//  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {6, 22, "SC022", "LP6589", "P0000006", "2012-12-22", "1、面料、辅料品质优良，符合客户要求; 2、款式配色准确无误; 3、包装美观、配比正确.", "U001", "U001"});    		
//    	}
//    	
//    	cursor = dbhlp.querySQL("select uID from qmCheckRecordMst ");
//    	Log.d(DEBUG_TAG, "InitInsertData2:" + Integer.toString(cursor.getCount()));
//    	if (cursor.getCount() <= 0) {
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(uID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//    				  + "values(?,?,?,?,?,?)", new Object[] {1, 12, "SC010", "QX7886", "P0000001", 1});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(uID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//  				  + "values(?,?,?,?,?,?)", new Object[] {2, 12, "SC010", "QX7886", "P0000001", 2});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(uID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//  				  + "values(?,?,?,?,?,?)", new Object[] {3, 12, "SC010", "QX7886", "P0000001", 3}); 
//    		
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//  				  + "values(?,?,?,?,?,?)", new Object[] {4, 12, "SC011", "TX7001", "P0000002", 1});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//				  + "values(?,?,?,?,?,?)", new Object[] {5, 12, "SC011", "TX7001", "P0000002", 2});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//				  + "values(?,?,?,?,?,?)", new Object[] {6, 12, "SC011", "TX7001", "P0000002", 3});    		
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//    			  + "values(?,?,?,?,?,?)", new Object[] {7, 12, "SC011", "TX7001", "P0000002", 4});    		
// 
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//      			  + "values(?,?,?,?,?,?)", new Object[] {8, 18, "SC011", "YW0006", "P0000003", 1});     		
//    		
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//    			  + "values(?,?,?,?,?,?)", new Object[] {9, 12, "SC012", "QX1111", "P0000004", 1}); 
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//  				  + "values(?,?,?,?,?,?)", new Object[] {10, 12, "SC012", "QX1111", "P0000004", 2});      		
//    		
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//    			  + "values(?,?,?,?,?,?)", new Object[] {11, 20, "SC012", "UK1256", "P0000005", 1});		
//    		
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//    			  + "values(?,?,?,?,?,?)", new Object[] {12, 22, "SC022", "LP6589", "P0000006", 1});
//    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckRecordMst(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, iItemID) "
//  				  + "values(?,?,?,?,?,?)", new Object[] {13, 22, "SC022", "LP6589", "P0000006", 2});       		
//    	}
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
    public boolean onPrepareOptionsMenu(Menu menu) {   
        // Set 'delete' menu item state depending on count   
        MenuItem loginItem = menu.findItem(LOGIN_ID);   
        // 登录验证，如果未进行登录则弹出登录窗口
        SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_READABLE);
        String login_user_id = setting.getString("user_id", "");
        if (login_user_id == "") {
            loginItem.setTitle(R.string.menu_login);
        } else {
            loginItem.setTitle(R.string.menu_logout);
        }
        return super.onPrepareOptionsMenu(menu);   
    }      
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case LOGIN_ID: {
                Log.d(DEBUG_TAG, "click login");
                if (!comm.isNetworkAvailable(this)) {
                    comm.showMsg(this, R.string.network_inactive);
                    break;
                }
                Resources res = getResources();
                if (item.getTitle().toString() == res.getString(R.string.menu_login)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_ACTIVITY_REQUESTCODE);
                } else if (item.getTitle().toString() == res.getString(R.string.menu_logout)) {
                    showDialog(0);
                }
            	break;
            }
            case CONSETTING_ID: {        
                Resources res = getResources();
                if (item.getTitle().toString() == res.getString(R.string.menu_logout)) {
                    AskDialog();
                    if (DialogReturnType == false) {
                        break;              
                    }               
                }
                
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
        
        String selection = null;
        String[] selectionArgs = null;
        dbHelper dbhlp = new dbHelper(this);
        if ((ParamValue != null) && (ParamValue != "")) {
        	selection = "sOrderNo like '%?%' or sStyleNo like '%?%'";
        	selectionArgs = new String[] {ParamValue, ParamValue};
        }
        Cursor cursor = dbhlp.select("qmCheckPlan", "iID,dRequestCheck,sOrderNo,sStyleNo,sProductID,sCheckItemDesc,iFactoryID,sQCUserID", selection, selectionArgs, 20);

        if (cursor.getCount() > 0) {
        	ItemKeyList = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
            	ItemKeyList[cursor.getPosition()] = cursor.getInt(0);
                data.add(cursor.getString(1)
                        + "※" + cursor.getString(2)
                        + "※" + cursor.getString(3)
                        + "※" + cursor.getString(4)
                        + "※" + cursor.getString(5)
                        );
            }
            comm.showMsg(this, String.format(getResources().getString(R.string.have_records), cursor.getCount()));
        }
        else {
        	comm.showMsg(this, R.string.not_record);
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
            TextView tv_sProductID = (TextView) arg1.findViewById(R.id.PlanProductID);
            
            intent.putExtra("KeyID", ItemKeyList[arg2]);
            intent.putExtra("OrderNo", tv_sOrderNo.getText());
            intent.putExtra("StyleNo", tv_sStyleNo.getText());
            intent.putExtra("ProductID", tv_sProductID.getText());
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
                case R.id.main_sync: {
                    Log.d(DEBUG_TAG, "click sync");
                    if (!comm.isNetworkAvailable(MainActivity.this)) {
                        comm.showMsg(MainActivity.this, R.string.network_inactive);
                        break;
                    }
                    // 登录验证，如果未进行登录则弹出登录窗口
                    SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_READABLE);
                    String logined_user_id = setting.getString("user_id", "");
                    if (logined_user_id == "") {
                        comm.showMsg(MainActivity.this, R.string.main_need_login);
                        break;
                    }

                    if (logined_user_id != "") {
                        new syncAsyncTask().execute(logined_user_id);
                    } else {
                        comm.showMsg(MainActivity.this, R.string.main_not_data_need_sync);
                    }
                    
                    break;
                }        		
    		}
    	}
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {// 注销  
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(comm.getResourceString(this, R.string.main_sure_logout))
                    .setCancelable(false)
                    .setPositiveButton(comm.getResourceString(this, R.string.ok),  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  int id) {  
                                    SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_WRITEABLE);
                                    setting.edit().clear().commit();
                                }
                            }).setNegativeButton(comm.getResourceString(this, R.string.cancel), null);  
            AlertDialog alert = builder.create();  
            return alert;
        }
        return null;
    }    
    
    public void AskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.dialog_askinfo);
        builder.setTitle(R.string.dialog_titile);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences setting = getSharedPreferences("Login",Context.MODE_WORLD_WRITEABLE);
                setting.edit().clear().commit();
//                btnLogin.setText(R.string.btn_login);
                DialogReturnType = true;
                dialog.dismiss();
            }
        });
        
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
           DialogReturnType = false;            
           dialog.dismiss();
         }
        });
        builder.create().show();
     }    
    
    public class syncAsyncTask extends AsyncTask<String, Integer, Integer> {
        final static String DEBUG_TAG = "syncAsyncTask";
        private ProgressDialog progressDialog;
        private int syncMaxCount = 0;
        private int syncCurrentCount = 0;

        @Override
        protected void onPreExecute() {
            Log.d(DEBUG_TAG, "onPreExecute");
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle(comm.getResourceString(MainActivity.this, R.string.process_wait));
            progressDialog.show();
        }        
        
        protected Integer doInBackground(String... strJson) {
            Log.d(DEBUG_TAG, "doInBackground");
            String user_id = "";
            syncMaxCount = strJson.length;
            
            for (int i = 0; i < strJson.length; i++) {
                user_id = strJson[i];
                Log.d(DEBUG_TAG, "begin user_id: " + user_id);
                syncCurrentCount = i + 1;
                String resultHttp = "";
                dbHelper dbhlp = new dbHelper(MainActivity.this);
                
                // 下载QC计划
                Log.d(DEBUG_TAG, "begin download");
                int maxID = 1;
                
                Cursor cursor = dbhlp.querySQL("select max(iID) as iID from qmCheckPlan");
                while (cursor.moveToNext()) {
                    Log.d(DEBUG_TAG, "download qmCheckPlan");
                    if (cursor.getString(cursor.getColumnIndex("iID")) != null) {
                        maxID = cursor.getInt(cursor.getColumnIndex("iID"));
                    } else {
                    	maxID = 0;
                    }
                    
                    String params = comm.fmtHttpParams("{'sQCUserID':'"+strJson[i]+"','iID':'"+Integer.toString(maxID)+"'}");
                    try {
                        resultHttp = comm.invokeHttp(MainActivity.this, "downloadCheckPlan", params);
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, e.toString());
                    }
                }
                cursor.close();
                if (resultHttp != null && !resultHttp.equals("")) {
                    try {
                         JSONObject json = new JSONObject(resultHttp);
                         //如何返回成功
                         if (json.getString("status").equals("succeed")) {
                             //处理返回表列表
                             JSONArray jsonTableArray = json.getJSONArray("data");
                             for (int j=0; j < jsonTableArray.length(); j++) {
                                 //获取返回表
                                 JSONObject jsonTable = jsonTableArray.getJSONObject(j);
                                 if (jsonTable.getString("table").equals("qmCheckPlan")) {
                                     //获取返回记录列表
                                     JSONArray jsonRecordArray = jsonTable.getJSONArray("records");
                                     for (int k=0;  k < jsonRecordArray.length(); k++) {
                                         //获取返回记录
                                         JSONObject jsonRecordData = jsonRecordArray.getJSONObject(k);
                                         dbhlp.insert("qmCheckPlan", "iID,iFactoryID,sOrderNo,sStyleNo,sProductID,dRequestCheck,sCheckItemDesc,sQCUserID," +
                                         		"sUserID,bApproved", new String[]{
                                        		 jsonRecordData.getString("iID"), 
                                        		 jsonRecordData.getString("iFactoryID"),
                                        		 jsonRecordData.getString("sOrderNo"), 
                                        		 jsonRecordData.getString("sStyleNo"), 
                                        		 jsonRecordData.getString("sProductID"), 
                                        		 jsonRecordData.getString("dRequestCheck"), 
                                        		 jsonRecordData.getString("sCheckItemDesc"), 
                                        		 jsonRecordData.getString("sQCUserID"), 
                                        		 jsonRecordData.getString("sUserID"), 
                                        		 jsonRecordData.getString("bApproved"), 
                                        		 });
                                     }
                                 }
                             }
                             //建峰，你要看到代码处理下这里，同步后刷新会报错！
//                             refreshList();
                         } else if (json.getString("status").equals("failed")) {
//                         	if (json.getString("error").equals("not record!")) {
//                        		comm.showErrorMsg(MainActivity.this, R.string.main_not_data_need_sync);
//                        	}
//                        	else comm.showErrorMsg(MainActivity.this, json.getString("error"));
//                        	 return 2;
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                	return 1;
                }
                resultHttp = "";
                //上传QC结果
                Log.d(DEBUG_TAG, " begin upload");
                String jsonMst = "", jsonDtl = "";
                int recordDtlCount = 0;
                
//                Cursor csCheckRecordMat = dbhlp.querySQL("select uID, iFactoryID, sOrderNo, sStyleNo, sProductID"
//                        + " , iItemID, dChecdedDate, sRemark, sUserID, datetime_rec, datetime_modify, datetime_delete "
//                        + " from qmCheckRecordMst a where datetime_modify > coalesce(datetime_upload, 0)");
                Cursor csCheckRecordMat = dbhlp.select("qmCheckRecordMst", "uID, iFactoryID, sOrderNo, sStyleNo, sProductID" 
                		+ ", iItemID, dCheckedDate, sRemark, sUserID, datetime_rec, datetime_modify, datetime_delete, datetime_upload"
                		, "dCheckedDate is not null and datetime_modify > coalesce(datetime_upload, date('1900-01-01'))", null);
                while (csCheckRecordMat.moveToNext()) {
                    Log.d(DEBUG_TAG, "Build uploadCheckRecordMaster Json rec:");
                    jsonMst = "{'uMobileKey':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("uID"))
                            +"','iFactoryID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iFactoryID"))
                            +"','sOrderNo':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sOrderNo"))
                            +"','sStyleNo':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sStyleNo"))
                            +"','sProductID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sProductID"))
                            +"','iItemID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iItemID"))
                            +"','dCheckedDate':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("dCheckedDate"))
                            +"','sRemark':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sRemark"))
                            +"','sUserID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sUserID"))
                            +"','datetime_rec':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_rec"))
                            +"','datetime_modify':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_modify"))
                            +"','datetime_delete':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_delete"))
                            +"'}";
                    
                    jsonDtl = "";
                    recordDtlCount = 0;
                    
                    Cursor csCheckRecordDtl = dbhlp.querySQL("select b.uID, b.uMstID, b.dCreateDate, "
                            + " b.datetime_rec, b.datetime_modify, b.datetime_delete "
                            + " from qmCheckRecordMst a inner join qmCheckRecordDtl b on b.uMstID = a.uID "
                            + " where a.uID = ? and b.datetime_modify > coalesce(a.datetime_upload, date('1900-01-01'))"
                    		, new String[]{csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("uID"))});
//                    Cursor csCheckRecordDtl = dbhlp.select("qmCheckRecordDtl", "uID, uMstID, sPhoto, dCreateDate, "
//                            + " datetime_rec, datetime_modify, datetime_delete"
//                    		, "uMstID = ? and datetime_modify > coalesce(?, 0)" 
//                    		, new String[]{csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("uID"))
//                    		, csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_upload"))});
                    while (csCheckRecordDtl.moveToNext()) {
                        Log.d(DEBUG_TAG, "Build uploadCheckRecordDetail Json rec:" + recordDtlCount);
                        String str = "{'uMobileKey':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("uID"))
                                +"','dCreateDate':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("dCreateDate"))
                                +"','datetime_rec':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("datetime_rec"))
                                +"','datetime_modify':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("datetime_modify"))
                                +"','datetime_delete':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("datetime_delete"))
                                +"'}";
                        if (jsonDtl.equals("")) {
                        	jsonDtl = str;
                        } else {
                        	jsonDtl += "," + str;
                        }
                        recordDtlCount += 1;
                    }
                    csCheckRecordDtl.close();
                    if (jsonMst != "") {
                        jsonMst = "{'table':'qmCheckRecordMst', 'count':'1', 'records':["+jsonMst+"]}";
                    
                        if (jsonDtl != "") {
                            jsonDtl = ",{'table':'qmCheckRecordDtl', 'count':'"+Integer.toString(recordDtlCount)+"', 'records':["+jsonDtl+"]}";
                        } else {jsonDtl = "";}
                    
                        String params = comm.fmtHttpParams(jsonMst+jsonDtl);
                        try {
                            resultHttp = comm.invokeHttp(MainActivity.this, "uploadCheckRecord", params);
                        } catch (Exception e) {
                            Log.e(DEBUG_TAG, e.toString());
                        }
//                        处理返回结果
                        if (resultHttp != null && !resultHttp.equals("")) {
                            try {
                                JSONObject json = new JSONObject(resultHttp);
                                //如何返回成功
                                if (json.getString("status").equals("succeed")) {
                                    JSONArray jsonTableArray = json.getJSONArray("data");
                                    for (int j=0; j < jsonTableArray.length(); j++) {
                                        JSONObject jsonTable = jsonTableArray.getJSONObject(j);
                                        if (jsonTable.getString("table").equals("qmCheckRecordMst")) {
                                            JSONArray jsonRecordArray = jsonTable.getJSONArray("records");
                                            for (int k=0; k < jsonRecordArray.length(); k++) {
                                                JSONObject jsonRecordData = jsonRecordArray.getJSONObject(k);
                                                
                                                //上传QC图片
                                                Log.d(DEBUG_TAG, "begin upload Picture");
                                                recordDtlCount = 0;
                                                String resultHttpPic = "";
                                                Cursor cursor1 = dbhlp.querySQL("select b.uID, b.sPhoto "
                                                        + " from qmCheckRecordMst a inner join qmCheckRecordDtl b on b.uMstID = a.uID "
                                                        + " where a.uID = ? and b.datetime_modify > coalesce(a.datetime_upload, date('1900-01-01'))"
                                                		, new String[]{jsonRecordData.getString("uMobileKey")});
//                                                Cursor cursor1 = dbhlp.select("qmCheckRecordDtl", "uID,sPhoto", "uMstID = ?", new String[]{jsonRecordData.getString("uMobileKey")});
                                                while (cursor1.moveToNext()) {
                                                    Log.d(DEBUG_TAG, "Build uploadCheckRecordDetail Pic rec:" + recordDtlCount++);
                                                    try {
                                                        resultHttpPic = comm.invokeHttp(MainActivity.this, "uploadCheckRecordPic"
                                                                //组织文件名称，这样可以找到需要插入图片的那条记录
                                                                , cursor1.getString(cursor1.getColumnIndex("uID"))
                                                                , cursor1.getBlob(cursor1.getColumnIndex("sPhoto"))
                                                                );
                                                    } catch (Exception e) {
                                                        Log.e(DEBUG_TAG, e.toString());
                                                    }
                                                }
                                                if (resultHttpPic != "") {
                                                    JSONObject jsonPic = new JSONObject(resultHttpPic);
                                                    //如何返回成功
                                                    if (jsonPic.getString("status").equals("succeed")) {
                                                        Log.d(DEBUG_TAG, " finished upload Picture");
                                                    }
                                                }
                                                dbhlp.updateSyncDatetime("qmCheckRecordMst", jsonRecordData.getString("uMobileKey"), jsonRecordData.getString("datetime_upload"));
                                                cursor1.close();
                                            }
                                        }
                                    }
                                } else if (json.getString("status").equals("failed")) {
//                                 	if (json.getString("error").equals("not record!")) {
//                                		return 2;
//                                	}
//                                	else return 3;     
//                                	return 2;
                                 }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                        	return 1;
                        }
                    }
                }
                resultHttp = "";
                Log.d(DEBUG_TAG, " finished upload");
                //更新进度
                publishProgress(syncCurrentCount);
                csCheckRecordMat.close();
                dbhlp.close();
            }
            return 0;
        }
        
        protected void onProgressUpdate(Integer progress) {
            Log.d(DEBUG_TAG, "onProgressUpdate");
            progressDialog.setMessage(comm.getResourceString(MainActivity.this, R.string.processing) + " " 
                    + Integer.toString(progress) + "/" + Integer.toString(syncMaxCount));
        }

        protected void onPostExecute(Integer result) {
            Log.d(DEBUG_TAG, "onPostExecute");
            progressDialog.cancel();
            if (result == 1) {
            	comm.showErrorMsg(MainActivity.this, R.string.sync_failed);
            } else if (result != -1) {
            	comm.showMsg(MainActivity.this, R.string.main_sync_completed);
            } else if (result == 2) {
            	comm.showErrorMsg(MainActivity.this, R.string.main_not_data_need_sync);
            }
        }
    }    
}

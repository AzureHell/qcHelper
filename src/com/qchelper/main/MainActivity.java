package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.qchelper.comm.comm;
import com.qchelper.comm.httpHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends Activity {
	final static String DEBUG_TAG = "MainActivity";
	
    private static final int LOGIN_ID = Menu.FIRST;
    private static final int CONSETTING_ID = Menu.FIRST + 1;  
    private static final int EXIT_ID = Menu.FIRST + 2;  	
	
	ListView PlanList;
	PlanAdapter planAdapter;
	Button MainSearch, btnSync;
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
    				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {1, 12, "SC010", "QX7886", "P0000001", "2012-11-22", "1�����ϡ�����Ʒ�����������Ͽͻ�Ҫ��; 2����ʽ��ɫ׼ȷ����; 3����װ���ۡ������ȷ.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {2, 12, "SC011", "TX7001", "P0000002", "2012-11-28", "1�����ϡ�����Ʒ�����������Ͽͻ�Ҫ��; 2��ˮϴɫ�ζ�; 3����װ���ۡ������ȷ.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {3, 18, "SC011", "YW0006", "P0000003", "2012-11-30", "1����ɫ׼ȷ�����������ɫ��ȷ��ɫ��ɫ������Ӧ��3.5��֮�ڣ����辭�ͻ�ȷ��; 2����ʽ��ɫ׼ȷ����; 3����װ���ۡ������ȷ.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {4, 12, "SC012", "QX1111", "P0000004", "2012-12-08", "1����Ʒ�ɾ������ࡢ�����; 2����ʽ��ɫ׼ȷ����; 3����װ���ۡ������ȷ; 4�����ϡ�����Ʒ�����������Ͽͻ�Ҫ��.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {5, 20, "SC012", "UK1256", "P0000005", "2012-12-12", "1����Ʒ�ɾ������ࡢ�����; 2����ʽ��ɫ׼ȷ����; 3����װ���ۡ������ȷ; 4��ˮϴɫ�ζ�; 5�����ϡ�����Ʒ�����������Ͽͻ�Ҫ��.", "U001", "U001"});
    		dbhlp.getWritableDatabase().execSQL("insert into qmCheckPlan(iID, iFactoryID, sOrderNo, sStyleNo, sProductID, dRequestCheck, sCheckItemDesc, sQCUserID, sUserID) "
  				  + "values(?,?,?,?,?,?,?,?,?)", new Object[] {6, 22, "SC022", "LP6589", "P0000006", "2012-12-22", "1�����ϡ�����Ʒ�����������Ͽͻ�Ҫ��; 2����ʽ��ɫ׼ȷ����; 3����װ���ۡ������ȷ.", "U001", "U001"});    		
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
            	
                Log.d(DEBUG_TAG, "click login");
                if (!comm.isNetworkAvailable(this)) {
                    comm.showMsg(this, R.string.network_inactive);
                    break;
                }
                Resources res = getResources();
//                if (btnLogin.getText().toString() == res.getString(R.string.btn_login)) {
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    startActivityForResult(intent, LOGIN_ACTIVITY_REQUESTCODE);
//                } else if (btnLogin.getText().toString() == res.getString(R.string.btn_logout)) {
//                    showDialog(0);
//                }
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
                        + "��" + cursor.getString(2)
                        + "��" + cursor.getString(3)
                        + "��" + cursor.getString(4)
                        + "��" + cursor.getString(5)
                        );
            }
            
            if (1 == 1) { //((ParamValue != null) && (ParamValue != ""))
            	Toast.makeText(this, "�ҵ�" + Integer.toString(cursor.getCount()) + "����¼", 1500).show();            	
            }
            
        }
        else {
        	if (1 == 1) { //((ParamValue != null) && (ParamValue != ""))
        		Toast.makeText(this, "û���ҵ���¼", 1500).show();        		
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
            //��ʾ����  
            TextView tv_dRequestCheck = (TextView) view.findViewById(R.id.PlanCheckDate);
            TextView tv_sOrderNo = (TextView) view.findViewById(R.id.PlanOrderNo);
            TextView tv_sStyleNo = (TextView) view.findViewById(R.id.PlanStyleNo);
            TextView tv_sProductID = (TextView) view.findViewById(R.id.PlanProductID);
            TextView tv_sCheckItemDesc = (TextView) view.findViewById(R.id.PlanCheckDesc);
            
            String[] strarray = getItem(position).split("��");
            if (strarray.length == 5) {
            	tv_dRequestCheck.setText(strarray[0]);
            	tv_sOrderNo.setText(strarray[1]);
            	tv_sStyleNo.setText(strarray[2]);
            	tv_sProductID.setText(strarray[3]);
            	tv_sCheckItemDesc.setText(strarray[4]);
            }
            //������д��view
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
                case R.id.main_sync: {
                    Log.d(DEBUG_TAG, "click sync");
                    if (!comm.isNetworkAvailable(MainActivity.this)) {
                        comm.showMsg(MainActivity.this, R.string.network_inactive);
                        break;
                    }
                    // ��¼��֤�����δ���е�¼�򵯳���¼����
                    SharedPreferences setting = getSharedPreferences("HummingbirdLogin",Context.MODE_WORLD_READABLE);
                    String login_user_id = setting.getString("user_id", "");
                    if (login_user_id == "") {
                        comm.showMsg(MainActivity.this, R.string.main_need_login);
                        break;
                    }
                    
                    try {
                        dbHelper dbhlp = new dbHelper(MainActivity.this);
                        Cursor cursor = dbhlp.querySQL("select id, factory_no, case when worksection_no is null then '' else worksection_no end as worksection_no "
                                + ", bill_no, order_no, style_no, product_no "
                                + ", color_no, color_name, size_no, size_name, quantity "
                                + ", datetime_opt, datetime_rec, datetime_delete "
                                + " from process_rec where datetime_upload is null ");
                        String[] strJson = new String[cursor.getCount()];
                        int i = 0;
                        while (cursor.moveToNext()) {
                            Log.d(DEBUG_TAG, "Build sync Json rec:" + i);
                            strJson[i] = new JSONStringer().object()
                                    .key("user_id_opt").value(login_user_id)
                                    .key("id").value(cursor.getString(cursor.getColumnIndex("id")))
                                    .key("factory_no").value(cursor.getString(cursor.getColumnIndex("factory_no")))
                                    .key("worksection_no").value(cursor.getString(cursor.getColumnIndex("worksection_no")))
                                    .key("bill_no").value(cursor.getString(cursor.getColumnIndex("bill_no")))
                                    .key("order_no").value(cursor.getString(cursor.getColumnIndex("order_no")))
                                    .key("style_no").value(cursor.getString(cursor.getColumnIndex("style_no")))
                                    .key("product_no").value(cursor.getString(cursor.getColumnIndex("product_no")))
                                    .key("color_no").value(cursor.getString(cursor.getColumnIndex("color_no")))
                                    .key("color_name").value(cursor.getString(cursor.getColumnIndex("color_name")))
                                    .key("size_no").value(cursor.getString(cursor.getColumnIndex("size_no")))
                                    .key("size_name").value(cursor.getString(cursor.getColumnIndex("size_name")))
                                    .key("quantity").value(cursor.getString(cursor.getColumnIndex("quantity")))
                                    .key("datetime_opt").value(cursor.getString(cursor.getColumnIndex("datetime_opt")))
                                    .key("datetime_rec").value(cursor.getString(cursor.getColumnIndex("datetime_rec")))
                                    .key("datetime_delete").value(cursor.getString(cursor.getColumnIndex("datetime_delete")))
                                    .endObject().toString();
                            Log.d(DEBUG_TAG, "sync Json: " + strJson[i]);
                            i++;
                        }
                        if (strJson.length > 0) {
                            new syncAsyncTask().execute(strJson);
                        } else {
                            comm.showMsg(MainActivity.this, R.string.main_not_data_need_sync);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }        		
    		}
    	}
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
            syncMaxCount = strJson.length;
            
            /* LJF */
            String SERVER_URL = "";         
            dbHelper dbhlp = new dbHelper(MainActivity.this);
            Cursor dbcur = dbhlp.querySQL("select id, server_ip, server_port "
                    + " from server_con "); 
            if (dbcur.getCount() > 0) {
                dbcur.moveToFirst();
                if ((dbcur.getString(dbcur.getColumnIndex("server_ip")).length() > 0) && (dbcur.getString(dbcur.getColumnIndex("server_port")).length() > 0)) {
                    SERVER_URL = "http://" + dbcur.getString(dbcur.getColumnIndex("server_ip")) 
                      + ":" + dbcur.getString(dbcur.getColumnIndex("server_port")) + "/sync";                   
                }

            }     
            
            Log.d(DEBUG_TAG, "doInBackground_2");
            if (SERVER_URL == ""){
                return null;            
            } 
            
            for (int i = 0; i < strJson.length; i++) {
                syncCurrentCount = i + 1;
                
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("data", strJson[i]));
                String result = null;
                try {
                    result = httpHelper.invoke(SERVER_URL, params);
//                    result = httpHelper.invoke("sync", params); 
                } catch (Exception e) {
                    Log.e(DEBUG_TAG, e.toString());
                }
                try {
                    JSONObject json = new JSONObject(result);
                    //dbHelper dbhlp = new dbHelper(HummingbirdActivity.this);
                    dbhlp.updateSyncDatetime("process_rec", json.getInt("id"), json.getString("user_id_opt"), json.getString("datetime_upload"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(syncCurrentCount);
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
            comm.showMsg(MainActivity.this, R.string.main_sync_completed);
        }
    }    
}

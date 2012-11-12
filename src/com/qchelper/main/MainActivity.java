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
        Log.d(DEBUG_TAG, "MainActivity_CREATE");
        setContentView(R.layout.activity_main);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        this.setTitle(R.string.title_activity_main);
//        InitInsertData();
        
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
    public boolean onPrepareOptionsMenu(Menu menu) {   
        // Set 'delete' menu item state depending on count   
        MenuItem loginItem = menu.findItem(LOGIN_ID);   
        // ��¼��֤�����δ���е�¼�򵯳���¼����
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
        
        String SqlText;
 
        dbHelper dbhlp = new dbHelper(this);
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
                    // ��¼��֤�����δ���е�¼�򵯳���¼����
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
    
    public Boolean download(String user_id) {
        try {
            dbHelper dbhlp = new dbHelper(MainActivity.this);
            Cursor cursor = dbhlp.querySQL("select max(iID) as iID from qmCheckRecordMst");
            String[] strJson = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Log.d(DEBUG_TAG, "Build sync Json rec:" + i);
                strJson[i] = new JSONStringer().object()
                        .key("sQCUserID").value(user_id)
                        .key("iID").value(cursor.getString(cursor.getColumnIndex("iID")))
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
        return true;
    }
    
    public Boolean upload(String user_id) {
        try {
            dbHelper dbhlp = new dbHelper(MainActivity.this);
            Cursor cursor = dbhlp.querySQL("select iID, iFactoryID, sOrderNo, sStyleNo, sProductID"
                    + "iItemID, dChecdedDate, sRemark, datetime_rec, datetime_delete, user_id_opt"
                    + " from qmCheckRecordMst where datetime_upload is null");
            String[] strJson = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                Log.d(DEBUG_TAG, "Build sync Json rec:" + i);
                strJson[i] = new JSONStringer().object()
                        .key("user_id_opt").value(user_id)
                        .key("iID").value(cursor.getString(cursor.getColumnIndex("iID")))
                        .key("iFactoryID").value(cursor.getString(cursor.getColumnIndex("iFactoryID")))
                        .key("sOrderNo").value(cursor.getString(cursor.getColumnIndex("sOrderNo")))
                        .key("sStyleNo").value(cursor.getString(cursor.getColumnIndex("sStyleNo")))
                        .key("sProductID").value(cursor.getString(cursor.getColumnIndex("sProductID")))
                        .key("iItemID").value(cursor.getString(cursor.getColumnIndex("iItemID")))
                        .key("dChecdedDate").value(cursor.getString(cursor.getColumnIndex("dChecdedDate")))
                        .key("sRemark").value(cursor.getString(cursor.getColumnIndex("sRemark")))
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
        return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {// ע��  
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
//                user_id = strJson[i];
                syncCurrentCount = i + 1;
                String resultHttp = "";
                dbHelper dbhlp = new dbHelper(MainActivity.this);
                
                // ����QC�ƻ�
                Log.d(DEBUG_TAG, "begin download");
                int maxID = 1;
                Cursor cursor = dbhlp.querySQL("select max(iID) as iID from qmCheckPlan");
                while (cursor.moveToNext()) {
                    Log.d(DEBUG_TAG, "download qmCheckPlan");
                    if (cursor.getString(cursor.getColumnIndex("iID")) != null) {
                        maxID = cursor.getInt(cursor.getColumnIndex("iID"));
                    }
                    
                    String params = comm.fmtHttpParams("{'sQCUserID':'"+strJson[i]+"','iID':'"+Integer.toString(maxID)+"'}");
                    try {
                        resultHttp = comm.invokeHttp(MainActivity.this, "downloadCheckPlan", params);
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, e.toString());
                    }
                }
                cursor.close();
                if (resultHttp != "") {
                    try {
                         JSONObject json = new JSONObject(resultHttp);
                         //��η��سɹ�
                         if (json.getString("status").equals("succeed")) {
                             //�����ر��б�
                             JSONArray jsonTableArray = json.getJSONArray("data");
                             for (int j=0; j < jsonTableArray.length(); j++) {
                                 //��ȡ���ر�
                                 JSONObject jsonTable = jsonTableArray.getJSONObject(j);
                                 if (jsonTable.getString("table").equals("qmCheckPlan")) {
                                     //��ȡ���ؼ�¼�б�
                                     JSONArray jsonRecordArray = jsonTable.getJSONArray("records");
                                     for (int k=0;  k < jsonRecordArray.length(); k++) {
                                         //��ȡ���ؼ�¼
                                         JSONObject jsonRecordData = jsonRecordArray.getJSONObject(k);
                                         ContentValues cv = new ContentValues();
                                         String str = "iID"; cv.put(str, jsonRecordData.getString(str));
                                         str = "iFactoryID"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sOrderNo"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sStyleNo"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sProductID"; cv.put(str, jsonRecordData.getString(str));
                                         str = "dRequestCheck"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sCheckItemDesc"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sCheckItemDesc"; cv.put(str, jsonRecordData.getString(str));
                                         str = "sUserID"; cv.put(str, jsonRecordData.getString(str));
                                         dbhlp.insert("qmCheckPlan", cv);
                                     }
                                 }
                             }
//                             refreshList();
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }   
                }
                resultHttp = "";
                //�ϴ�QC���
                Log.d(DEBUG_TAG, " begin upload");
                String jsonMst = "", jsonDtl = "";
                int recordDtlCount = 0;
                
                //��ʱ�ֶ�����
                user_id = "1";
                
                Cursor csCheckRecordMat = dbhlp.querySQL("select iID, iFactoryID, sOrderNo, sStyleNo, sProductID"
                        + " , iItemID, dChecdedDate, sRemark, datetime_rec, datetime_delete, user_id_opt "
                        + " from qmCheckRecordMst a where datetime_upload is null and  exists (select 1 from qmCheckRecordDtl where iMstID = a.iID limit 1  ) ");
                while (csCheckRecordMat.moveToNext()) {
                    Log.d(DEBUG_TAG, "Build uploadCheckRecordMaster Json rec:");
                    jsonMst = "{'iID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iID"))
                            +"','iFactoryID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iFactoryID"))
                            +"','sOrderNo':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sOrderNo"))
                            +"','sStyleNo':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sStyleNo"))
                            +"','sProductID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sProductID"))
                            +"','iItemID':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iItemID"))
                            +"','dChecdedDate':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("dChecdedDate"))
                            +"','sRemark':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("sRemark"))
                            +"','datetime_rec':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_rec"))
                            +"','datetime_delete':'"+csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("datetime_delete"))
                            +"','user_id_opt':'" + user_id 
                            +"'}";
                    
                    jsonDtl = "";
                    recordDtlCount = 0;
                    
                    Cursor csCheckRecordDtl = dbhlp.querySQL("select iID, iMstID, sPhoto, dCreaimestamp, "
                            + " datetime_rec, datetime_delete "
                            + " from qmCheckRecordDtl where iMstID = " + csCheckRecordMat.getString(csCheckRecordMat.getColumnIndex("iID")));
                    while (csCheckRecordDtl.moveToNext()) {
                        Log.d(DEBUG_TAG, "Build uploadCheckRecordDetail Json rec:" + recordDtlCount);
                        String str = "{'user_id_opt':'"+user_id
                                +"','iID':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("iID"))
                                +"','iMstID':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("iMstID"))
                                //��֯��Ϊ�ļ�����
                                +"','sPhoto':'" + "user_id_opt(" + user_id
                                + ")iMstID(" + csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("iMstID"))
                                + ")iID(" + csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("iID")) + ").png"
//                                +"','sPhoto':'"+Base64.encodeToString(csCheckRecordDtl.getBlob(csCheckRecordDtl.getColumnIndex("sPhoto")), Base64.DEFAULT)
                                +"','dCreaimestamp':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("dCreaimestamp"))
                                +"','datetime_rec':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("datetime_rec"))
                                +"','datetime_delete':'"+csCheckRecordDtl.getString(csCheckRecordDtl.getColumnIndex("datetime_delete"))
                                +"'}";
                        if (jsonDtl != "") {
                            jsonDtl = "," + str;
                        } else {
                            jsonDtl = str;
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
//                        �����ؽ��
                        if (resultHttp != "") {
                            try {
                                JSONObject json = new JSONObject(resultHttp);
                                //��η��سɹ�
                                if (json.getString("status").equals("succeed")) {
                                    JSONArray jsonTableArray = json.getJSONArray("data");
                                    for (int j=0; j < jsonTableArray.length(); j++) {
                                        JSONObject jsonTable = jsonTableArray.getJSONObject(j);
                                        if (jsonTable.getString("table").equals("qmCheckRecordMst")) {
                                            JSONArray jsonRecordArray = jsonTable.getJSONArray("records");
                                            for (int k=0; k < jsonRecordArray.length(); k++) {
                                                JSONObject jsonRecordData = jsonRecordArray.getJSONObject(k);
                                                
                                                //�ϴ�QCͼƬ
                                                Log.d(DEBUG_TAG, "begin upload Picture");
                                                recordDtlCount = 0;
                                                Cursor cursor1 = dbhlp.querySQL("select iID, iMstID, sPhoto "
                                                        + " from qmCheckRecordDtl where iMstID = " + jsonRecordData.getString("iID"));
                                                while (cursor1.moveToNext()) {
                                                    Log.d(DEBUG_TAG, "Build uploadCheckRecordDetail Pic rec:" + recordDtlCount++);
                                                    try {
                                                        resultHttp = comm.invokeHttp(MainActivity.this, "uploadCheckRecordPic"
                                                                //��֯�ļ����ƣ����������ҵ���Ҫ����ͼƬ��������¼
                                                                , "user_id_opt(" + user_id 
                                                                + ")iMstID(" + cursor1.getString(cursor1.getColumnIndex("iMstID"))
                                                                + ")iID(" + cursor1.getString(cursor1.getColumnIndex("iID")) + ").png"
                                                                , cursor1.getBlob(cursor1.getColumnIndex("sPhoto"))
                                                                );
                                                    } catch (Exception e) {
                                                        Log.e(DEBUG_TAG, e.toString());
                                                    }
                                                }
                                                Log.d(DEBUG_TAG, resultHttp);
                                                Log.d(DEBUG_TAG, " finished upload Picture");
//                                                dbhlp.updateSyncDatetime("qmCheckRecordMst", jsonRecordData.getInt("iID")
//                                                        , jsonRecordData.getString("user_id_opt"),  jsonRecordData.getString("datetime_upload"));
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                resultHttp = "";
                Log.d(DEBUG_TAG, " finished upload");
                //���½���
                publishProgress(syncCurrentCount);
                csCheckRecordMat.close();
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

package com.example.qchelper;

import   java.text.SimpleDateFormat;     

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class dbHelper extends SQLiteOpenHelper {
    final static String DEBUG_TAG = "dbQC";
    
    private final static String DATABASE_NAME="QCHelper";
    private final static int DATABASE_VERSION=1;
    
    public dbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * iID ���
         * iFactoryID ����
         * sOrderNo ������
         * sStyleNo ��ʽ��
         * sProductID ����
         * dRequestCheck ��������
         * sCheckItemDesc ��������
         * sQCUserID QC���
         * sUserID �û����
         */
        String sql_create ="Create table qmCheckPlan(iID integer PRIMARY KEY "
                + ", iFactoryID integer, sOrderNo nvarchar(50), sStyleNo nvarchar(50), sProductID nvarchar(50) "
                + ", dRequestCheck timestamp, sCheckItemDesc nvarchar(500), sQCUserID nvarchar(50), sUserID nvarchar(50));";
        db.execSQL(sql_create);
        Log.d(DEBUG_TAG, "CREATE_1");
        /*
         * iID ��� 
         * iFactoryID ����
         * sOrderNo ������
         * sStyleNo ��ʽ��
         * sProductID ����
         * iItemID ������Ŀ
         * dChecdedDate ��������
         * sRemark �������
         * datetime_opt ����ʱ�䣬�Զ�����
         * datetime_rec ��¼ʱ�䣬���޸�
         * datetime_upload �ϴ�ʱ��
         * datetime_delete ɾ��ʱ��
         */
        
        sql_create ="Create table qmCheckRecordMst(iID integer PRIMARY KEY "
                + ", iFactoryID integer, sOrderNo nvarchar(50), sStyleNo nvarchar(50), sProductID nvarchar(50) "
                + ", iItemID integer, dChecdedDate timestamp, sRemark nvarchar(500) "                
                + ", datetime_rec timestamp default (datetime('now','localtime')), datetime_opt timestamp "
                + ", datetime_upload timestamp, datetime_delete timestamp, user_id_opt integer);";
        db.execSQL(sql_create); 
        Log.d(DEBUG_TAG, "CREATE_2");
        
        /*
         * iID ������
         * iMstID ���ṹID
         * sPhoto ͼƬ
         * dCreateDate ����ʱ��
         * datetime_opt ����ʱ�䣬�Զ�����
         * datetime_rec ��¼ʱ�䣬���޸�
         * datetime_upload �ϴ�ʱ��
         * datetime_delete ɾ��ʱ��
         */   
        
        sql_create =" Create table qmCheckRecordDtl(iID integer primary key autoincrement "
    	+ ", iMstID integer, sPhoto bolb, dCreaimestamp default (datetime('now','localtime')) "
        + ", datetime_rec timestamp default (datetime('now','localtime')), datetime_opt timestamp "
        + ", datetime_upload timestamp, datetime_delete timestamp, user_id_opt integer );";
        db.execSQL(sql_create);        
        Log.d(DEBUG_TAG, "CREATE_3");
        
        /*
         * iID ������
         * server_ip �������
         * server_port ���α��
         */   
        
        sql_create =" Create table ServerCon(iID integer primary key autoincrement "
    	+ ", server_ip varchar(40), server_port varchar(20));";
        db.execSQL(sql_create);
        Log.d(DEBUG_TAG, "CREATE_4");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_check=" DROP TABLE IF EXISTS qmCheckPlan";
        db.execSQL(sql_check);
        Log.d(DEBUG_TAG, "DROP_1");
        
        sql_check=" DROP TABLE IF EXISTS qmCheckRecordMst";
        db.execSQL(sql_check);
        Log.d(DEBUG_TAG, "DROP_2");
        
        sql_check=" DROP TABLE IF EXISTS qmCheckRecordDtl";
        db.execSQL(sql_check);
        Log.d(DEBUG_TAG, "DROP_3");
        
        sql_check=" DROP TABLE IF EXISTS ServerCon";
        db.execSQL(sql_check);
        Log.d(DEBUG_TAG, "DROP_4");
        onCreate(db);
    }
    
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
    
    public Cursor querySQL(String sql) {
        Log.d(DEBUG_TAG, "querySQL");
        return querySQL(sql, null);
    }
    
    public Cursor querySQL(String sql, String[] selectionArgs) {
        Log.d(DEBUG_TAG, "querySQL");
        return getReadableDatabase().rawQuery(sql, selectionArgs);
    }
    
    public Cursor select(String table_name) {
        return select(table_name, null, null, "100");
    }
    
    public Cursor select(String table_name, String selection, String[] selectionArgs) {
        return select(table_name, selection, selectionArgs);
    }
    
    public Cursor select(String table_name, String limit) {
        return select(table_name, null, null, limit);
    }
    
    public Cursor select(String table_name, String selection, String[] selectionArgs, String limit) {
        Log.d(DEBUG_TAG, "select");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (table_name == "qmCheckPlan") {
            cursor = db.query(table_name, null, selection, selectionArgs, null, null, " dRequestCheck asc", limit);
        }
        else if (table_name == "qmCheckRecordMst") {
        	cursor = db.query(table_name, null, selection, selectionArgs, null, null, "iItemID asc", limit);
        }
        else if (table_name == "qmCheckRecordDtl") {
            cursor = db.query(table_name, null, selection, selectionArgs, null, null, "dCreateDate asc", limit);        
        }        
        else if (table_name == "ServerCon") {
        	cursor = db.query(table_name, null, selection, selectionArgs, null, null,  null, limit);        	
        }
        return cursor;
    }    
    
    public long insert(String table_name, String values) {
        Log.d(DEBUG_TAG, "insert");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        String[] strarray=values.split(",");
        Log.d(DEBUG_TAG, "insert_2:" + table_name);
        if (table_name == "ServerCon") {
        	Log.d(DEBUG_TAG, "in(ServerCon)_1");
        	if (strarray.length == 2) {
        		Log.d(DEBUG_TAG, "in(ServerCon)_2");
        		cv.put("server_ip", strarray[0]);
        		cv.put("server_port", strarray[1]);
        	}
        	
        }        
        Log.d(DEBUG_TAG, "insert_3");
        long row=db.insert(table_name, null, cv);
        return row;
    }
    
    public long insert(String table_name, int mstid, byte[] pic ) {
        Log.d(DEBUG_TAG, "insert");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        Log.d(DEBUG_TAG, "insert_2:" + table_name);
        if (table_name == "qmCheckRecordDtl") {
        	Log.d(DEBUG_TAG, "in(qmCheckRecordDtl)_1");
        	cv.put("iMstID", mstid);
        	cv.put("sPhoto", pic);
        }        
        Log.d(DEBUG_TAG, "insert_3");
        long row=db.insert(table_name, null, cv);
        return row;
    }    
    
    public void delete(String table_name, int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        db.delete(table_name, where, whereValue);
    }
      
    public int update(String table_name, int id, String values) {
        Log.d(DEBUG_TAG, "update");
        SQLiteDatabase db=this.getWritableDatabase();
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues(); 
        String[] strarray=values.split(","); 
        if (table_name == "qmCheckRecordMst") {
            if (strarray.length == 1) {
                cv.put("sRemark", strarray[0]);
            }
        }
        else if (table_name == "ServerCon") {
        	if (strarray.length == 2) {
        	cv.put("server_ip", strarray[0]);	
        	cv.put("server_port", strarray[1]);
        	}
        	
        }        
        return db.update(table_name, cv, where, whereValue);
    }
    
    public int updateRemark(String table_name, int id, String remark) {
        Log.d(DEBUG_TAG, "updateRemark");
        SQLiteDatabase db=this.getWritableDatabase();
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String date = sDateFormat.format(new java.util.Date());       
        if (table_name == "qmCheckRecordMst") {
        	cv.put("sRemark", remark);
        	cv.put("dChecdedDate", date);
        }       
        return db.update(table_name, cv, where, whereValue);
    }    
    
    public int UpdateOptDatetime(String table_name, int id, String user_id) {
        SQLiteDatabase db=this.getWritableDatabase();
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String date = sDateFormat.format(new java.util.Date()); 
        if (table_name == "qmCheckRecordMst") {
            cv.put("user_id_opt", user_id);
            cv.put("dChecdedDate", date);
        }

        return db.update(table_name, cv, where, whereValue);    	
    }
    
    public int updateSyncDatetime(String table_name, int id, String user_id, String datetime_upload) {
        Log.d(DEBUG_TAG, "updateSyncDatetime");
        SQLiteDatabase db=this.getWritableDatabase();
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        ContentValues cv=new ContentValues(); 
        if (table_name == "qmCheckRecordMst") {
            cv.put("user_id_opt", user_id);
            cv.put("datetime_upload", datetime_upload);
        }
        else if (table_name == "qmCheckRecordDtl") {
            cv.put("user_id_opt", user_id);
            cv.put("datetime_upload", datetime_upload);        	
        }
        return db.update(table_name, cv, where, whereValue);
    }
}
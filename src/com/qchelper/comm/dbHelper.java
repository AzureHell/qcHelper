package com.qchelper.comm;

import java.text.SimpleDateFormat;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {
    final static String DEBUG_TAG = "dbHelper";
    
    private final static String DATABASE_NAME="QCHelper";
    private final static int DATABASE_VERSION=1;
    
    public dbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * iID 编号
         * iFactoryID 工厂
         * sOrderNo 订单号
         * sStyleNo 款式号
         * sProductID 产编
         * dRequestCheck 检验日期
         * sCheckItemDesc 检验内容
         * sQCUserID QC编号
         * sUserID 用户编号
         * bApproved 已审核
         */
        String sql_create ="Create table qmCheckPlan(iID integer PRIMARY KEY "
                + ", iFactoryID integer, sOrderNo nvarchar(50), sStyleNo nvarchar(50), sProductID nvarchar(50) "
                + ", dRequestCheck timestamp, sCheckItemDesc nvarchar(500), sQCUserID nvarchar(50), sUserID nvarchar(50) "
                + ", bApproved bit default 0);";
        db.execSQL(sql_create);
        Log.d(DEBUG_TAG, "CREATE_1");
        /*
         * iID 编号 
         * iFactoryID 工厂
         * sOrderNo 订单号
         * sStyleNo 款式号
         * sProductID 产编
         * iItemID 检验项目
         * dChecdedDate 检验日期
         * sRemark 检验结论
         * datetime_rec 记录时间
         * datetime_upload 上传时间
         * datetime_delete 删除时间
         */
        
        sql_create ="Create table qmCheckRecordMst(uID varchar(32) primary key  "
                + ", iFactoryID integer, sOrderNo nvarchar(50), sStyleNo nvarchar(50), sProductID nvarchar(50) "
                + ", iItemID integer, dCheckedDate timestamp, sRemark nvarchar(500), sUserID varchar(50) "                
                + ", datetime_rec timestamp default (datetime('now','localtime'))"
                + ", datetime_modify timestamp default (datetime('now','localtime')) "
                + ", datetime_upload timestamp, datetime_delete timestamp);";
        db.execSQL(sql_create);
        Log.d(DEBUG_TAG, "CREATE_2");
        
        /*
         * uID 自增列
         * uMstID 父结构ID
         * sPhoto 图片
         * dCreateDate 创建时间
         * datetime_rec 记录时间
         * datetime_upload 上传时间
         * datetime_delete 删除时间
         */   
        
        sql_create =" Create table qmCheckRecordDtl(uID varchar(32) primary key "
    	+ ", uMstID varchar(32), sPhoto bolb, dCreateDate default (datetime('now','localtime')) "
        + ", datetime_rec timestamp default (datetime('now','localtime')) "
    	+ ", datetime_modify timestamp default (datetime('now','localtime')) "
        + ", datetime_delete timestamp );";
        db.execSQL(sql_create);
        Log.d(DEBUG_TAG, "CREATE_3");
        
        /*
         * iID 自增列
         * server_ip 工厂编号
         * server_port 工段编号
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
        //return super.getReadableDatabase();
        return super.getWritableDatabase();
    }

    public void execSQL(String sql) {
        Log.d(DEBUG_TAG, "querySQL");
        execSQL(sql, null);
    }
    
    public void execSQL(String sql, Object[] bindArgs) {
        Log.d(DEBUG_TAG, "querySQL");
        getWritableDatabase().execSQL(sql, bindArgs);
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
        return select(table_name, null, null, null, 20);
    }

    public Cursor select(String table_name, String columns, Integer limit) {
        return select(table_name, columns, null, null, limit);
    }
    
    public Cursor select(String table_name, String columns, String selection, String[] selectionArgs) {
        return select(table_name, columns, selection, selectionArgs, 100);
    }
    
    /* columns需要查询的字段名如：id,name,remark
     * selection需要查询的条件，如：id = ? and name like '%?%'
     * selectionArgs查询条件的参数：20, hell
     * limit为查询结果行数，如：20
     */    
    public Cursor select(String table_name, String columns, String selection, String[] selectionArgs, Integer limit) {
        Log.d(DEBUG_TAG, "select");
        
    	String[] columnarray = null;
    	if (columns != null && !columns.equals("")) {
    		columnarray = columns.split(",");
    	}
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (table_name == "qmCheckPlan") {
            cursor = db.query(table_name, columnarray, selection, selectionArgs, null, null, " dRequestCheck asc", Integer.toString(limit));
        }
        else if (table_name == "qmCheckRecordMst") {
        	cursor = db.query(table_name, columnarray, selection, selectionArgs, null, null, "iItemID asc", Integer.toString(limit));
        }
        else if (table_name == "qmCheckRecordDtl") {
            cursor = db.query(table_name, columnarray, selection, selectionArgs, null, null, "dCreateDate asc", Integer.toString(limit));        
        }        
        else if (table_name == "ServerCon") {
        	cursor = db.query(table_name, columnarray, selection, selectionArgs, null, null,  null, Integer.toString(limit));        	
        }
        return cursor;
    }

    
    public long insert(String table_name, String fields, String[] fieldvalues) {
        Log.d(DEBUG_TAG, "insert to " + table_name);
        ContentValues cv = new ContentValues();
        String[] fieldarray = fields.split(",");
		for (int i=0; i < fieldarray.length; i++) {
			cv.put(fieldarray[i], fieldvalues[i]);
		};
        if (table_name.equals("ServerCon")) {
    		Log.d(DEBUG_TAG, "in(ServerCon)_2");
    		cv.put("server_ip", fieldvalues[0]);
    		cv.put("server_port", fieldvalues[1]);
        } else if (table_name.equals("qmCheckPlan")) {
            cv.put("iID", fieldvalues[0]);
            cv.put("iFactoryID", fieldvalues[1]);
            cv.put("sOrderNo", fieldvalues[2]);
            cv.put("sStyleNo", fieldvalues[3]);
            cv.put("sProductID", fieldvalues[4]);
            cv.put("dRequestCheck", fieldvalues[5]);
            cv.put("sCheckItemDesc", fieldvalues[6]);
            cv.put("sQCUserID", fieldvalues[7]);
            cv.put("sUserID", fieldvalues[8]);
        } else if (table_name == "qmCheckRecordMst") {
        	cv.put("uID", UUID.randomUUID().toString());
        }
        long row = insert(table_name, null, cv);
        return row;
    }
    
    public long insertCheckRecordDtl(String table_name, String mstid, byte[] pic ) {
        Log.d(DEBUG_TAG, "insertCheckRecordDtl");
        ContentValues cv=new ContentValues();
    	cv.put("uID", UUID.randomUUID().toString());
    	cv.put("uMstID", mstid);
    	cv.put("sPhoto", pic);
        long row = insert(table_name, null, cv);
        return row;
    }
    
    public long insert(String table_name, String nullColumnHack, ContentValues cv) {
        Log.d(DEBUG_TAG, "insert");
        SQLiteDatabase db = this.getWritableDatabase();
        long row = db.insert(table_name, nullColumnHack, cv);
        return row;
    }
    
    public void delete(String table_name, int id)
    {
        String where="iID=?";
        String[] whereValue={Integer.toString(id)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, where, whereValue);
    }
    
    public int updateRemark(String table_name, String id, String remark) {
        Log.d(DEBUG_TAG, "updateRemark");
        return update(table_name, id, "sRemark", new String[]{remark});
    }
    
    public int updateOptDatetime(String table_name, String id) {
        Log.d(DEBUG_TAG, "UpdateOptDatetime");
        return update(table_name, id, "dCheckedDate", new String[]{comm.getNowStringDateTime()});
    }
    
    public int updateSyncDatetime(String table_name, String id, String datetime_upload) {
        Log.d(DEBUG_TAG, "updateSyncDatetime");
        return update(table_name, id, "datetime_upload", new String[]{datetime_upload});
    }
    
    public int updateToDelete(String table_name, String id) {
        Log.d(DEBUG_TAG, "updateToDelete");
        return update(table_name, id, "datetime_delete", new String[]{comm.getNowStringDateTime()});
    }

    public int update(String table_name, String id, String fields, String[] fieldvalues) {
        Log.d(DEBUG_TAG, "update table:" + table_name);
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";
        if (table_name.equals("qmCheckRecordMst") || table_name.equals("qmCheckRecordDtl")) {
        	where = "uID = ?";
        }
        else {
        	where = "iID = ?";
        }
        ContentValues cv = new ContentValues(); 
        String[] fieldarray = fields.split(",");
		for (int i = 0; i < fieldarray.length; i++) {
			cv.put(fieldarray[i], fieldvalues[i]);
		}
		// 如果是单独更新upload时间时不更新modify，因为modify根据upload来判断是否上传数据到服务器上，如果他们同时更新的话会导致modify时间总是晚于upload时间而每次都进行更新
        if (table_name == "qmCheckRecordMst" && !fields.equals("datetime_upload")) {
            cv.put("datetime_modify", comm.getNowStringDateTime());
        } else if (table_name == "qmCheckRecordDtl") {
            cv.put("datetime_modify", comm.getNowStringDateTime());
        }
        return db.update(table_name, cv, where, new String[]{id});
    }    
}
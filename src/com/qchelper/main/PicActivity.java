package com.qchelper.main;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qchelper.comm.comm;
import com.qchelper.comm.dbHelper;


public class PicActivity extends Activity {
	final static String TAG="PicActivity";
	
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径  
	public static final String SAVE_PATH_IN_SDCARD = "/QCHelper/PicTemp"; //图片及其他数据保存文件夹  
	public static final String IMAGE_CAPTURE_NAME = "cameraTmp.jpg"; //照片名称  
	public final static int REQUEST_CODE_TAKE_PICTURE = 1; //设置拍照操作的标志  
	public final static int REQUEST_CODE_IMAGE_SHOW = 2; //设置大图浏览的标志
	
	String MstID;
	String[] ItemKeyList;
	
	Button btnPicBack;
	Button btnGetPic;
	GridView gvPicView;
	ImageAdapter impAdapter;
	
	int SaveMaxHeight = 800; //保存图片的高度,以后可考虑支持配置
	int ShowMaxHeight = 120; //显示列表中图片高度,后可考虑支持配置

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "ActivityCreate_Begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        MstID = bundle.getString("KeyID");
        
        btnPicBack = (Button) findViewById(R.id.btnPicBack);
        btnPicBack.setOnClickListener(new ButtonClickEvent());
        btnGetPic = (Button) findViewById(R.id.btnGetPic);
        btnGetPic.setOnClickListener(new ButtonClickEvent());
        
        gvPicView = (GridView) findViewById(R.id.gvPicView);
        gvPicView.setNumColumns(3); 
        impAdapter = new ImageAdapter(this, R.layout.qcpicitem, GetImageData());
        gvPicView.setAdapter(impAdapter);
        gvPicView.setOnItemClickListener(new ItemClickEvent());
        
        Log.d(TAG, "ActivityCreate_End");
    }
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pic, menu);
        return true;
    }
    */
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   	
    	Log.d(TAG, "ActivityResult");
    	
    	if (requestCode == REQUEST_CODE_TAKE_PICTURE){
    		if (resultCode == Activity.RESULT_OK) {
        		Bitmap PicData;

        		if (isHasSdcard()) {
        			PicData = getImage(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD + "/" + IMAGE_CAPTURE_NAME);
        			Log.d(TAG, "ActivityResult_getImage");
        		}
        		else {
                	Bundle bundle = data.getExtras();
                	PicData=(Bitmap) bundle.get("data");
        		} 
          		
        		Log.d(TAG, "ActivityResult_ImageHeight:" + Integer.toString(PicData.getHeight())); 
            	if (PicData != null) {
            		dbHelper dbhlp = new dbHelper(this);
            		if (dbhlp.insertCheckRecordDtl("qmCheckRecordDtl", MstID, comm.bitmapToBytes(PicData)) > 0) {
            			comm.showMsg(this, R.string.Image_SuccessfullySaved);
            	        impAdapter = new ImageAdapter(this, R.layout.qcpicitem, GetImageData());
            	        gvPicView.setAdapter(impAdapter);
            	        dbhlp.updateOptDatetime("qmCheckRecordMst", MstID);
            		}
            		else {
            			comm.showErrorMsg(this, R.string.Image_FailSaved);
            		}            			
            	}       		
        	} 
    		else {
    			comm.showMsg(this, R.string.Image_CancelPicture);
    		}
    	}
    	else if (requestCode == REQUEST_CODE_IMAGE_SHOW) {
	        impAdapter = new ImageAdapter(this, R.layout.qcpicitem, GetImageData());
	        gvPicView.setAdapter(impAdapter);    		
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data); 	
   	
    }
    
    private Bitmap[] GetImageData() {
    	Log.d(TAG, "GetImageData");
    	Bitmap[] Data = new Bitmap[15];
    	
    	dbHelper dbhlp = new dbHelper(this);
    	Cursor cursor = dbhlp.select("qmCheckRecordDtl", "uID,sPhoto", "uMstID = ?", new String[]{MstID});
    	if (cursor.getCount() > 0) {
    		ItemKeyList = new String[cursor.getCount()];
    		while (cursor.moveToNext()) {
    			ItemKeyList[cursor.getPosition()] = cursor.getString(0);
    			Data[cursor.getPosition()] = comm.bytesTobitmap(cursor.getBlob(1));
    		}
    	}
    	return Data;    	
    }
    
    class ImageAdapter extends ArrayAdapter<Bitmap> {
        public ImageAdapter(Context context,  int textViewResourceId, Bitmap[] objects) {
            super(context,  textViewResourceId, objects);
        }
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            View view = convertView;
            Log.d(TAG, "getView:" + Integer.toString(position));
            view = LayoutInflater.from(getContext()).inflate(R.layout.qcpicitem, null);
            //显示名称
            ImageView imgView = (ImageView) view.findViewById(R.id.imgCheckPic);
            TextView txeDesc = (TextView) view.findViewById(R.id.imgPicDesc);
            
            Bitmap bmap = getItem(position);
            //imgView.setMaxHeight(60);
            imgView.setImageBitmap(bmap);
            txeDesc.setText("图 " + Integer.toString(position + 1));
            
            //返回重写的view
            return view;
        }       
    }    
    
    private Bitmap getImage(String imagePath){
    	Log.d(TAG, "getImage:Begin");
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	
    	options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高  
    	Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); //此时返回bitmap为空
    	
    	Log.d(TAG, "getImage:1");
 
    	//计算缩放比  
    	int be = (int)(options.outHeight / (float)SaveMaxHeight);  
    	int ys = options.outHeight % SaveMaxHeight;//求余数  
    	float fe = ys / (float)SaveMaxHeight;  
    	if(fe>=0.5)be = be + 1;  
    	if (be <= 0)  
    	  be = 1;
    	Log.d(TAG, "getImage:" + Integer.toString(be) + "_" + Integer.toString(ys));    	
    	options.inSampleSize = be; 
    	
    	//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false  
    	options.inJustDecodeBounds = false;  
    	bitmap=BitmapFactory.decodeFile(imagePath,options);
    	Log.d(TAG, "getImage:End");
    	return bitmap;
    }
    
    public static boolean isHasSdcard() {    	
        String status = Environment.getExternalStorageState();  
        if (status.equals(Environment.MEDIA_MOUNTED)){
            return true;  
        } else {  
            return false;  
        }  
     } 
    
    public void CallCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		if ((ItemKeyList == null) || (ItemKeyList.length < 15)) {
			// 存储卡可用 将照片存储在 sdcard(小米手机直接从系统相机中返回图片有问题)
			if(isHasSdcard()){
				Log.d(TAG, SDCARD_ROOT_PATH+ SAVE_PATH_IN_SDCARD);
				File DestDir = new File(SDCARD_ROOT_PATH+ SAVE_PATH_IN_SDCARD);
				if (! DestDir.exists()) {
					DestDir.mkdirs();
					
				}
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD + "/",IMAGE_CAPTURE_NAME)));
				Log.d(TAG, "ActivityGetPic_ExistsCard");
			}				
			startActivityForResult(intent,REQUEST_CODE_TAKE_PICTURE);					
		}
		else {
			Toast.makeText(PicActivity.this, R.string.image_count_check, 1500).show();
		}    	
    }
    
    class ItemClickEvent implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.d(TAG, "ItemClickEvent:" + Integer.toString(arg2));
			int FPosition = arg2;
			
			//Log.d(TAG, "ItemClickEvent:" + Integer.toString(FPosition) + "_" + Integer.toString(ItemKeyList.length));
			if ((ItemKeyList != null) && ((FPosition + 1) <= ItemKeyList.length)) {
				Log.d(TAG, "ItemClickEvent:" + Integer.toString(arg2));
				Intent intent = new Intent(PicActivity.this, ImageShowActivity.class);
				intent.putExtra("KeyID", ItemKeyList[FPosition]);
				startActivityForResult(intent, REQUEST_CODE_IMAGE_SHOW);
			}
			else {
				CallCamera();
				//Toast.makeText(PicActivity.this, R.string.image_not_exists, 1500).show();
			}
		}    	
    }
    
    class ButtonClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			switch (v.getId()){
			case R.id.btnGetPic: {
				Log.d(TAG, "ActivityGetPic_Begin");
				CallCamera();
				Log.d(TAG, "ActivityGetPic_End");
				break;
			}
			case R.id.btnPicBack: {
				finish();
				break;
			}
			}
		}
    }
}

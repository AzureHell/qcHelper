package com.qchelper.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.provider.MediaStore;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.graphics.Bitmap;
import android.widget.Toast;
import android.os.Environment;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.database.Cursor;
import android.widget.ImageView;
import android.widget.AdapterView;

import com.example.qchelper.R;
import com.qchelper.main.ImageShowActivity;
import com.qchelper.main.dbHelper;


public class PicActivity extends Activity {
	final static String TAG="PicActivity";
	
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//·��  
	public static final String SAVE_PATH_IN_SDCARD = "/QCHelper/PicTemp"; //ͼƬ���������ݱ����ļ���  
	public static final String IMAGE_CAPTURE_NAME = "cameraTmp.jpg"; //��Ƭ����  
	public final static int REQUEST_CODE_TAKE_PICTURE = 1; //�������ղ����ı�־  
	public final static int REQUEST_CODE_IMAGE_SHOW = 2; //���ô�ͼ����ı�־
	
	int MstID;
	int[] ItemKeyList;
	
	Button btnPicBack;
	Button btnGetPic;
	GridView gvPicView;
	ImageAdapter impAdapter;
	
	int SaveMaxHeight = 800; //����ͼƬ�ĸ߶�,�Ժ�ɿ���֧������
	int ShowMaxHeight = 120; //��ʾ�б���ͼƬ�߶�,��ɿ���֧������

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "ActivityCreate_Begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        MstID = bundle.getInt("KeyID");
        
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
            		if (dbhlp.insert("qmCheckRecordDtl", MstID, BitmapToBytes(PicData)) > 0) {
            			Toast.makeText(this, R.string.Image_SuccessfullySaved, 1500).show();            			
            	        impAdapter = new ImageAdapter(this, R.layout.qcpicitem, GetImageData());
            	        gvPicView.setAdapter(impAdapter);
            	        dbhlp.UpdateOptDatetime("qmCheckRecordMst", MstID, "U001");
            	        
            		}
            		else {
            			Toast.makeText(this, R.string.Image_FailSaved, 1500).show();
            		}            			
            	}       		
        	} 
    		else {
    			Toast.makeText(this, R.string.Image_CancelPicture, 1500).show();    			
    		}
    	}
    	else if (requestCode == REQUEST_CODE_IMAGE_SHOW) {
	        impAdapter = new ImageAdapter(this, R.layout.qcpicitem, GetImageData());
	        gvPicView.setAdapter(impAdapter);    		
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data); 	
   	
    }
    
    private Bitmap[] GetImageData() {
    	Log.d(TAG, "GetImageData_Begin");
    	Bitmap[] Data = new Bitmap[15];
    	
    	Log.d(TAG, "GetImageData_1:" + Integer.toString(MstID));
    	
    	dbHelper dbhlp = new dbHelper(this);
    	Log.d(TAG, "GetImageData_2");
    	Cursor cursor = dbhlp.querySQL("select iID, sPhoto from qmCheckRecordDtl where iMstID=" + Integer.toString(MstID)
    			+ " order by iID asc "
    			+ " limit 15 ");
    	Log.d(TAG, "GetImageData_3");
    	if (cursor.getCount() > 0) {
    		//Data = new Bitmap[cursor.getCount()];
    		ItemKeyList = new int[cursor.getCount()];
    		while (cursor.moveToNext()) {
    			ItemKeyList[cursor.getPosition()] = cursor.getInt(0);
    			Data[cursor.getPosition()] = BytesToBitmap(cursor.getBlob(1));
    		}
    	}
    	Log.d(TAG, "GetImageData_End");
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
            //��ʾ����
            ImageView imgView = (ImageView) view.findViewById(R.id.imgCheckPic);
            TextView txeDesc = (TextView) view.findViewById(R.id.imgPicDesc);
            
            Bitmap bmap = getItem(position);
            //imgView.setMaxHeight(60);
            imgView.setImageBitmap(bmap);
            txeDesc.setText("ͼ " + Integer.toString(position + 1));
            
            //������д��view
            return view;
        }       

    }    
    
    private Bitmap getImage(String imagePath){
    	Log.d(TAG, "getImage:Begin");
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	
    	options.inJustDecodeBounds = true;  
        // ��ȡ���ͼƬ�Ŀ�͸�  
    	Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); //��ʱ����bitmapΪ��
    	
    	Log.d(TAG, "getImage:1");
 
    	//�������ű�  
    	int be = (int)(options.outHeight / (float)SaveMaxHeight);  
    	int ys = options.outHeight % SaveMaxHeight;//������  
    	float fe = ys / (float)SaveMaxHeight;  
    	if(fe>=0.5)be = be + 1;  
    	if (be <= 0)  
    	  be = 1;
    	Log.d(TAG, "getImage:" + Integer.toString(be) + "_" + Integer.toString(ys));    	
    	options.inSampleSize = be; 
    	
    	//���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
    	options.inJustDecodeBounds = false;  
    	bitmap=BitmapFactory.decodeFile(imagePath,options);
    	Log.d(TAG, "getImage:End");
    	return bitmap;
    }
    
    
    public byte[] BitmapToBytes(Bitmap bmap) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    	return baos.toByteArray();
    }
    
    public Bitmap BytesToBitmap(byte[] bytes) {
    	if (bytes.length > 0){
           /*
    		BitmapFactory.Options options = new BitmapFactory.Options();        	
        	options.inJustDecodeBounds = true;         	
        	Log.d(TAG, "BytesToBitmap:1");     
        	//�������ű�  
        	int be = (int)(options.outHeight / (float)ShowMaxHeight);  
        	int ys = options.outHeight % ShowMaxHeight;//������  
        	float fe = ys / (float)ShowMaxHeight;  
        	if(fe>=0.5)be = be + 1;  
        	if (be <= 0)  
        	  be = 1;
        	Log.d(TAG, "BytesToBitmap:" + Integer.toString(be) + "_" + Integer.toString(ys));    	
        	options.inSampleSize = be; 
        	
        	//���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
        	options.inJustDecodeBounds = false;   		
    		
    		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);*/
    		
    		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    	}
        else {
        	return null;
        }
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
			// �洢������ ����Ƭ�洢�� sdcard(С���ֻ�ֱ�Ӵ�ϵͳ����з���ͼƬ������)
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

package com.qchelper.main;

import java.io.BufferedOutputStream;    
import java.io.File;    
import java.io.FileOutputStream;    
import java.io.IOException;    

import com.qchelper.comm.comm;
import com.qchelper.comm.dbHelper;

import android.app.Activity;    
import android.content.pm.ActivityInfo;    
import android.graphics.Bitmap;    
import android.graphics.BitmapFactory;    
import android.graphics.PixelFormat;    
import android.hardware.Camera;    
import android.hardware.Camera.AutoFocusCallback;    
import android.hardware.Camera.PictureCallback;    
import android.os.Bundle;    
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;    
import android.view.SurfaceView;    
import android.view.View;    
import android.view.Window;    
import android.view.SurfaceHolder.Callback;    
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;

//import android.view.WindowManager.LayoutParams;
//import android.widget.LinearLayout;

import android.content.res.Configuration;
import android.graphics.Matrix;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.util.DisplayMetrics;

import android.widget.Toast;

public class CameraActivity extends Activity implements 
  Callback, OnClickListener, AutoFocusCallback{
	final static String TAG = "CameraActivity";
	
    SurfaceView mySurfaceView;
    SurfaceHolder holder;
    Camera myCamera = null;
    boolean isClicked = false; //是否拍照
    byte[] imageBytes = null;
    boolean OrientationState = true;
    
    Button btnOk, btnTake, btnRetake, btnCancel;
    
    int PreviewHeight, PreviewWidth, PicHeight, PicWidth;
    
    String MstID;
    
//    LinearLayout llviewlayout;
    
    private SensorManager myManager = null;
    
    //创建jpeg图片回调数据对象    
    PictureCallback jpeg = new PictureCallback() {    
            
//        @Override    
        public void onPictureTaken(byte[] data, Camera camera) {    
            // TODO Auto-generated method stub    
            try    
            {// 获得图片
            	if (data.length > 0) {
            		if (OrientationState == true) {            			
            			Log.d(TAG, "Taken_Portrait");
            			Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
            			Bitmap bMapRotate;
                        Matrix matrix = new Matrix();
                        matrix.reset();
                        matrix.postRotate(90);
                        bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), matrix, true);
//                        bMap = bMapRotate;
//                        bMapRotate = Bitmap.createBitmap(bMap, 0, 0, PicWidth, PicHeight, matrix, true);
                        if (bMapRotate != null) {
                        	Log.d(TAG, "Taken_Portrait_2");
                        	imageBytes = comm.bitmapToBytes(bMapRotate);
                        } 
            		}else {
            			Log.d(TAG, "Taken_LandScape");         			
            			imageBytes = data;
            		} 
//            		imageBytes = data;
                    isClicked = true;
                    SetButtonState(isClicked);            		
            	}
//            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);    
//            File file = new File(filePath);    
//            BufferedOutputStream bos =    
//                new BufferedOutputStream(new FileOutputStream(file));    
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中    
//            bos.flush();//输出    
//            bos.close();//关闭    
            }catch(Exception e)    
            {    
               e.printStackTrace();    
            }    
               
        }    
    };    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //设置拍摄方向
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        
        Bundle bundle = this.getIntent().getExtras();
        MstID = bundle.getString("MstID");
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        if (dm.heightPixels >= 480) {
        	PreviewHeight = 480;
        	PreviewWidth = 640;
        } else if ((dm.heightPixels >= 320) && (dm.heightPixels < 480)) {
        	PreviewHeight = 320;
        	PreviewWidth = 480;       	
        } else {
        	PreviewHeight = 240;
        	PreviewWidth = 320;         	
        }
        dm = null;
//    	PreviewHeight = 480;
//    	PreviewWidth = 640;        
        PicHeight =1200; PicWidth = 1600;
        
//        Log.d(TAG, "Display: " + dm.widthPixels + ", " + dm.heightPixels);
//        Toast.makeText(this, "Display: " + dm.widthPixels + ", " + dm.heightPixels, 1500).show();
        
//        llviewlayout = (LinearLayout) findViewById(R.id.camera_view);                
//        mySurfaceView = new SurfaceView(this);
//        llviewlayout.addView(mySurfaceView);
//        Log.d(TAG, "Create_2");
//        LayoutParams lp = new LayoutParams(); //(LayoutParams) mySurfaceView.getLayoutParams();
//        Log.d(TAG, "Create_3");
//        lp.height = 640;
//        lp.width = 480;
//        lp.gravity = Gravity.CENTER;
//        mySurfaceView.setLayoutParams(lp);
//        Log.d(TAG, "Create_4");
        
        mySurfaceView = (SurfaceView)findViewById(R.id.Surface_view);
        //获得句柄    
        holder = mySurfaceView.getHolder();    
        //添加回调    
        holder.addCallback(this);    
        //设置类型    
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);    
        //设置监听    
        mySurfaceView.setOnClickListener(this); 
        
        btnOk = (Button) findViewById(R.id.camera_ok);
        btnOk.setOnClickListener(this);
        
        btnTake = (Button) findViewById(R.id.camera_take);
        btnTake.setOnClickListener(this);
        
        btnRetake = (Button) findViewById(R.id.camera_retake);
        btnRetake.setOnClickListener(this);
        
        btnCancel = (Button) findViewById(R.id.camera_cancel);
        btnCancel.setOnClickListener(this);        
        
        SetButtonState(isClicked);
        
        myManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        myManager.registerListener(mySensorListener, 
//        		myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
//        		SensorManager.SENSOR_DELAY_UI);
//        myManager.registerListener(mySensorListener, 
//        		myManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
//        		SensorManager.SENSOR_DELAY_UI);        
        myManager.registerListener(mySensorListener, 
        		myManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
        		SensorManager.SENSOR_DELAY_UI);         
        Log.d(TAG, "Camera_Create");
        
    }  
    
    float[] mags = new float[3];
    float[] accels = new float[3];
    float[] InclinationMat = new float[9];
    float[] RotationMat = new float[9];
    float[] attitude = new float[3];
    private final SensorEventListener mySensorListener = new SensorEventListener() {    	

    	
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
//			Log.d(TAG, "onSensorChanged");
			int type = event.sensor.getType();
//			if (type == Sensor.TYPE_MAGNETIC_FIELD) {
//				mags = event.values;
//			}
//			if (type == Sensor.TYPE_ACCELEROMETER) {
//				accels = event.values;
//			}
//			SensorManager.getRotationMatrix(RotationMat, InclinationMat, accels, mags);
//			SensorManager.getOrientation(RotationMat, attitude);
			if (type == Sensor.TYPE_ORIENTATION) {
				attitude = event.values;
			}
//			Log.d(TAG, "onSensorChanged: " + attitude[0] + ", " + attitude[1] + ", " + attitude[2]);
			if ((attitude[2] >= -45) && (attitude[2] <= 45)) {
//				Log.d(TAG, "H");
				CameraActivity.this.OrientationState = true; 
			} else {
				CameraActivity.this.OrientationState = false;
//				Log.d(TAG, "S");
			}
		}    	
    };    

    public void SetButtonState(boolean IsState) {
        btnOk.setEnabled(IsState);
        btnRetake.setEnabled(IsState);
        btnTake.setEnabled(!IsState);
//        btnCancel.setEnabled(!IsState);        
    }
       
//    @Override    
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {    
        // TODO Auto-generated method stub
    	Log.d(TAG, "surfaceChanged");
        //设置参数并开始预览    
        Camera.Parameters params = myCamera.getParameters();    
        params.setPictureFormat(PixelFormat.JPEG);    
        params.setPreviewSize(PreviewWidth, PreviewHeight);
        params.setPictureSize(PicWidth, PicHeight);
        myCamera.setParameters(params);    
        myCamera.startPreview();    
            
    }    
    
//    @Override    
    public void surfaceCreated(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
        //开启相机    
        if(myCamera == null)    
        {    
            myCamera = Camera.open();    
            try { 
//            	myCamera.setDisplayOrientation(90);
//                myCamera.setPreviewDisplay(holder);
            	Camera.Parameters param = myCamera.getParameters();
            	if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){ //竖屏
            		Log.d(TAG, "portrait");
            		param.set("orientation", "portrait");
            		myCamera.setDisplayOrientation(90);
            	}else { //横屏
            		Log.d(TAG, "landscape");
            		param.set("orientation", "landscape");
            		myCamera.setDisplayOrientation(0);
            	}
            	myCamera.setParameters(param);
            	myCamera.setPreviewDisplay(holder);             	
            } catch (IOException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
            }    
        }    
           
    }
    
//    @Override    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
        //关闭预览并释放资源    
        myCamera.stopPreview();    
        myCamera.release();    
        myCamera = null;    
            
    }
    
//    @Override    
    public void onClick(View v) {    
    	
//        if(!isClicked)    
//        {    
//            myCamera.autoFocus(this);//自动对焦    
//            isClicked = true;    
//        }else    
//        {    
//            myCamera.startPreview();//开启预览    
//            isClicked = false;    
//        }  
        
        switch (v.getId()) {
        case R.id.Surface_view: {
            if(!isClicked)    
            {    
                myCamera.autoFocus(this);//自动对焦
            }else    
            {    
                myCamera.startPreview();//开启预览    
                isClicked = false;
                SetButtonState(isClicked);
                imageBytes = null;
            }
            break;
        }
        case R.id.camera_ok: {
        	Log.d(TAG, "RestltOk");
//        	Intent intent = this.getIntent();
//        	intent.putExtra("ImageData", imageBytes);
//        	this.setResult(RESULT_OK, intent);
//        	Log.d(TAG, "RestltOk_2");
//        	this.finish();
//        	Log.d(TAG, "RestltOk_3");
        	if ((imageBytes != null) && (imageBytes.length > 0)) {
        		Log.d(TAG, MstID);
        		dbHelper dbhlp = new dbHelper(this);
        		if (dbhlp.insertCheckRecordDtl("qmCheckRecordDtl", MstID, imageBytes) > 0) {        			
        	        dbhlp.updateOptDatetime("qmCheckRecordMst", MstID);
        	        this.setResult(RESULT_OK);
        	        comm.showMsg(this, R.string.Image_SuccessfullySaved);
        	        Log.d(TAG, "SuccessSaved");
        		}
        		else {
        			this.setResult(RESULT_CANCELED);
        			comm.showErrorMsg(this, R.string.Image_FailSaved);
        		}        		
        		
        	}
        	this.finish();
        	break;
        }
        case R.id.camera_take: {
            myCamera.autoFocus(this);//自动对焦    

            break;
        }
        case R.id.camera_retake: {
            myCamera.startPreview();//开启预览    
            isClicked = false;
            SetButtonState(isClicked);
            imageBytes = null;
            break;
        }
        case R.id.camera_cancel: {
        	this.setResult(RESULT_CANCELED);
        	this.finish();
        	break;
        }
        
        }
            
    }
    
//    @Override    
    public void onAutoFocus(boolean success, Camera camera) {    
        // TODO Auto-generated method stub    
        if(success)    
        {    
           //设置参数,并拍照    
           Camera.Parameters params = camera.getParameters();    
           params.setPictureFormat(PixelFormat.JPEG);
           params.setPreviewSize(PreviewWidth, PreviewHeight);
           
           params.setPictureSize(PicWidth, PicHeight);
        	   
           camera.setParameters(params); 
           camera.takePicture(null, null, jpeg);    
        } 
        
    } 
    
}

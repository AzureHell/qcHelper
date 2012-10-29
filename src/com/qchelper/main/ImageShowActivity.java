package com.qchelper.main;

import java.util.ArrayList;
import java.util.List;

import com.qchelper.comm.dbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageShowActivity extends Activity {
	final static String TAG = "ImageShowActivity";
    int KeyID;
    int PositionID = -1;
    int MstID;
    ImageView imgView;
    Button btnImgShowBack;
    Button btnImgShowDelete;
    final static int CURRENT_ROW = 0; //当前记录
    final static int NEXT_ROW = 1; //下一记录
    final static int PRIOR_ROW = 2; //上一记录
    
    private Bitmap CurrMap;
    float x=0,y=0;


    List<String> KeyList = new ArrayList<String>();
    
    //图片缩放控制
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;	
	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	
	/* 图片左右移动 */
	// 声明两个按钮,分别代表向左和向右滑动

	private ImageView btnLeft = null;  
	private ImageView btnRight = null;  
	// 设置WindowManager
	private WindowManager wm = null;  
	private WindowManager.LayoutParams wmParams = null;	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        KeyID = bundle.getInt("KeyID"); 
               
        imgView = (ImageView) findViewById(R.id.ivImageShow);
        CurrMap = GetImageData(KeyID);
        imgView.setImageBitmap(CurrMap);
        
        //获取主键列表
        GetKeyList();
        
        btnImgShowBack = (Button) findViewById(R.id.btnShowBack);
        btnImgShowDelete = (Button) findViewById(R.id.btnDeleteImage);
        btnImgShowBack.setOnClickListener(new ButtonClickEvent());
        btnImgShowDelete.setOnClickListener(new ButtonClickEvent());
                
        imgView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				ImageView view = (ImageView) v;
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					if (oldDist > 10f) {
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x, event.getY()
								- start.y);
					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						if (newDist > 10f) {
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					break;
				}

				view.setImageMatrix(matrix);
				return true;
			}
			
			private float spacing(MotionEvent event) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}

			private void midPoint(PointF point, MotionEvent event) {
				float x = event.getX(0) + event.getX(1);
				float y = event.getY(0) + event.getY(1);
				point.set(x / 2, y / 2);
			}}); 
        
        
        // 初始化左右按钮  
        initImageButtonView();        
        
    }
    
    
    //初始化悬浮按钮    
    private void initImageButtonView() {
    	Log.d(TAG, "initImageButtonView_Begin");
        // 获取WindowManager  
        wm = (WindowManager) getApplicationContext().getSystemService("window");  
        // 设置LayoutParams相关参数  
        wmParams = new WindowManager.LayoutParams();  
        // 设置window type  
        wmParams.type = LayoutParams.TYPE_PHONE;  
        // 设置图片格式,效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;  
        // 设置Window flag参数  
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL  
              | LayoutParams.FLAG_NOT_FOCUSABLE;  
        // 设置x、y初始值  
        wmParams.x = 0;  
        wmParams.y = 0;  
        // 设置窗口长宽数据  
        wmParams.width = 100;  
        wmParams.height = 100;         
        // 创建左右按钮  
        createLeftButtonView();  
        createRightButtonView();
        Log.d(TAG, "initImageButtonView_End");
    } 
   
    // 设置左边按钮   
    private void createLeftButtonView() {
    	Log.d(TAG, "createLeftButtonView_Begin");
        btnLeft = new ImageView(this);  
        btnLeft.setImageResource(R.drawable.prior);  
        btnLeft.setAlpha(200);
        Log.d(TAG, "createLeftButtonView_1");
        btnLeft.setOnClickListener(new View.OnClickListener() {        	
            public void onClick(View arg0) {            	
                // 上一个图像 
            	if (KeyList.size() <= 0) {
            		Toast.makeText(ImageShowActivity.this, "检验项目中不存在图片", 1000).show();
            	} else {
                	if ((PositionID - 1) < 0) {
                    	Toast.makeText(ImageShowActivity.this, "已是第一张图片", 1000).show();                	
                    } 
                    else {
                    	PositionID = PositionID - 1;
                    	CurrMap = GetImageData(Integer.parseInt(KeyList.get(PositionID)));
                    	imgView.setImageBitmap(CurrMap);
                    }              		
            	}            	  	
 
            } });  
        // 调整窗口  
        wmParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        Log.d(TAG, "createLeftButtonView_2");
        // 显示图像  
        wm.addView(btnLeft, wmParams);
        Log.d(TAG, "createLeftButtonView_Begin");
    }  
    
    // 设置右边按钮   
    private void createRightButtonView() {    	
        btnRight = new ImageView(this);  
        btnRight.setImageResource(R.drawable.next);  
        btnRight.setAlpha(200);  
        btnRight.setOnClickListener(new View.OnClickListener() {        	
            public void onClick(View arg0) {             	
                // 下一个图像
            	if (KeyList.size() <= 0) {
            		Toast.makeText(ImageShowActivity.this, "检验项目中不存在图片", 1000).show();
            	} else {
                	if ((PositionID + 1) >= KeyList.size()) {
                    	Toast.makeText(ImageShowActivity.this, "已是最后一张图片", 1000).show();                	
                    } 
                    else {
                    	PositionID = PositionID + 1;
                    	CurrMap = GetImageData(Integer.parseInt(KeyList.get(PositionID)));                    	
                    	imgView.setImageBitmap(CurrMap);
                    }              		
                	
            	}            	
               
            } });  
        // 调整窗口  
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;  
        // 显示图像  
        wm.addView(btnRight, wmParams);  
    }
    
      
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_show, menu);
        return true;
    }
    */

    private Bitmap GetImageData(int CurrID) {
    	Log.d(TAG, "GetImageData_Begin");
    	Bitmap Data = null;
    	
    	String SqlText = "select iMstID, sPhoto from qmCheckRecordDtl where iID=" + Integer.toString(CurrID);
    	
    	dbHelper dbhlp = new dbHelper(this);
    	Log.d(TAG, "GetImageData_1");
    	Cursor cursor = dbhlp.querySQL(SqlText);
    	Log.d(TAG, "GetImageData_2");
    	if (cursor.getCount() > 0) {
    		cursor.moveToFirst();
    		MstID = cursor.getInt(0); 
    		Data = BytesToBitmap(cursor.getBlob(1));
    	}
    	Log.d(TAG, "GetImageData_End");
    	
    	return Data;    	
    } 
    
    private void GetKeyList() {
    	String SqlText = "select iID from qmCheckRecordDtl where iMstID=" + Integer.toString(MstID);
    	
    	dbHelper dbhlp = new dbHelper(this);
    	Log.d(TAG, "GetImageData_1");
    	Cursor cursor = dbhlp.querySQL(SqlText);
    	Log.d(TAG, "GetImageData_2");
    	if (cursor.getCount() > 0) {
    		while (cursor.moveToNext()) {
    			KeyList.add(cursor.getString(0));
    			if (KeyID == cursor.getInt(0)) {
    				PositionID = cursor.getPosition();
    			}
    		}
    	}    	
    	
    }
    
    public Bitmap BytesToBitmap(byte[] bytes) {
    	if (bytes.length > 0){ 
    		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    	}
        else {
        	return null;
        }
    }
    
    public void AskDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(R.string.dialog_askinfo);
        builder.setTitle(R.string.dialog_titile);
                
        builder.setPositiveButton(R.string.btn_Ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String StrKey = KeyList.get(PositionID);
				
                dbHelper dbhlp = new dbHelper(ImageShowActivity.this);
                dbhlp.delete("qmCheckRecordDtl", Integer.parseInt(StrKey));
                KeyList.remove(PositionID);
                if ((PositionID + 1) > KeyList.size()) {
                	PositionID = PositionID - 1;                	
                }
                if (KeyList.size() > 0) {
                    CurrMap = GetImageData(Integer.parseInt(KeyList.get(PositionID)));
                    imgView.setImageBitmap(CurrMap);                	
                }
                else {
                	imgView.setImageBitmap(null);
                }
                Toast.makeText(ImageShowActivity.this, "图片已删除", 1500).show();
		    	dialog.dismiss();	
			}

    	});
        
    	builder.setNegativeButton(R.string.btn_Cancel, new DialogInterface.OnClickListener() {

    	public void onClick(DialogInterface dialog, int which) {   		
    	   dialog.dismiss();
    	 }
    	});
    	builder.create().show();
     }    
    
    class ButtonClickEvent implements View.OnClickListener {

		public void onClick(View v) {
			switch (v.getId()){
			case R.id.btnDeleteImage: {
				AskDialog();
				break;
			}
			case R.id.btnShowBack: {
				ImageShowActivity.this.finish();
				break;
			}
			}
			
		}
    	
    }    

    
    @Override
    public void onDestroy(){
        super.onDestroy();
        //在程序退出(Activity销毁）时销毁悬浮窗口
        wm.removeView(btnLeft);
        wm.removeView(btnRight);
    }     
}

package com.example.dialogtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cDialog();
        
    }

    Handler mhHandler =new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		proDia.setProgress(msg.arg1);
    	};
    };
    

	private ProgressDialog proDia;   
	public void cDialog(){
		proDia = new ProgressDialog(MainActivity.this);
    	proDia.setTitle("搜索网络");
    	proDia.setMessage("请耐心等待");
    	//设置水平条的格式为水平
    	proDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	//设置最大值
    	proDia.setMax(100);
    	//设置进度的开始数值,从30开始
    	proDia.setProgress(30);
    	proDia.setCancelable(false);
    	proDia.show();
    	
        final String fileName = "updata.apk";
        File tmpFile = new File("/sdcard/update");
        if (!tmpFile.exists()) {
                tmpFile.mkdir();
        }
        final File file = new File("/sdcard/update/" + fileName);
    	System.gc();
    	new AppDownload("http://storage.pgyer.com/4/d/4/1/a/4d41a4cf619f7b02f9e18b23b693366d.apk",file) {
			
			@Override
			protected int doInBackgroundproess(int txt) {
				Message message = mhHandler.obtainMessage();
				message.arg1 = txt;
				mhHandler.sendMessage(message);
				return 0;
			}
		}.execute(file.length());
    } 

}

package com.example.dialogtest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Message;

public abstract class AppDownload extends AsyncTask<Long,Integer,Integer>{
		String mWebPath;
		File mFile;
		
		public AppDownload(String webPath,File file){
			mWebPath = webPath;
			mFile = file;
		}
		
		@Override
		protected Integer doInBackground(Long... params) {
			HttpURLConnection httpURLConnection = null;
			BufferedInputStream bufferedInputStream = null;
			BufferedOutputStream bufferedOutputStream = null;
			URL url;
			try {
				url = new URL(mWebPath);
				httpURLConnection = (HttpURLConnection)url.openConnection();
				// 读取超时最长时间
				httpURLConnection.setReadTimeout(10 * 1000);
				//连接时间
				httpURLConnection.setConnectTimeout(1000 * 10);
				// 允许输入输出
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				// 不允许缓存
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setRequestProperty(HTTP.CHARSET_PARAM, HTTP.UTF_8);
				httpURLConnection.setRequestProperty(HTTP.CONN_DIRECTIVE,HTTP.CONN_KEEP_ALIVE);
				//文件长度
				int fileLength = httpURLConnection.getContentLength();
				
				int readLength = 0;//当前读取长度
				long completeLength = 0;//完成度
				byte[] buffer = new byte[1024*2];
				
				bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(mFile));
				long lastTime = System.currentTimeMillis();
				
				while((readLength = bufferedInputStream.read(buffer))>0){
					bufferedOutputStream.write(buffer,0,readLength);
					completeLength += readLength;
					long currentTime = System.currentTimeMillis();
					int progress = (int)(((double)completeLength/fileLength)*100);
					if(progress==100 || currentTime-lastTime >= 1000){
//						Message message = mHandler.obtainMessage();
//						message.arg1 = progress;
//						mHandler.sendMessage(message);
						doInBackgroundproess(progress);
						lastTime = currentTime;
					}
				}
				bufferedOutputStream.flush();
			}catch(MalformedURLException e){
				e.printStackTrace();
				return -1;
			}catch(IOException e){
				e.printStackTrace();
				return -1;
			}finally{
				try {
					if(bufferedInputStream != null)bufferedInputStream.close();
					if(bufferedOutputStream != null)bufferedOutputStream.close();
					if(httpURLConnection != null)httpURLConnection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					return 1;
				}
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result == -1);
		}
		
		 protected abstract int doInBackgroundproess(int txt);
}
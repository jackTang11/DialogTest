package com.example.dialogtest;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

/**
 * 存储路径
 * Provides application storage paths
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class StorageUtils {

	private static final String INDIVIDUAL_DIR_NAME = "uil-images";

	private StorageUtils() {
		
	}

	/**
	 * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
	 * is mounted. Else - Android defines cache directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @param cacheDir
	 *            Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if(appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())){
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "universal"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			try {
				new File(dataDir, ".nomedia").createNewFile();
			} catch (IOException e) {
			}
			if (!appCacheDir.mkdirs()) {
				return null;
			}
		}
		return appCacheDir;
	}
	

	
	
	/**
	 * sd卡状态
	 * @param context
	 * @return
	 */
	public static boolean isSDMounted(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false;
	}
	
	public static String getRootPath(String md5Str,String typeName){
		String path = Environment.getExternalStorageDirectory().toString()+"/healthsmallone/"+md5Str+"/"+typeName+"/";
		return path;
	}
	
	
	/**下载路径
	 * @return
	 */
	public static String getDownCachePath(){
		String path = getRootPath("111","downs");
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
			createRootFileDir(path);
		}
		return path;
	}
	
	public static File createRootFileDir(String path){
		File dataDir = new File(path);
		if(!dataDir.exists()){
			boolean b = dataDir.mkdirs();
			if(b){
				try{
					new File(dataDir, ".nomedia").createNewFile();
				}catch(IOException e){
				}
			}
		}
		return dataDir;
	}
	
}

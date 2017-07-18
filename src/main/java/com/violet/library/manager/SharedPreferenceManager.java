package com.violet.library.manager;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.violet.library.BaseApplication;
import com.violet.library.security.SecuritySharedPreference;

public class SharedPreferenceManager {
	private static SecuritySharedPreference.SecurityEditor editor;
	private static SecuritySharedPreference sp;

	private static SecuritySharedPreference getPreferencesInstance(Context context) {
		if (sp == null) {
			sp = new SecuritySharedPreference(context.getApplicationContext(),"config",Context.MODE_PRIVATE);
		}
		return sp;
	}

	private static Editor getEditorInstance(Context context) {

		if (editor == null) {
			editor = getPreferencesInstance(context).edit();
		}
		return editor;
	}

	public static void remove(Context context, String key) {
		getEditorInstance(context).remove(key).commit();
	}

	
	public static void setString(Context context, String key, String value) {
		getEditorInstance(context).putString(key, value).commit();
	}

	public static void setStringSync(Context context, String key, String value) {
		getEditorInstance(context).putString(key, value).apply();
	}

	public static void setBoolean(Context context, String key, boolean value) {
		getEditorInstance(context).putBoolean(key, value).commit();
	}
    public static void setInt(Context context, String key, int value){
    	getEditorInstance(context).putInt(key, value).commit();
    }
    public static void setLong(Context context, String key, long value){
    	getEditorInstance(context).putLong(key, value).commit();
    }
	public static void setFloat(Context context, String key, float value){
		getEditorInstance(context).putFloat(key, value).commit();
	}

	
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	};

	public static String getString(Context context, String key, String defValue) {
		return getPreferencesInstance(context).getString(key, defValue);
	};

	public static boolean getBoolean(Context context, String key) {
		return getPreferencesInstance(context).getBoolean(key, false);
	}
    public static int getInt(Context context, String key){
    	return getPreferencesInstance(context).getInt(key, -1);
    }
    public static long getLong(Context context, String key){
    	return getPreferencesInstance(context).getLong(key, -1);
    }
	public static float getFloat(Context context, String key){
		return getPreferencesInstance(context).getFloat(key, -1);
	}
}

package ai.t.rec;

import ai.t.rec.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfigUtil {

	static Context appContext = RecApplication.getContext();

	public static final String KEYS_SAVE_PATH = appContext
			.getString(R.string.keys_save_path);
	public static final String KEYS_SWAP_CHANNEL = appContext
			.getString(R.string.keys_swap_channel);
	public static final String KEYS_SAVE_MONO = appContext
			.getString(R.string.keys_save_mono);
	public static final String KEYS_SAVE_STEREO = appContext
			.getString(R.string.keys_save_stereo);

	private static ConfigUtil mInstance;
	private static SharedPreferences mSp = PreferenceManager
			.getDefaultSharedPreferences(RecApplication.getContext());

	public static ConfigUtil getInstance() {
		if (mInstance == null) {
			mInstance = new ConfigUtil();
		}
		return mInstance;
	}

	public String getSavePath() {
		String savePath = mSp.getString(KEYS_SAVE_PATH, RecApplication
				.getContext().getString(R.string.default_save_path));
		return savePath;
	}

	public void setSavePath(String savePath) {
		mSp.edit().putString(KEYS_SAVE_PATH, savePath).commit();
	}
	
	public Boolean isSaveMono(){
		return mSp.getBoolean(KEYS_SAVE_MONO, true);
	}
	
	public Boolean isSaveStereo(){
		return mSp.getBoolean(KEYS_SAVE_STEREO, true);
	}
	
	public Boolean isSwapChannel(){
		return mSp.getBoolean(KEYS_SWAP_CHANNEL, false);
	}
	
	public void showInfo(InfoCallback callback){
		String msg = "save_path:"+getSavePath()+"\n"
				+"isSwapChannel:"+isSwapChannel()+"\n"
				+"isSaveMono:"+isSaveMono()+"\n"
				+"isSaveStereo:"+isSaveStereo()+"\n";
				
		callback.onUpdateInfo(msg);
	}

}

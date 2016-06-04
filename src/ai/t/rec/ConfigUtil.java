package ai.t.rec;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfigUtil {

	Context appContext = RecApplication.getContext();

	private final String KEYS_SAVE_PATH = appContext
			.getString(R.string.keys_save_path);
	private final String KEYS_SWAP_CHANNEL = appContext
			.getString(R.string.keys_swap_channel);
	private final String KEYS_SAVE_MONO = appContext
			.getString(R.string.keys_save_mono);
	private final String KEYS_SAVE_STEREO = appContext
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

}

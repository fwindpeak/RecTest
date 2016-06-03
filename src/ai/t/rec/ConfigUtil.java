package ai.t.rec;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigUtil {

	Context appContext = RecApplication.getContext();

	private final String KEYS_SAVE_PATH = appContext
			.getString(R.string.keys_save_path);

	private static ConfigUtil mInstance;
	private static SharedPreferences mSp = RecApplication.getContext()
			.getSharedPreferences("rec_preference", Context.MODE_PRIVATE);

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
		mSp.edit().putString(KEYS_SAVE_PATH, savePath).apply();
	}

}

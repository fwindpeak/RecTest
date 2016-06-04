package ai.t.rec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

/**
 * Created by fwind on 2016/5/24.
 */

public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(R.menu.pre_menu);
	}

	CheckBoxPreference isRecMono, isRecStereo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		isRecMono = (CheckBoxPreference) findPreference(ConfigUtil.KEYS_SAVE_MONO);
		isRecStereo = (CheckBoxPreference) findPreference(ConfigUtil.KEYS_SAVE_STEREO);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals(ConfigUtil.KEYS_SAVE_STEREO)
				|| key.equals(ConfigUtil.KEYS_SAVE_MONO)) {
			if (isRecMono.isChecked() == false
					&& isRecStereo.isChecked() == false) {
				new AlertDialog.Builder(this).setMessage("不能什么都不保存吧？")
						.setPositiveButton("确定", null).show();
				if(key.equals(ConfigUtil.KEYS_SAVE_STEREO)){
					isRecStereo.setChecked(true);
				}else{
					isRecMono.setChecked(true);
				}
			}
		}

	}

}

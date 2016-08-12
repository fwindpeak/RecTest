package ai.t.rec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity implements View.OnClickListener,
		MyRecorder.RecordCallback {

	private static final int ForSetting = 0;// 打开设置界面标记
	private final int SETTING_REQUESTCODE = 2;

	EditText etLog;
	MyRecorder record;

	Button btnStart, btnStop;
	
	RecBroadcastReceive recBroadcastReceive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etLog = (EditText) findViewById(R.id.editTextLog);
		etLog.setMovementMethod(ScrollingMovementMethod.getInstance());
		btnStart = (Button) findViewById(R.id.buttonStart);
		btnStop = (Button) findViewById(R.id.buttonStop);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
		recBroadcastReceive = new RecBroadcastReceive();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("ai.rec.start");
		intentFilter.addAction("ai.rec.stop");
		registerReceiver(recBroadcastReceive, intentFilter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuClear:
			clearInfo();
			break;
		case R.id.menuDelete:
			FileManager.deleteAllPCMFiles(new InfoCallback() {
				@Override
				public void onUpdateInfo(String msg) {
					printInfo(msg);
				}
			});
			break;
		case R.id.menuSetting:
			Intent intent = new Intent();
			intent.setClass(this, SettingActivity.class);
			startActivityForResult(intent, SETTING_REQUESTCODE);
			break;
		case R.id.menuAbout:
			showAbout();
			break;
		case R.id.menuExit:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	void printInfo(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				etLog.append(msg + "\n");
				etLog.setSelection(etLog.getText().length(), etLog.getText()
						.length());
			}
		});

	}

	void clearInfo() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				etLog.setText("");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonStart:
			onStartClick();
			break;
		case R.id.buttonStop:
			onStopClick();
			break;
		}
	}
	
	public void onStartClick(){
		printInfo("开始录音");
		record = new MyRecorder(this);
		record.start();
		btnStart.setClickable(false);
	}
	
	public void onStopClick(){
		record.stop();
		printInfo("结束录音");
		btnStart.setClickable(true);
	}
	

	@Override
	public void onUpdateUI(String msg) {
		printInfo(msg);
	}

	@Override
	public void onStop(String filePath) {
		printInfo(filePath);
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		String version = "1.0";
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	void showAbout() {
		new AlertDialog.Builder(this).setTitle(getString(R.string.menu_about))
				.setMessage("录音测试工具 v" + getVersion())
				.setPositiveButton("确定", null).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETTING_REQUESTCODE) {
			ConfigUtil.getInstance().showInfo(new InfoCallback() {

				@Override
				public void onUpdateInfo(String msg) {
					// TODO Auto-generated method stub
					printInfo(msg);
				}
			});
		}
	}

	public class RecBroadcastReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals("ai.rec.start")){
				onStartClick();
			}else if(action.equals("ai.rec.stop")){
				onStopClick();
			}
		}

	}

}

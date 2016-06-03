package ai.t.rec;

import android.app.Application;
import android.content.Context;

public class RecApplication extends Application {
	
	static Context mContext;
	public static Context getContext(){
		if(mContext == null){
			 throw new RuntimeException("RecApplication Context is null");
		}
		return mContext;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
	}
	
	

}

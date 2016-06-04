package ai.test.rec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
	
	public static String getTimeString(){
		String timeStr="";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA);
		timeStr  = sdf.format(new Date());
		
		return timeStr;
	}

}

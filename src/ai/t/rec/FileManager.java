package ai.t.rec;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by fwind on 2016/5/21.
 */
public class FileManager {
    public static String getSavePath() {
        final String dirPath = ConfigUtil.getInstance().getSavePath();
        File pathFile = new File(dirPath);
        if (pathFile.isDirectory() == false) {
            pathFile.mkdirs();
        }
        return dirPath;
    }

    public static void deleteAllPCMFiles( InfoCallback callback) {
        File dirFile = new File(getSavePath());
        File[] pcmFIles = dirFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".pcm")||filename.endsWith(".wav")) {
                    return true;
                }
                return false;
            }
        });
        for (File file : pcmFIles) {
            callback.onUpdateInfo("del->"+file.getAbsolutePath());
            file.delete();
        }

    }
}

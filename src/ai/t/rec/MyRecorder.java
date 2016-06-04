package ai.t.rec;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

class MyRecorder {

	final int frequence = 16 * 1000;
	final int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
	final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	boolean isRecording = false;
	File audioFile_rec, audioFile_echo, audioFile_stereo;
	RecordCallback callback;
	Thread recordThread = new Thread(new Runnable() {
		@Override
		public void run() {
			fwindlog("doInBackground start");
			isRecording = true;
			try {
				// 开通输出流到指定的文件
				BufferedOutputStream bos_rec = new BufferedOutputStream(
						new FileOutputStream(audioFile_rec));
				BufferedOutputStream bos_echo = new BufferedOutputStream(
						new FileOutputStream(audioFile_echo));
				// 添加_立体声_rain
				BufferedOutputStream bos_stereo = new BufferedOutputStream(
						new FileOutputStream(audioFile_stereo));

				int bufferSize = frequence * 2;
				// 实例化AudioRecord
				AudioRecord record = new AudioRecord(
						MediaRecorder.AudioSource.MIC, frequence,
						channelConfig, audioEncoding, bufferSize);
				// 定义缓冲
				byte[] buffer = new byte[bufferSize];
				byte[] buffer_rec = new byte[bufferSize / 2];
				byte[] buffer_echo = new byte[bufferSize / 2];
				// 开始录制
				record.startRecording();
				int r = 0; // 存储录制进度
				// 定义循环，根据isRecording的值来判断是否继续录制
				WaveHeader head = new WaveHeader(1);
				byte[] whead = head.getHeader();
				int pcmSize = 0; // pcm数据的长度
				bos_echo.write(whead);
				bos_rec.write(whead);

				bos_stereo.write(whead);

				while (isRecording) {
					int bufferReadResult = record
							.read(buffer, 0, buffer.length);
					pcmSize += bufferReadResult / 2;
					for (int i = 0; i < bufferReadResult; i += 4) {
						int n = i / 2;
						buffer_rec[n] = buffer[i];
						buffer_rec[n + 1] = buffer[i + 1];
						buffer_echo[n] = buffer[i + 2];
						buffer_echo[n + 1] = buffer[i + 3];
					}

					bos_rec.write(buffer_rec, 0, bufferReadResult / 2);
					bos_echo.write(buffer_echo, 0, bufferReadResult / 2);
					bos_stereo.write(buffer, 0, bufferReadResult);

					callback.onUpdateUI(r + "");
					r++; // 自增进度值
				}

				head.setDataSize(pcmSize);

				// 录制结束
				record.stop();
				Log.v("audioFile_rec.length:", "::" + audioFile_rec.length());

				bos_rec.flush();
				bos_echo.flush();
				bos_stereo.flush();
				bos_rec.close();
				bos_echo.close();
				bos_stereo.close();

				// 写文件头的数据长度
				head.setDataSize(pcmSize);
				whead = head.getHeader();
				RandomAccessFile raf_rec = new RandomAccessFile(audioFile_rec,
						"rw");
				raf_rec.seek(0);
				raf_rec.write(whead);
				raf_rec.close();

				RandomAccessFile raf_echo = new RandomAccessFile(
						audioFile_echo, "rw");
				raf_echo.seek(0);
				raf_echo.write(whead);
				raf_echo.close();

				RandomAccessFile raf_stereo = new RandomAccessFile(
						audioFile_stereo, "rw");
				raf_stereo.seek(0);

				head.setChannels((short) 2);
				head.setDataSize(pcmSize * 2);
				whead = head.getHeader();

				raf_stereo.write(whead);
				raf_stereo.close();

				callback.onStop(audioFile_rec.getAbsolutePath() + "\n"
						+ audioFile_echo.getAbsolutePath() + "\n"
						+ audioFile_stereo.getAbsolutePath());
			} catch (Exception e) {
				// TODO: handle exception
				fwindlog(e);
			}
		}
	});

	public MyRecorder(RecordCallback callback) {

		this.callback = callback;

		final String dirPath = FileManager.getSavePath();

		callback.onUpdateUI("dirPath:" + dirPath);

		String ctm = Util.getTimeString();
		this.audioFile_rec = new File(dirPath + "/" + ctm + "_rec.wav");
		this.audioFile_echo = new File(dirPath + "/" + ctm + "_echo.wav");
		this.audioFile_stereo = new File(dirPath + "/" + ctm + "_stereo.wav");
	}

	/**
	 * 停止录音
	 */
	public void stop() {
		isRecording = false;
	}

	/**
	 * 开始录音
	 */
	public void start() {
		recordThread.start();
	}

	void fwindlog(final String msg) {
		Log.e("fwindlog", msg);
	}

	void fwindlog(final Exception e) {
		Log.e("fwindlog", Log.getStackTraceString(e));
	}

	public interface RecordCallback {
		/**
		 * 更新UI消息
		 * 
		 * @param msg
		 *            消息字符串
		 */
		void onUpdateUI(final String msg);

		/**
		 * 录音结束
		 * 
		 * @param filePath
		 *            录音文件存放路径
		 */
		void onStop(final String filePath);
	}

}
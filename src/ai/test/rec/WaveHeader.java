package ai.test.rec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WaveHeader {

    private final char fileID[] = {'R', 'I', 'F', 'F'};
    private int fileLength;
    private char wavTag[] = {'W', 'A', 'V', 'E'};
    private char FmtHdrID[] = {'f', 'm', 't', ' '};
    private int FmtHdrLeth;
    private short FormatTag;
    private short Channels;
    private int SamplesPerSec;
    private int AvgBytesPerSec;
    private short BlockAlign;
    private short BitsPerSample;
    private char DataHdrID[] = {'d', 'a', 't', 'a'};
    private int DataHdrLeth;

    public WaveHeader(int PCMSize) {
        fileLength = PCMSize + (44 - 8);
        FmtHdrLeth = 16;
        BitsPerSample = 16;
        Channels = 1;
        FormatTag = 0x0001;
        SamplesPerSec = 16000;
        BlockAlign = (short) (Channels * BitsPerSample / 8);
        AvgBytesPerSec = BlockAlign * SamplesPerSec;
        DataHdrLeth = PCMSize;
    }

    /**
     * 立体声通道设置 getHead前调用.
     * @param c channels值
     */
    public void setChannels(short c){
        Channels = c;
    }

    public void setDataSize(int size) {
        DataHdrLeth = size;
        fileLength = size + (44 - 8);
    }

    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WriteChar(bos, fileID);
        WriteInt(bos, fileLength);
        WriteChar(bos, wavTag);
        WriteChar(bos, FmtHdrID);
        WriteInt(bos, FmtHdrLeth);
        WriteShort(bos, FormatTag);
        WriteShort(bos, Channels);
        WriteInt(bos, SamplesPerSec);
        WriteInt(bos, AvgBytesPerSec);
        WriteShort(bos, BlockAlign);
        WriteShort(bos, BitsPerSample);
        WriteChar(bos, DataHdrID);
        WriteInt(bos, DataHdrLeth);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }

    private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
        byte[] mybyte = new byte[2];
        mybyte[1] = (byte) ((s << 16) >> 24);
        mybyte[0] = (byte) ((s << 24) >> 24);
        bos.write(mybyte);
    }


    private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] = (byte) (n >> 24);
        buf[2] = (byte) ((n << 8) >> 24);
        buf[1] = (byte) ((n << 16) >> 24);
        buf[0] = (byte) ((n << 24) >> 24);
        bos.write(buf);
    }

    private void WriteChar(ByteArrayOutputStream bos, char[] id) {
        for (char c : id) {
            bos.write(c);
        }
    }
}
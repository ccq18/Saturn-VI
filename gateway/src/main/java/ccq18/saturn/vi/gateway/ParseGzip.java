package ccq18.saturn.vi.gateway;

import ccq18.saturn.vi.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.*;

@Slf4j
@Data
public class ParseGzip {
//    private List<Byte> readdata = new LinkedList<>();
    private static String encoding = "UTF-8";

    private int read(InputStream is) throws IOException {
        int readc = is.read();
//        if (readc != -1) {
//            readdata.add((byte)readc);
//        }
        return readc;
    }

    /**
     * 读取一行数据
     *
     * @param is
     * @return
     * @throws IOException
     */
    public String readLine(InputStream is) throws IOException {
        log.info("readLine ");
        List<Byte> lineByteList = new ArrayList<Byte>();
        byte readByte = -1;

        do {
            try {
                readByte = (byte) read(is);
            } catch (SocketException e) {
                return null;
            }

            if (readByte == -1) {
                return null;
            }
            ///r/n
            if (readByte == 13) {
                byte nextByte = (byte) read(is);
                if (nextByte == 10) {
                    break;
                } else {
                    lineByteList.add(Byte.valueOf(readByte));
                    lineByteList.add(Byte.valueOf(nextByte));
                }
                ///n
            } else if (readByte == 10) {
                break;
            } else {
                lineByteList.add(Byte.valueOf(readByte));
                log.info("r:{}", readByte);
            }

        } while (readByte != 10);// 读取到最后一个"\n"换行的字符
        log.info("rd");
        if (lineByteList.size() == 0) {
            return "";
        }
        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();
        String line = new String(tmpByteArr, encoding);
        return line;
    }

    public boolean isBlank(String sLength) {
        if (sLength == null) {
            return true;
        }

        if (sLength.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public void readGzipbody(InputStream is, OutputStream clientout) throws IOException {
        int chunk = 0;
        do {
            chunk = getChunkSize(is,clientout);
            if(chunk>0){
                log.info("read chunk:{}", chunk);
                HttpUtil.readAndPrint(chunk, is, clientout);
                log.info("readed chunk:{}", chunk);
            }else {
                log.info("chunk end");
                break;
            }

//
//            List<Byte> bodyByteList = new ArrayList<Byte>();
//            byte readByte = 0;
//            int count = 0;
//
//            while (count < chunk) {  // 读取消息体，最多读取chunk个byte
//                readByte = (byte) read(is);
//                char c = '.';
//                if (readByte >= 32 && readByte <= 95) {
//                    c = (char) readByte;
//                }
////                log.info("rw:{}", c);
//                bodyByteList.add(Byte.valueOf(readByte));
//                count++;
//            }
//            log.info("readed chunk:{}", chunk);

        } while (chunk > 0);
    }


    /**
     * 获取压缩包块的大小
     *
     * @param is
     * @return
     * @throws IOException
     */
    public int getChunkSize(InputStream is,OutputStream clientout) throws IOException {
        String sLength = readLine(is).trim();
        HttpUtil.println(clientout,sLength);
        log.info("print size:{}", sLength);
        if (isBlank(sLength)) {  // 字符串前面有可能会是一个回车换行。
            // 读了一个空行，继续往下读取一行。
            sLength = readLine(is).trim();
            HttpUtil.println(clientout,sLength);
            log.info("print size:{}", sLength);
        }
        if (sLength.length() < 4) {
            sLength = 0 + sLength;
        }
        // 把16进制字符串转化为Int类型
        int length = Integer.valueOf(sLength, 16);
        return length;
    }


}

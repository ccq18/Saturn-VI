package ccq18.saturn.vi;

import ccq18.saturn.vi.model.Request;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpUtil {

    private static String encoding = "UTF-8";

    /**
     * 读取一行数据
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readLine(InputStream is) throws IOException {
        List<Byte> lineByteList = new ArrayList<Byte>();
        byte readByte = -1;

        do {
            try {
                readByte = (byte) is.read();
            } catch (SocketException e) {
                return null;
            }

            if (readByte == -1) {
                return null;
            }
            ///r/n
            if (readByte == 13) {
                byte nextByte = (byte) is.read();
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
//                log.info("r:{}", readByte);
            }

        } while (readByte != 10);// 读取到最后一个"\n"换行的字符
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

    public static void printheader(OutputStream out, String http, Map<String, String> reqHeaders) throws IOException {
        println(out, http);

        for (Map.Entry<String, String> entry : reqHeaders.entrySet()) {
            String value = entry.getKey() + ": " + entry.getValue();
            log.info(value);
            println(out, value);
        }
        println(out, "");
    }

    public static void println(OutputStream out, String s) throws IOException {
        out.write(s.getBytes());
        byte[] cs = { '\n'};
        out.write(cs);
    }

    public static Request readRequest(InputStream buf) throws IOException {
        String http = readLine(buf);
        log.info("rr:{}", http);
        List<String> headerStr = readHeaderStr(buf);
        Map<String, String> headers = parseHeaderStr(headerStr);
        String contents = null;
        if (headers.containsKey("Content-Length")) {
            int len = Integer.parseInt(headers.get("Content-Length").trim());
            byte[] bytes = new byte[len];
            buf.read(bytes);
            contents = new String(bytes);
        }

        return new Request(http, headers, contents);
    }

    public static List<String> readHeaderStr(InputStream buf) throws IOException {
        List<String> headerStr = new ArrayList<>();
        while (true) {
            //接收从客户端发送过来的数据
            String str = readLine(buf);

            if (str == null) {
                break;
            }
            if ("".equals(str)) {
                break;
            }
            headerStr.add(str);
        }
        log.info("rr end");

        return headerStr;
    }


    public static Map<String, String> parseHeaderStr(List<String> headerStr) {
        Map<String, String> headers = new HashMap<>();
        for (String str : headerStr
        ) {
            int symbol = str.indexOf(":");
            if (symbol > 0) {
                String key = str.substring(0, symbol);
                String value = str.substring(symbol + 1).trim();
                log.info("k:{},v:{},", key, value);
                headers.put(key, value);
            }
        }

        return headers;
    }


    public static void readAndPrint(int len, InputStream serverIs, OutputStream clientout) throws IOException {
        int chunk = 40960;
        byte[] bytes = new byte[chunk];
        int readlen;
        log.info("readAndPrint  len:{}", len);
        while ((readlen = serverIs.read(bytes, 0, Math.min(len, chunk))) > 0) {
            len -= readlen;
            log.info("send resp content  len:{},readlen:{}", len, readlen);
            //返回到客户端
            clientout.write(bytes, 0, readlen);
            if (len <= 0) {
                log.info("sended nowlen end ");
                break;
            }
        }
    }
}

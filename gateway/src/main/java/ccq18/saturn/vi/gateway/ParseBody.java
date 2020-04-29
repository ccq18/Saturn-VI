package ccq18.saturn.vi.gateway;

import lombok.extern.slf4j.Slf4j;
import ccq18.saturn.vi.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
public class ParseBody {
    public void parseAuto(InputStream serverIs, OutputStream clientout) throws IOException {
        byte[] bytes = new byte[1024];
        int readlen;
        while ((readlen = serverIs.read(bytes)) > 0) {
            log.info("send resp content ,readlen:{}", readlen);
            //返回到客户端
            clientout.write(bytes, 0, readlen);
        }
    }

    public void parse(InputStream serverIs, OutputStream clientout, Map<String, String> respHeader) throws IOException {

        if (respHeader.containsKey("Content-Length")) {
            log.info("has Content-Length");
            int len = Integer.parseInt(respHeader.get("Content-Length").trim());
            log.info("send resp content  len:{}", len);

            HttpUtil.readAndPrint(len, serverIs, clientout);
            log.info("sended resp content ");
        } else {
            parseAuto(serverIs, clientout);
        }

    }
}

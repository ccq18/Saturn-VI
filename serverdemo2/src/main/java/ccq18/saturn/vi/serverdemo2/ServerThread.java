package ccq18.saturn.vi.serverdemo2;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// nc ip port

/**
 * 该类为多线程类，用于服务端
 */
public class ServerThread implements Runnable {

    private Socket client = null;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        PrintStream out = null;
        try {
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //解析请求头
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String http = buf.readLine();
            List<String> headerStr = readHeaderStr(buf);
            Map<String, String> headers = parseHeaderStr(headerStr);
            String contents = null;
            //有 contents 时解析 contents
            if (headers.containsKey("Content-Length")) {
                int len = Integer.parseInt(headers.get("Content-Length").trim());
                if (len > 0) {
                    char[] bytes = new char[len];
                    buf.read(bytes);
                    contents = new String(bytes);
                }

            }

            System.out.println("request:");
            System.out.println(http);
            System.out.println(headers);
            System.out.println(contents);
            if (headers.size() <= 0) {
                return;
            }
            String[] resp = new String[]{
                    "HTTP/1.1 200 ok",
                    "Content-Type: text/html; charset=utf-8",
                    "",
                    "<h1>hello</h1>",
            };
            for (String i : resp) {
                out.println(i);
                System.out.println("resp:" + i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static List<String> readHeaderStr(BufferedReader buf) throws IOException {
        List<String> headerStr = new ArrayList<>();
        while (true) {
            //接收从客户端发送过来的数据
            String str = buf.readLine();
            if (str == null) {
                break;
            }
            if ("".equals(str)) {
                break;
            }
            headerStr.add(str);
        }
        return headerStr;
    }

    public static Map<String, String> parseHeaderStr(List<String> headerStr) {
        Map<String, String> headers = new HashMap<>();
        for (String header : headerStr) {
            String[] heds = header.split(":");
            if (heds.length == 2) {
                headers.put(heds[0], heds[1]);
            }
        }
        return headers;
    }


}
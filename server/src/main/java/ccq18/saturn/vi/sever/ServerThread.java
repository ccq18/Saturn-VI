package ccq18.saturn.vi.sever;

import ccq18.saturn.vi.sever.protocol.http.HttpProtocol;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

// nc ip port

/**
 * 该类为多线程类，用于服务端
 */
@Slf4j
public class ServerThread implements Runnable {

    private Socket client;
    private HttpProtocol httpProtocol;

    public ServerThread(Socket client, HttpProtocol httpProtocol) {
        this.client = client;
        this.httpProtocol = httpProtocol;
    }

    @Override
    public void run() {
        try {
//            this.getClass().getResourceAsStream();
            httpProtocol.handle(client.getOutputStream(), client.getInputStream());
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}


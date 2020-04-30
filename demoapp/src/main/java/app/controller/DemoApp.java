package app.controller;

import ccq18.saturn.vi.sever.Server;

public class DemoApp {
    public static void main(String[] args) throws Exception {
        int port = 2006;
        int nThreads = 20;
        Server.run(port, nThreads, "app.controller",NotFound.class);
    }
}

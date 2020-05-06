package app.controller;

import ccq18.saturn.vi.sever.Server;
import ccq18.saturn.vi.sever.summer.SummerApp;

public class DemoApp {
    public static void main(String[] args) throws Exception {
        int port = 2006;
        int nThreads = 20;
        SummerApp.run(port, nThreads, "app.controller",NotFound.class);
    }
}

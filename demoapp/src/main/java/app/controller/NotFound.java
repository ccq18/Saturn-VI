package app.controller;

import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.sever.RequestHandle;

public class NotFound implements RequestHandle {

    @Override
    public String handle(Request request) {
        return "404 not found";
    }
}

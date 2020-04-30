package app.controller;

import ccq18.saturn.vi.HttpStatus;
import ccq18.saturn.vi.MediaType;
import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.model.Response;
import ccq18.saturn.vi.sever.annotation.Controller;
import ccq18.saturn.vi.sever.annotation.RequestMapping;
import ccq18.saturn.vi.sever.annotation.RequestMethod;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Singleton
public class HelloController {

    @AllArgsConstructor
    @Data
    static class Obj {
        private String abc;
        private String name;
    }

    @RequestMapping(path = "/pickup-codes", method = {RequestMethod.GET})
    public String handle(Request request) {
        return "<h1>hello</h1>\n" +
                "<a href=\"/\">index</a>\n";

    }

    @RequestMapping(path = "/", method = {RequestMethod.GET})
    public Obj index(Request request) {
        return new Obj("hello", "hi");

    }

    @RequestMapping(path = "/hello", method = {RequestMethod.GET})
    public Response hello(Request request) {
        return new Response(HttpStatus.OK, MediaType.TEXT_HTML_UTF8, "<h1>hello</h1>");
    }
}

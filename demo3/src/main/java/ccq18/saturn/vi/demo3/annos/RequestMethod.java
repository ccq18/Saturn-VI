package ccq18.saturn.vi.demo3.annos;

import lombok.Getter;

@Getter
public enum RequestMethod {
    /**
     *
     */
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;
    ;
}
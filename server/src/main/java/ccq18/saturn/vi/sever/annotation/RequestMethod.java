package ccq18.saturn.vi.sever.annotation;

import lombok.AllArgsConstructor;
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
package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

import java.util.Objects;

@Service
@RequestHandler(path="echo")
public class EchoRequestHandler implements IRequestHandler {

    @Override
    public String response ( Request request ) {

        String result = null;
        String[] endpointParsed = request.getEndpointParsed();

        assert Objects.requireNonNull(endpointParsed).length == 3;
        result = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + endpointParsed[2].length() + "\r\n\r\n" + endpointParsed[2];

        return result;
    }

}

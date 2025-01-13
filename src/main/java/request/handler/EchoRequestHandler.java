package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

import java.util.Objects;

@Service
@RequestHandler(path="echo")
public class EchoRequestHandler implements IRequestHandler {

    @Override
    public String getResponse ( Request request ) {

        String result = null;
        String[] endpointParsed = request.getEndpointParsed();

        if ( endpointParsed.length <= 2 )
            return "HTTP/1.1 400 Bad Request: No echo content given\r\n\r\n";

        result = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + endpointParsed[2].length() + "\r\n\r\n" + endpointParsed[2];

        return result;
    }

    @Override
    public String postResponse ( Request request ) {
        return null;
    }

}

package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

@Service
@RequestHandler(path="user-agent")
public class UserAgentRequestHandler implements IRequestHandler {

    @Override
    public String getResponse ( Request request ) {

        String userAgentContent = request.getUserAgentParsed()[1];

        return "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgentContent.length() + "\r\n\r\n" + userAgentContent;

    }

    @Override
    public String postResponse ( Request request ) {
        return null;
    }

}
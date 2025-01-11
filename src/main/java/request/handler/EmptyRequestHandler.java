package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

@Service
@RequestHandler(path="/")
public class EmptyRequestHandler implements IRequestHandler {

    @Override
    public String response( Request request ) {
        return "HTTP/1.1 200 OK\r\n\r\n";
    }
}

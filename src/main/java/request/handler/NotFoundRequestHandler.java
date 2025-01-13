package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

@Service
@RequestHandler(isNotFoundHandler = "1")
public class NotFoundRequestHandler implements IRequestHandler {

    @Override
    public String getResponse( Request request ) {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

    @Override
    public String postResponse (Request request ) {
        return null;
    }

}

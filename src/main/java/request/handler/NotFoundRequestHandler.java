package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import request.Request;
import request.Response;

@Service
@RequestHandler(isNotFoundHandler = "1")
public class NotFoundRequestHandler implements IRequestHandler {

    @Override
    public Response getResponse( Request request ) {
        return new Response(HttpStatus.NOT_FOUND);
    }

    @Override
    public Response postResponse ( Request request ) {
        return null;
    }

}

package request.handler;

import request.Request;
import request.Response;

public interface IRequestHandler {

    public Response getResponse ( Request request );

    public Response postResponse ( Request request );

}

package request.handler;

import request.Request;

public interface IRequestHandler {

    public String getResponse ( Request request );

    public String postResponse ( Request request );

}

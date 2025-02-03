package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import request.Request;
import request.Response;

import java.util.HashMap;

@Service
@RequestHandler(path="user-agent")
public class UserAgentRequestHandler implements IRequestHandler {

    @Override
    public Response getResponse ( Request request ) {

        String userAgentContent = request.getHeader().get("User-Agent");

        return new Response(
                HttpStatus.OK,
                new HashMap<>(){{
                    put("Content-Type", String.valueOf(MediaType.TEXT_PLAIN));
                    put("Content-Length", String.valueOf(userAgentContent.length()));
                }},
                userAgentContent
        );
    }

    @Override
    public Response postResponse ( Request request ) {
        return null;
    }

}
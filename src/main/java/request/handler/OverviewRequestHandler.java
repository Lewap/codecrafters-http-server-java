package request.handler;

import annotations.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import request.Request;
import request.Response;
import util.OverviewHtml;

import java.util.HashMap;

@Service
@RequestHandler(path="overview")
public class OverviewRequestHandler implements IRequestHandler {

    private static final Logger logger = LogManager.getLogger(OverviewRequestHandler.class);

    @Override
    public Response getResponse(Request request) {

        String body = OverviewHtml.getHtml();

        return new Response(
                HttpStatus.OK,
                new HashMap<>(){{
                    put("Content-Type", String.valueOf(MediaType.TEXT_HTML));
                    put("Content-Length", String.valueOf(body.length()));
                }},
                body
        );
    }

    @Override
    public Response postResponse(Request request) {
        return null;
    }
}

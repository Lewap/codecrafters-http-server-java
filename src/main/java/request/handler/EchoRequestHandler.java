package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import request.Request;
import request.Response;

import server.SupportedContentEncoding;

import java.util.HashMap;
import java.util.Map;

@Service
@RequestHandler(path="echo")
public class EchoRequestHandler implements IRequestHandler {

    @Override
    public Response getResponse ( Request request ) {

        Response response;

        String[] endpointParsed = request.getEndpointParsed();
        Map<String,String> requestHeader = request.getHeader();

        String[] acceptEncodingParsed = requestHeader.get("Accept-Encoding").split(",");
        for (int i=0; i<acceptEncodingParsed.length; i++) {
            acceptEncodingParsed[i] = acceptEncodingParsed[i].replace(" ","");
        }

        Map<String,String> responseHeaders = new HashMap<>();

        if ( endpointParsed.length <= 2 )
            return new Response(HttpStatus.BAD_REQUEST, "No echo content given");

        responseHeaders.put("Content-Type", String.valueOf(MediaType.TEXT_PLAIN));
        responseHeaders.put("Content-Length", String.valueOf(endpointParsed[2].length()));

        if ( SupportedContentEncoding.isSupported(acceptEncodingParsed) ) {
            responseHeaders.put("Content-Encoding", SupportedContentEncoding.GZIP.getValue());
        }

        response = new Response(
                HttpStatus.OK,
                responseHeaders,
                endpointParsed[2]
        );

        return response;
    }

    @Override
    public Response postResponse ( Request request ) {
        return null;
    }

}

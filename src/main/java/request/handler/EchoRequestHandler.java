package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import request.Request;
import request.Response;

import server.SupportedContentEncoding;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

@Service
@RequestHandler(path="echo")
public class EchoRequestHandler implements IRequestHandler {

    private void gzip(InputStream is, OutputStream os, int bufferSize) throws IOException {
        GZIPOutputStream gzipOs = new GZIPOutputStream(os);
        byte[] buffer = new byte[bufferSize];
        int bytesRead = 0;
        while ((bytesRead = is.read(buffer)) > -1) {
            gzipOs.write(buffer, 0, bytesRead);
        }
        gzipOs.close();
    }

    @Override
    public Response getResponse ( Request request ) {

        Response response;

        String[] endpointParsed = request.getEndpointParsed();
        Map<String,String> requestHeader = request.getHeader();

        Map<String,String> responseHeaders = new HashMap<>();

        if ( endpointParsed.length <= 2 )
            return new Response(HttpStatus.BAD_REQUEST, "No echo content given");

        responseHeaders.put("Content-Type", String.valueOf(MediaType.TEXT_PLAIN));

        String[] acceptEncodingParsed = null;

        String body = endpointParsed[2];
        System.out.println("body as is: " + body);

        if ( requestHeader.get("Accept-Encoding") != null ) {
            acceptEncodingParsed = requestHeader.get("Accept-Encoding").split(",");
            if ( SupportedContentEncoding.isSupported(acceptEncodingParsed) ) {
                responseHeaders.put("Content-Encoding", SupportedContentEncoding.GZIP.getValue());

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    gzip(new ByteArrayInputStream(body.getBytes()), os, body.length());
                } catch (IOException e) {
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Error encoding input: " + e.getMessage());
                }

                body = os.toString();

                System.out.println("body compressed: " + body);
            }
        }

        responseHeaders.put("Content-Length", String.valueOf(body.length()));

        response = new Response(
                HttpStatus.OK,
                responseHeaders,
                body
        );

        return response;
    }

    @Override
    public Response postResponse ( Request request ) {
        return null;
    }

}

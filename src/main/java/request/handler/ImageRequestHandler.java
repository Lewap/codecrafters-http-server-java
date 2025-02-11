package request.handler;

import annotations.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import request.Request;
import request.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Service
@RequestHandler(path="image")
public class ImageRequestHandler implements IRequestHandler {

    private static final Logger logger = LogManager.getLogger(ImageRequestHandler.class);

    @Override
    public Response getResponse(Request request) {

        Response response = null;

        String[] endpointParsed = request.getEndpointParsed();

        if ( endpointParsed.length <= 2 )
            return new Response(HttpStatus.BAD_REQUEST, "No image name given");

        String filename = endpointParsed[2];

        InputStream imageInStr = this.getClass().getResourceAsStream("/WEB-INF/" + filename);

        if (imageInStr == null)
            return new Response(HttpStatus.NOT_FOUND);

        try {

            byte[] image = imageInStr.readAllBytes();
            imageInStr.close();

            response = new Response(
                    HttpStatus.OK,
                    new HashMap<>() {{
                        put("Content-Type", String.valueOf(MediaType.IMAGE_JPEG));
                        put("Content-Length", String.valueOf(image.length));
                    }},
                    image
            );
        } catch (IOException e) {
            logger.error (e);
        }

        return response;
    }

    @Override
    public Response postResponse(Request request) {
        return null;
    }

}

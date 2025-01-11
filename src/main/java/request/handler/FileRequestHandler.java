package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

import javax.imageio.IIOException;
import java.io.*;
import java.util.Arrays;

@Service
@RequestHandler(path="files")
public class FileRequestHandler implements IRequestHandler {

    public String response ( Request request ) {

        String dir = null;
        String fileSize = null;
        String filename = request.getEndpointParsed()[2];
        String fileLine = null;

        for (int i=0; i < request.getArgs().length; i++) {
            String key = request.getArgs()[i];
            if ("--directory".equals(key)) {
                dir = request.getArgs()[i+1];
                break;
            }
        }

        File file = new File(dir,filename);

        try {
            fileSize = String.valueOf(file.length());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            fileLine = reader.readLine();
            reader.close();
        } catch (IOException e) {
            return "HTTP/1.1 404 Not Found\r\n\r\n";
            //TODO: would be better to implement this using an exception and notify the dispatcher to revert to the NotFoundRequestHandler
        }

        return "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\n\r\nContent-Length: " + fileSize + "\r\n\r\n" + fileLine;

    }

}

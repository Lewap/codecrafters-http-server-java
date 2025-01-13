package request.handler;

import annotations.RequestHandler;
import org.springframework.stereotype.Service;
import request.Request;

import java.io.*;

@Service
@RequestHandler(path="files")
public class FileRequestHandler implements IRequestHandler {

    public String getDirFromArgs ( Request request ) {

        String result = null;

        for (int i=0; i < request.getArgs().length; i++) {
            String key = request.getArgs()[i];
            if ("--directory".equals(key)) {
                result = request.getArgs()[i+1];
                break;
            }
        }
        return result;
    }

    public String getResponse ( Request request ) {

        String dir = getDirFromArgs( request );
        String fileSize;
        String filename = request.getEndpointParsed()[2];
        String fileLine;

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

    @Override
    public String postResponse ( Request request ) {

        String dir = getDirFromArgs( request );
        String filename = null;
        String fileContent = request.getBody();

        String[] requestEndpointParsed = request.getEndpointParsed();

        if ( requestEndpointParsed.length > 2 )
            filename = request.getEndpointParsed()[2];
        else
            return "HTTP/1.1 400 Bad Request: No filename given\r\n\r\n";

        File file = new File (dir,filename);

        try {
            boolean created = file.createNewFile();
            if ( !created ) {
                System.out.println("File exists, deleting and creating new");
                file.delete();
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "HTTP/1.1 201 Created\r\n\r\n";
    }

}

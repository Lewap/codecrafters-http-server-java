package request.handler;

import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.util.HashMap;

import annotations.RequestHandler;

import request.Request;
import request.Response;

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

    @Override
    public Response getResponse (Request request ) {

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
            return new Response(HttpStatus.NOT_FOUND);
            //TODO: would be better to implement this using an exception and notify the dispatcher to revert to the NotFoundRequestHandler
        }

        return new Response (
                HttpStatus.OK,
                new HashMap<>(){{
                    put("Content-Type", String.valueOf(MediaType.APPLICATION_OCTET_STREAM));
                    put("Content-Length",fileSize);
                }},
                fileLine
        );

    }

    @Override
    public Response postResponse ( Request request ) {

        String dir = getDirFromArgs( request );
        String filename = null;
        String fileContent = request.getBody();

        String[] requestEndpointParsed = request.getEndpointParsed();

        if ( requestEndpointParsed.length > 2 )
            filename = request.getEndpointParsed()[2];
        else {
            return new Response(HttpStatus.BAD_REQUEST, "No filename given");
        }

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

        return new Response(HttpStatus.CREATED);
    }

}

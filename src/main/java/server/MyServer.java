package server;

import request.Request;
import request.Response;
import request.dispatcher.RequestDispatcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private ServerSocket serverSocket;
    private static String[] args;

    public MyServer ( String[] args ) {
        MyServer.args = args;
    }

    public void start (int port) {

        try {
            serverSocket = new ServerSocket(port);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            while (true) {
                new connectionHandler (serverSocket.accept()).start();
                System.out.println("New connection received " + java.time.LocalDateTime.now());
            }

        } catch (IOException ex) {
            System.out.println("Server start IO Exception: " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    public void stop () {

        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Server stop IO Exception: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private static class connectionHandler extends Thread {

        private final Socket clientSocket;

        public connectionHandler (Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run () {

            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();
                //the PrintWriter couldn't handle writing a byte array
                //PrintWriter out = new PrintWriter(clientSocketOutputStream, true);

                String inputLine;
                List<String> requestLines = new ArrayList<>();
                Request request = null;
                RequestDispatcher requestDispatcher = null;

                while ((inputLine = in.readLine()) != null) {

                    requestLines.add(inputLine);

                    if ( inputLine.length() == 0 ) { //the final line of the Request Headers

                        request = new Request( requestLines, MyServer.args  );

                        String cl = request.getHeader().get("Content-Length");
                        int contentLength = Integer.parseInt(cl==null?"0":cl);

                        if ( contentLength > 0) {

                            char[] bodyChars = new char[contentLength];

                            int charsRead = in.read(bodyChars, 0, contentLength);
                            String body = new String(bodyChars, 0, charsRead);

                            request.setBody(body);

                        }

                        requestDispatcher = new RequestDispatcher();

                        Response response = requestDispatcher.invokeRequestHandler(request);

                        out.write(response.toString().getBytes(StandardCharsets.UTF_8));

                        byte[] bodyAsByteArray = response.getBodyAsByteArray();

                        //for writing the body as not changed byte array - needed for gzip compression
                        if ( bodyAsByteArray != null )
                            out.write(bodyAsByteArray);

                    }

                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("connectionHandler IO Exception: " + ex.getMessage());
                throw new RuntimeException(ex);
            }

        }
    }
}
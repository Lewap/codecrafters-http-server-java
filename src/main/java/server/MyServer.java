package server;

import request.Request;
import request.dispatcher.RequestDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
            System.out.println("accepted new connection on a new thread");

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                List<String> requestLines = new ArrayList<>();
                Request request = null;
                RequestDispatcher requestDispatcher = null;

                while ((inputLine = in.readLine()) != null) {
                    //
                    //System.out.println("READ request line " + inputLine + " length = " + inputLine.length());
                    requestLines.add(inputLine);

                    if ( inputLine.length() == 0 ) { //the final line of the Request Headers

                        request = new Request( requestLines, MyServer.args  );

                        int contentLength = request.getContentLength();
                        System.out.println("content length " + contentLength);

                        if ( contentLength > 0) {

                            char[] bodyChars = new char[contentLength];

                            int charsRead = in.read(bodyChars, 0, contentLength);
                            String body = new String(bodyChars, 0, charsRead);

                            request.setBody(body);

                        }

                        //System.out.println("calling the request dispatcher");
                        requestDispatcher = new RequestDispatcher();

                        out.println(requestDispatcher.invokeRequestHandler(request));

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
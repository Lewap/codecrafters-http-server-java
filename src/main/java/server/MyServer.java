package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(MyServer.class);

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
            //serverSocket.setReuseAddress(true);
            while (true) {
                ConnectionHandler ch = new ConnectionHandler (serverSocket.accept());
                String threadName = ch.getName();
                ch.setName("ConnectionHandler-" + threadName);
                ch.start();
                logger.info("New connection accepted on thread: " + ch.getName());
            }

        } catch (IOException ex) {
            logger.error("Server start IO Exception: " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    public void stop () {

        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.error("Server stop IO Exception: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private static class ConnectionHandler extends Thread {

        private final Socket clientSocket;

        public ConnectionHandler (Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run () {

            try {

                logger.info("Connected client: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();

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

                        logger.info("Request: " + request);

                        requestDispatcher = new RequestDispatcher();

                        Response response = requestDispatcher.invokeRequestHandler(request);

                        out.write(response.toString().getBytes(StandardCharsets.UTF_8));

                        byte[] bodyAsByteArray = response.getBodyAsByteArray();

                        //for writing the body as not changed byte array - needed for gzip compression
                        if ( bodyAsByteArray != null )
                            out.write(bodyAsByteArray);

                        requestLines.clear();

                    }

                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException ex) {
                logger.error("ConnectionHandler IO Exception: " + ex.getMessage());
                throw new RuntimeException(ex);
            }

        }
    }
}
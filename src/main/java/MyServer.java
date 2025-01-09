import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private ServerSocket serverSocket;

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

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public connectionHandler (Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run () {
            System.out.println("accepted new connection");

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);


                String inputLine;

                String[] VERBRequestParsed = null;
                String uri = "/";
                String[] uriParsed = null;
                String userAgentContent = null;

                boolean isEchoUri = false;
                boolean isUserAgentUri = false;

                while ((inputLine = in.readLine()) != null) {
                    //requestLines.add(inputLine);
                    System.out.println("READ request line " + inputLine + " length = " + inputLine.length());

                    if ( inputLine.startsWith("GET") ) {
                        VERBRequestParsed = inputLine.split(" ");
                        uri = VERBRequestParsed[VERBRequestParsed.length-2]; //HTTP/1.1 at the end
                        uriParsed = uri.split("/");
                    }

                    if ( inputLine.startsWith("User-Agent:") ) {
                        userAgentContent = inputLine.split(" ")[1];
                    }

                    if ( inputLine.length() == 0 ) { //the final line of the Request - now perform the response
                        isEchoUri = (uriParsed != null && uriParsed.length == 3 && "echo".equals(uriParsed[1]) );
                        isUserAgentUri = (uriParsed != null && uriParsed.length == 2 && "user-agent".equals(uriParsed[1]) && userAgentContent != null );

                        if ( "/".equals(uri)
                                || isEchoUri
                                || isUserAgentUri
                        ) {
                            if ( isEchoUri ) {
                                out.println("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + uriParsed[2].length() + "\r\n\r\n" + uriParsed[2]);
                            } else if (isUserAgentUri) {
                                out.println("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgentContent.length() + "\r\n\r\n" + userAgentContent);
                            } else
                                out.println("HTTP/1.1 200 OK\r\n\r\n");
                        } else {
                            out.println("HTTP/1.1 404 Not Found\r\n\r\n");
                        }

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

    /*
        String[] uriParsed = null;
        String uri = "/";
        String[] request;
        String clientInput;
        String userAgentContent = null;
        try {

            clientInput = requestLines.get(0); //the request line with GET

            request = clientInput.split(" ");

            uri = request[request.length-2]; //HTTP/1.1 at the end
            uriParsed = uri.split("/");

            for (int i = 0; i < requestLines.size(); i++) {
                if ( requestLines.get(i).startsWith("User-Agent:") ) {
                    userAgentContent = requestLines.get(i).split(" ")[1];
                }
            }

        } catch (Exception e) {
            System.out.println("client input event: " + e.getMessage());
        }

        boolean isEchoUri = (uriParsed != null && uriParsed.length == 3 && "echo".equals(uriParsed[1]) );
        boolean isUserAgentUri = (uriParsed != null && uriParsed.length == 2 && "user-agent".equals(uriParsed[1]) && userAgentContent != null );

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        if ( "/".equals(uri)
                || isEchoUri
                || isUserAgentUri
        ) {
            if ( isEchoUri ) {
                out.println("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + uriParsed[2].length() + "\r\n\r\n" + uriParsed[2]);
            } else if (isUserAgentUri) {
                out.println("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + userAgentContent.length() + "\r\n\r\n" + userAgentContent);
            } else
                out.println("HTTP/1.1 200 OK\r\n\r\n");
        } else {
            out.println("HTTP/1.1 404 Not Found\r\n\r\n");
        }
        out.close();

    } catch (
    IOException e) {
        System.out.println("IOException: " + e.getMessage());
    }

     */

}

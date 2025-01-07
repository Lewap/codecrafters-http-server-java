import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
     try (ServerSocket serverSocket = new ServerSocket(4221);) {

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
       serverSocket.setReuseAddress(true);

       //Socket soc = serverSocket.accept(); // Wait for connection from client.
       Socket socket = serverSocket.accept();
       System.out.println("accepted new connection");

       BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

       String clientInputLine;
       List<String> requestLines = new ArrayList<>();

       while (!(clientInputLine = in.readLine()).equals("")) {
           System.out.println("READING request line and saving to an array " + clientInputLine);
           requestLines.add(clientInputLine);
       }

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

     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}

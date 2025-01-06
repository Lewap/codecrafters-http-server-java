import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class Main {

    private static PrintWriter out;
    private static BufferedReader in;

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

       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       String clientInput = in.readLine();
       String uri = clientInput.split(" ")[1];

       out = new PrintWriter(socket.getOutputStream(), true);
       if ( !"/".equals(uri) ) {
           out.println("HTTP/1.1 404 Not Found\r\n\r\n");
       } else {
           out.println("HTTP/1.1 200 OK\r\n\r\n");
       }
       out.close();

     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}

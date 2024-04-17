package sr;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;

public class server {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String clientInput = "";

    public server(int port) {
        try {
            // Setting up connection
            serverSocket = new ServerSocket(port);
            System.out.println("Created server on port " + port);

            socket = serverSocket.accept();
            System.out.println("Got connection address from " + socket.getInetAddress().toString() + " on port " + port);

            // Initializing input/output streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello!");
            
            boolean requestedFile = false;
            String fileName = "";
            ArrayList<String> eventNames = new ArrayList<String>();
            ArrayList<Integer> eventTimes = new ArrayList<Integer>();
            


            while (!clientInput.equals("bye")) {
                while(!requestedFile && !clientInput.equals("bye")){
                    try {
                        clientInput = in.readUTF();
                        if (clientInput.equals("send")) {
                            System.out.println("Client said:" + clientInput);
                            requestedFile = true;
                        } else {
                            System.out.println("Client event: " + clientInput);
                            out.writeUTF("event received");
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                // Send desired file to client
                if(requestedFile) {
                    System.out.println("Sending schedule file");
                    out.writeUTF("Sending file");
                    File sFile = new File("./sr/schedule.txt");
                    byte[] fileBytes = new byte[(int) sFile.length()];
                    FileInputStream fileIn = new FileInputStream(sFile);
                    BufferedInputStream fileReader = new BufferedInputStream(fileIn);
                    fileReader.read(fileBytes, 0, fileBytes.length);

                    out.write(fileBytes, 0, fileBytes.length);
                    fileIn.close();
                    fileReader.close();
                    requestedFile = false;
                }
                

            }

            // Close connection
            System.out.println("Received disconnect signal from client address " + socket.getInetAddress().toString() + " on port " + port);
            System.out.println("Exiting");
            out.writeUTF("Disconnected");
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error listening on port " + port);
            System.exit(-1);
        }
    }
    public static void main(String[] args) {
        try {
            int port = Integer.valueOf(args[0]);
            //Call server function with given port argument to initialize server/server sockets
            server s = new server(port);
        } catch (Exception e) {
            System.out.println("Failed to capture command line arguments");
            System.exit(-1);
        }
    }
}

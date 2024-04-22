package cr;
import java.io.*;
import java.net.*;

public class client {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String inFromServer = "";
    private String outToServer = "";
    private BufferedReader reader = null;

    public client(String hostname, int port) {
        try {
            // Setting up connection
            System.out.println("Attempting to connect to " + hostname + " on port " + port);
            socket = new Socket(hostname, port);
            System.out.println("Successfully connected to " + hostname + " on port " + port);

            // Initializing input/output streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));

            //Server sends "Hello!" to ensure connection is established before client can send a message
            inFromServer = in.readUTF();
            System.out.println("Received from server: " + inFromServer);
            // System.out.println("Enter events to schedule. Enter 'send' to receive a schedule. Enter 'bye' to exit.");
            boolean receiveFile = false;

            while (!outToServer.equals("bye")) {
                while(!receiveFile && !outToServer.equals("bye")) {
                    System.out.println("- Enter events to schedule seperated by a space. \n- Enter 'bye' to exit. \n- Enter \"view Schedule\" to view the master Schedule");
                    try {
                        System.out.print("Send server event: ");
                        outToServer = reader.readLine();
                        out.writeUTF(outToServer);
                        out.flush();
                        
                        inFromServer = in.readUTF();
                        if( inFromServer.equals("Master Schedule:")){
                            while(!inFromServer.equals("End of Schedule.")){
                                inFromServer = in.readUTF();
                                System.out.println(inFromServer);
                            }
                        }
                        if (inFromServer.equals("Sending file")) {
                            receiveFile = true;
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                try {
                    //Write schedule file received from server to cr directory
                    if (receiveFile) {
                        System.out.println("writing file"); //TODO
                        byte[] fileBytes = new byte[1024];
                        FileOutputStream fileOut = new FileOutputStream("./cr/" + "schedule.txt");
                        BufferedOutputStream fileWriter = new BufferedOutputStream(fileOut);
                        int totalBytes = in.read(fileBytes, 0, fileBytes.length);
                        fileWriter.write(fileBytes, 0, totalBytes);

                        fileWriter.close();
                        fileOut.close();
                        receiveFile = false;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            // Close connection
            System.out.println("Exiting");
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        try {
            String hostname = args[0];
            int port = Integer.valueOf(args[1]);
            //Call client function with given port and hostname arguments to initialize client/client socket
            client client = new client(hostname, port);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }
    }
}

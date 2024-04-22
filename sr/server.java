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
    private List<String> clientList = new ArrayList<>();

    private ArrayList<Event> masterSchedule = new ArrayList<>(Arrays.asList(
        new Event("Math", 9),
        new Event("Physics", 10),
        new Event("History", 11),
        new Event("Chemistry", 10),
        new Event("Biology", 13),
        new Event("Computer Science", 12),
        new Event("Literature", 9),
        new Event("Economics", 14),
        new Event("Art", 15),
        new Event("Psychology", 16)
    ));

    // Method to generate the schedule
    private void generateSchedule(List<String> requestedClasses) {
        // Filter the master list to only include events that the client requested
        ArrayList<Event> filteredEvents = new ArrayList<>();
        for (Event event : masterSchedule) {
            for (String requestedClass : requestedClasses) {
                if (event.name.contains(requestedClass)) { // Check if the event name contains the requested class
                    System.out.println("Adding " + event.name + " to the schedule");
                    filteredEvents.add(event);
                    break; // Avoid adding the same event multiple times
                }
            }
        }

        // Sort filtered events by their start times
        Collections.sort(filteredEvents, Comparator.comparingInt(e -> e.startTime));

        // Schedule the classes without overlaps
        ArrayList<String> scheduledEvents = new ArrayList<>();
        int lastEndTime = 0;
        for (Event event : filteredEvents) {
            if (event.startTime >= lastEndTime) {
                scheduledEvents.add(event.name);
                lastEndTime = event.startTime + 1; // Assuming each class is 1 hour long
            }
        }

        // Write the scheduled events to a file named "schedule.txt"
        try (PrintWriter out = new PrintWriter(new File("./sr/schedule.txt"))) {
            for (String eventName : scheduledEvents) {
                out.println(eventName);
            }
        } catch (IOException e) {
            System.out.println("Failed to write schedule file: " + e.getMessage());
        }
    }
    private List<String> readClientInput(String clientInput) {
        List<String> clientInputList = new ArrayList<>();
        // Assuming clientInput is a space-separated string
        String[] inputs = clientInput.split(" "); // Changed from comma to space, if needed
        for (String input : inputs) {
            if (!input.trim().isEmpty() && !input.equals("bye")) {
                clientInputList.add(input.trim());
            }
        }
        return clientInputList;
    }

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


            while (!clientInput.equals("bye")) {
                while(!requestedFile && !clientInput.equals("bye")){
                    try {
                        clientInput = in.readUTF();
                        System.out.println("Client said: " + clientInput);
                        clientList = readClientInput(clientInput);
                        System.out.println("Client list: " + clientList);
                        generateSchedule(clientList);
                        requestedFile = true;  
                        // if (clientInput.equals("send")) {
                        //     System.out.println("Client said:" + clientInput);
                        //     requestedFile = true;
                        // } else {
                        //     System.out.println("Client event: " + clientInput);
                        //     out.writeUTF("event received");
                        // }
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

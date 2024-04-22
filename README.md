
# Schedule Management System - P3

## Report:
https://docs.google.com/document/d/1p2QKTx_PzvNCmsmBOV5yxO_MmQ7kTc9yoaTJKBgj_G0/edit?usp=sharing

## Team Members
- Ashley Maurer
- Aiden Gaul
- Caleb Navarro - 9932 9701

## Description
This program is designed to help users manage their schedules by inputting desired classes, which are then compared against a master schedule to determine a compatible lineup of classes. This solution is implemented using a client-server model that can be run either locally or on the CISE machines.

## Prerequisites
- Java Development Kit (JDK) installed
- Access to CISE machines (if running remotely)

## Installation Instructions
1. Clone the repository or download the source code to your local machine.
2. Navigate to the root directory of the project.

## Running the Program

### Setting Up
If you are running the program on a CISE machine, create two SSH sessions; one for the client and one for the server. If running locally, open two terminal windows, one each for the client (`cr`) and the server (`sr`).

### Compiling the Code
In the terminal for both the client and server directories, run the following commands:
```bash
javac cr/*.java   # Compiles the client code
javac sr/*.java   # Compiles the server code
```

## Starting the Server
In the terminal for the server, start the server using the following command:

```bash
java sr/server 9932
```
Note: The server must be started before the client.

## Running the Client
Depending on your setup (local or CISE machines), use one of the following commands:

### Locally:
``` bash
java cr/client localhost 9932
```
### On CISE machines:
``` bash
java cr/client storm.cise.ufl.edu 9932
```
## Input and Output
After starting the client, input the classes you are interested in. The server will process this and return a file named schedule.txt in the cr directory with a list of available classes.

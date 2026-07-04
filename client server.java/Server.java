import java.net.*;
import java.io.*;
public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(9876); 
        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server started. Waiting for client messages...");

        while (true) {
            
             DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            serverSocket.receive(receivePacket); 
            String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());


            System.out.println("Client: " + clientMessage);
           
            System.out.print("Server: ");

            
            String serverMessage = reader.readLine();
            sendBuffer = serverMessage.getBytes();
           InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            serverSocket.send(sendPacket); 

          
            if (serverMessage.equalsIgnoreCase("exit")) {
                System.out.println("Server exited.");
                break;
            }
        }
        serverSocket.close();
    }}
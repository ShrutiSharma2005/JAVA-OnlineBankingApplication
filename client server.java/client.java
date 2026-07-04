import java.net.*;
import java.io.*;
public class client{
   public static void main(String[] args) throws IOException {
       DatagramSocket clientSocket = new DatagramSocket();
       InetAddress serverAddress = InetAddress.getByName("localhost");
       byte[] sendBuffer;
       byte[] receiveBuffer = new byte[1024];
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       while (true) {
           System.out.print("Client: ");
           String clientMessage = reader.readLine();  // <-- FIXED: read user input
           sendBuffer = clientMessage.getBytes();


           
           DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 9876);
           clientSocket.send(sendPacket); // Send the message to the server
           // Receiving data from server
           DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
           clientSocket.receive(receivePacket); // Block until data is received
           String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
           System.out.println("Server: " + serverMessage);
           // Exit on typing "exit"
           if (clientMessage.equalsIgnoreCase("exit")) {
               System.out.println("Client exited.");
               break;
           }
       }
       clientSocket.close();
   }
}
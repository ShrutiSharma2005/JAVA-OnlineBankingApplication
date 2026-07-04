import java.io.*;
import java.net.*;
public class server1 {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(9877);
			System.out.println("Server started. Waiting for a client...");
			clientSocket = serverSocket.accept();
			System.out.println("Client connected!");
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
			String fileName = inFromClient.readLine();
			System.out.println("Client requested file: " + fileName);
			File file = new File(fileName);


            
			if (file.exists() && !file.isDirectory()) {
				BufferedReader fileReader = new BufferedReader(new FileReader(file));
				String line;
				outToClient.println("FILE_FOUND");
				while ((line = fileReader.readLine()) != null) {
					outToClient.println(line);
				}
				fileReader.close();
				System.out.println("File sent successfully.");
			} else {
				outToClient.println("FILE_NOT_FOUND");
				System.out.println("Requested file not found.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (clientSocket != null) clientSocket.close();
				if (serverSocket != null) serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
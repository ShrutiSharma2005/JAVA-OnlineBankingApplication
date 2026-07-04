import java.io.*;
import java.net.*;
public class client1 {
	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 9877);
			System.out.println("Connected to the server!");

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Enter the name of the file to request: ");
			String fileName = userInput.readLine();

			outToServer.println(fileName);


			String serverResponse = inFromServer.readLine();
			if ("FILE_FOUND".equals(serverResponse)) {
				System.out.println("File found! Receiving content:");
				String line;
				while ((line = inFromServer.readLine()) != null) {
					System.out.println(line);
				}
			} else if ("FILE_NOT_FOUND".equals(serverResponse)) {
				System.out.println("File not found on the server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (socket != null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
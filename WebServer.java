// Omar Rodriguez
// CS 380
// Nima Davarpanah

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;


public class WebServer
{
	private static ServerSocket server;
	private static Socket socket;

	public static void main(String [] args) throws IOException {
		server = new ServerSocket(8080);
		while(true) {
			try {
				socket = server.accept();
				new ClientHandler(socket);
			}
      catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}

class ClientHandler extends Thread {
	private static Socket socket;
	public ClientHandler(Socket s) {
		socket = s;
		start();
	}
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
			String request = in.readLine();
			System.out.println(request);

			String filename = "";
			StringTokenizer str = new StringTokenizer(request);
			try {

				if(str.hasMoreElements() && str.nextToken().equalsIgnoreCase("GET") && str.hasMoreElements()) {
					filename = str.nextToken();
				} else {
					System.out.println("Error: ");
					throw new FileNotFoundException();
				}

				filename = "www/" + filename;
				InputStream ins = new FileInputStream(filename);
				String mime = "text/plain";
				if (filename.endsWith(".html") || filename.endsWith(".htm")) {
					mime = "text/html";
				}
				out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mime + "\r\n\r\n");

				byte[] response = new byte[4096];
				int n;

				while((n = ins.read(response)) > 0) {
					out.write(response,0,n);
				}
				out.close();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}

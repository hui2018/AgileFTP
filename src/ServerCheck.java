import java.util.Scanner;

public class ServerCheck {
	public String getServer() {
		Scanner scan = new Scanner(System.in);
		String serverAddress;
		System.out.print("Enter host: ");
		serverAddress = scan.next();
		return serverAddress;
	}

	public int getPort(){
		Scanner scan = new Scanner(System.in);
		int port;
		System.out.print("Enter port: ");
		port = scan.nextInt();
		return port;
	}

	public String getUser(){
		String userId;
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter User name: ");
		userId = scan.next();
		return userId;
	}

	public String getPassword(){
		String password;
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter password: ");
		password = scan.next();
		return password;
	}
}

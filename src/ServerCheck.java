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
	public static void main(String[] args) {
		/*int s;
		try{
			//create server
			FTPClient ftp = new FTPClient();
			
			String serverAddress="localhost";
			String userId="jack";
			String password="jack";
			int port = 21;

			//connect to server
	        ftp.connect(serverAddress,port);
	        
	        //login to server
	        if(!ftp.login(userId, password))
	        {
	            ftp.logout();
	            System.out.println("Login Error");
	        }
	        
	        int reply = ftp.getReplyCode();
	        //FTPReply stores a set of constants for FTP reply codes. 
	        if (!FTPReply.isPositiveCompletion(reply))
	        {
	            ftp.disconnect();
	            System.out.println("Connection Error");
	        }
	        //Test to stay login to the server
	        do
	        {
	        	System.out.print("Enter value: ");
	        	Scanner scan = new Scanner(System.in);
				s = scan.nextInt();
				//TODO what the user want to do
	        }while(s == 1);

	        ftp.logout();
	        ftp.disconnect();
	    }
	    catch (IOException ex)
	    {
	        ex.printStackTrace();
	        
	    }*/
	}
}

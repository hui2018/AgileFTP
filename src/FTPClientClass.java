import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner; 
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientClass {
	

	public static void main(String[] args) {
		int s;
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
	        
	    }
	}
}

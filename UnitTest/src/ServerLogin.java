import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.SocketException;

public class ServerLogin {
    public static void main(String[] args) {
        FTPClient ftp;
        String serverAddress;
        int port;
        String userId;
        String password;

        //Incorrect server
        try {
            //create server
            ftp = new FTPClient();
            serverAddress = "local";
            port = 21;

            //connect to server
            ftp.connect(serverAddress, port);
        }
        catch (IOException ex) {
            System.out.println("Incorrect server: Pass");
        }
        //correct server
        try{
            ftp = new FTPClient();
            serverAddress="localhost";
            port = 21;
            ftp.connect(serverAddress,port);
            System.out.println("Correct Server: Pass");

            //check login using filezilla server so the user and password can be different for different user to test
            //incorrect user and password
            userId="1";
            password="1";
            if(!ftp.login(userId, password))
            {
                ftp.logout();
                System.out.println("Login Error: Pass");
            }
            //reconnect to the server since it was logged out
            ftp.connect(serverAddress,port);
            userId="jack";
            password="jack";
            //passing in the correct argument
            if(!ftp.login(userId, password))
            {
                ftp.logout();
                System.out.println("Login Error: Failed");
            }
            else
                System.out.println("Login Success: Pass");
        }
        catch (IOException ex) {
        }
    }
}

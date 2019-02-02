/*----------------------------/
Main file


Changelog (insert new changes at top)
-------------

11/31/2019: Added Changelog, added shell - Nick H

/----------------------------*/

import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTPClient;

public class Main {

    public static void main(String[] args) {
        try {
            //create server
            FTPClient ftp = new FTPClient();
            ServerCheck server = new ServerCheck();

            //get server information
            String serverAddress = server.getServer();
            int port = server.getPort();
            String userId = server.getUser();
            String password = server.getPassword();
            //connect to server
            ftp.connect(serverAddress,port);

            //login to server
            if (!ftp.login(userId, password)) {
                ftp.logout();
                ftp.disconnect();
                System.out.println("Login Error");
            }
            //TODO all shell commands
            Scanner sc = new Scanner(System.in);
            Shell s = new Shell(sc);
            while (s.UserInput()) {
            }

            System.out.println("Logged out successfully!");
            ftp.logout();
            ftp.disconnect();
        } catch (IOException ex) {
            System.out.println("Unable to connect to server");
            //ex.printStackTrace();
        }
    }
}

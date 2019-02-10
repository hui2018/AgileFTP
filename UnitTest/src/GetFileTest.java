import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class GetFileTest {
    public static void main(String[] args) {
        FTPClient ftp;
        String serverAddress = "localhost";
        Shell s = new Shell();
        int port = 21;
        String userId = "jack";
        String password = "jack";
        try {
            ftp = new FTPClient();
            port = 21;
            ftp.connect(serverAddress, port);
            ftp.login(userId, password);
            String[] noFile = new String[]{"get", "nofile.txt"};
            String[] yesFile = new String[]{"get","test.txt"};
            //s.GetFile(noFile);
            //s.GetFile(yesFile);
            ftp.logout();
        }catch(IOException e){}
    }
}

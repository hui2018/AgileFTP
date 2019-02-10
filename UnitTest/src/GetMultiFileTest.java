import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class GetMultiFileTest {
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
            String[] noFile = new String[]{"getmulti", "nofile.txt"};
            String[] yesFile = new String[]{"getmulti","test.txt"};
            String[] mutliFile = new String[]{"getmulti", "test.txt", "myfile.txt", "asdf.nothing"};
            //s.GetMultipleFiles(noFile);
            //s.GetMultipleFiles(yesFile);
            //s.GetMultipleFiles(multiFile);
            ftp.logout();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyDirFromServer {
    private FTPClient ftp;
    public CopyDirFromServer(){
        ftp = new FTPClient();
    }
    public static void main(String[] args) {
        CopyDirFromServer s = new CopyDirFromServer();
        String [] incorrect = {"cpydir", "asdf"};
        String [] correct = {"cpydir", "Test"};
        s.CopyDirectoryFromServer(incorrect);
        s.CopyDirectoryFromServer(correct);
    }
    private void CopyDirectoryFromServer(String [] input)
    {
        try {
            ftp.connect("localhost", 2121);
            if (!ftp.login("admin", "admin")) {
                ftp.logout();
                ftp.disconnect();
                System.out.println("Login Error");
            }
            else
            {
                System.out.println("Login successfull.\nYou are now connected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(input[1].contains("."))
        {
            System.out.println("Please enter a correct directory path");
            return;
        }
        FTPFile[] ftpFiles;
        {
            try {
                ftpFiles = ftp.listFiles(input[1]);
                if (ftpFiles != null && ftpFiles.length > 0) {
                    //loop thru files
                    for (FTPFile file : ftpFiles) {
                        if (file.isFile()) {
                            File files = new File(file.getName());
                            ftp.enterLocalPassiveMode();
                            FileOutputStream dfile = new FileOutputStream(files);
                            ftp.retrieveFile(file.getName(),dfile);
                            dfile.close();
                            System.out.println("File " + file.getName() +" is downloaded");
                        }
                    }
                }
                else
                {
                    System.out.println("Please enter a correct directory path");
                }
            } catch (IOException e) {
                System.out.println("Directory does not exist");
                //e.printStackTrace();
            }
        }
    }
}

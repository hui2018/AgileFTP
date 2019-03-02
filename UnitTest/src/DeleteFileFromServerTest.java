import java.io.*;
import java.util.Scanner;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class DeleteFileFromServerTest {
    private static FTPClient ftp;
    public DeleteFileFromServerTest() {ftp = new FTPClient();}

    public static void main(String argv[])
    {
        DeleteFileFromServerTest delFile = new DeleteFileFromServerTest();
        String [] incorrect = {"testingFile123.txt", "blah blah"};
        String [] correct = {"test1.txt", "test2.txt", "test3.txt"};
        Scanner scan = new Scanner(System.in);
        System.out.println("Please place the following files into home folder on server");

        for(int i = 0; i < correct.length; i++)
        {
            System.out.println(correct[i]);
        }

        String serverAddress;
        System.out.print("Enter host: ");
        serverAddress = scan.next();

        int port;
        System.out.print("Enter port: ");
        port = scan.nextInt();

        String userId;
        System.out.print("Enter User name: ");
        userId = scan.next();

        String password;
        System.out.print("Enter password: ");
        password = scan.next();

        try
        {
            ftp.connect(serverAddress, port);
            if(!ftp.login(userId, password))
            {
                ftp.logout();
                ftp.disconnect();
                System.out.println("Login Error");
            }
            else
                System.out.println("Login successful.\nYou are not connected.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        delFile.DeleteFileFromServer(incorrect);
        delFile.DeleteFileFromServer(correct);
    }

    private void DeleteFileFromServer(String[] pathname)
    {
        boolean del = false;
        try
        {
            for(int i = 0; i < pathname.length; i++)
            {
                System.out.print("Deleting: " + pathname[i] + " : ");
                if(ftp.deleteFile(pathname[i]) == false)
                    System.out.print("Unable to delete: " + pathname[i]);
                else
                    System.out.print("Delete Successful: " + pathname[i]);

                System.out.print("\n");
            }
        }
        catch (FTPConnectionClosedException e)
        {
            System.err.println("Unable to connect to server: " + e);
        }
        catch (IOException e)
        {
            System.err.println("Error Delete: " + e);
        }
    }
}

import java.io.*;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

public class DeleteDirectoryFromServerTest {
    private static FTPClient ftp;
    public DeleteDirectoryFromServerTest() {ftp = new FTPClient();}

    public static void main(String argv[])
    {
        DeleteDirectoryFromServerTest delDir = new DeleteDirectoryFromServerTest();
        String [] incorrect = {"testingFile123", "blah blah"};
        String [] correct = {"test1", "test2", "test3"};
        Scanner scan = new Scanner(System.in);
        System.out.println("Please place the following empty directories into home folder on server");

        for(int i = 0; i < correct.length; i++)
        {
            System.out.println(correct[i]);
        }

        System.out.println("Inside test1, place a directory named test4 and put any file inside test4.");
        System.out.println("Place any amount of files inside test1 directory, and test2");

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

        delDir.DeleteDirectoryFromServer(incorrect);
        delDir.DeleteDirectoryFromServer(correct);

    }

    private void DeleteDirectoryFromServer(String[] pathname)
    {
        FTPFile[] files;
        boolean found = false;

        try
        {
            files = ftp.listFiles();
        }
        catch(FTPConnectionClosedException e)
        {
            System.err.println("Unable to connect to server: " + e);
            return;
        }
        catch(IOException e)
        {
            System.err.println("Error Delete: " + e);
            return;
        }

        for(int i = 0; i < pathname.length; i++)
        {
            for(int j = 0; j < files.length; j++)
            {
                if(pathname[i].equals(files[j].getName()))
                {
                    found = true;
                }
            }
            if(found == false)
                System.out.println("Unable to find: " + pathname[i]);

            found = false;
        }

        for(int i = 0; i < pathname.length; i++)
        {
            for(int j = 0; j < files.length; j++)
            {
                if(files[j].isDirectory())
                {
                    if (pathname[i].equals(files[j].getName()))
                    {
                        DeletingDirectories(files[j]);
                        try
                        {
                            ftp.changeToParentDirectory();
                            ftp.removeDirectory(pathname[i]);
                            System.out.println("Delete Successful: " + pathname[i]);
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
            }
        }
    }

    private void DeletingDirectories(FTPFile file)
    {
        if(file.isDirectory())
        {
            FTPFile[] ftpFiles = null;
            try
            {
                ftpFiles = ftp.listFiles(file.getName());
            }
            catch(IOException e)
            {
                System.err.println(e);
                return;
            }

            if(ftpFiles != null)
            {
                for(FTPFile f : ftpFiles)
                {
                    try
                    {
                        ftp.changeWorkingDirectory(file.getName());
                    }
                    catch (IOException e)
                    {
                        System.err.println(e);
                    }
                    DeletingDirectories(f);
                }
            }
            try
            {
                ftp.removeDirectory(file.getName());
            }
            catch (FTPConnectionClosedException e)
            {
                System.err.println("Unable to connect to server: " + e);
            }
            catch (IOException e)
            {
                System.err.println("Error Delete: " + e);
            }
            return;
        }
        else
        {
            try
            {
                ftp.deleteFile(file.getName());
                ftp.changeToParentDirectory();
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
}

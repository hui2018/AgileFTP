/*----------------------------/
FTP Client Shell code


Changelog (insert new changes at top)
-------------

2/9/19: Moved login into the shell

2/7/19: Added help and timeout functionality - Nick H

11/31/2019: Created class - Nick H

/----------------------------*/

//READ ME
//To add functionality to the shell, add new function to call
//functionality. Then, go to the switch statement in UserInput()
//and add a case
//READ ME

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
import org.apache.commons.net.ftp.FTPFile;

public class Shell {

    //Vars and constructors
    //------------------------------
    private int TimeOut;
    private Scanner sc;
    private FTPClient ftp;

    public Shell() {}

    public Shell(Scanner sc){
        this.sc = sc;
        this.TimeOut = 30*1000;
        ftp = new FTPClient();
    }

    public Shell(Scanner sc, int TimeOut) {
        this.sc = sc;
        this.TimeOut = TimeOut * 1000;
        ftp = new FTPClient();
        LogIn(null);
    }

    //User input gathering loop
    //-------------------------------
    public boolean UserInput () {
        System.out.print("shell->");
        boolean rv = true;

        String UserIn = TOInput();
        String [] UserInCom = UserIn.trim().split("\\s*>\\s*");

        //shell switch
        switch (UserInCom[0]) {
            case "sum":
                System.out.println(sum(UserInCom));
                break;
            case "q":
                rv = false;
                LogOut(UserInCom);
                break;
            case "login":
                LogIn(UserInCom);
                break;
            case "logout":
                LogOut(UserInCom);
                break;
            case "put":
                //put file to remote server example: put c:\filelocation\testing.txt
                if(CheckCommands(UserInCom))
                    PutFile(UserInCom);
                break;
            case "putmulti":
                //put multiple files to remote server example: putmulti c:\filelocation\testing.txt c:\other\second.txt
                if(CheckMultipleGetPutCommand(UserInCom))
                    PutMultipleFiles(UserInCom);
                break;
            case "get":
                //check there is only one file on command line by limit the command argument then run the function
                if(CheckCommands(UserInCom))
                    GetFile(UserInCom);
                break;
            case "getmulti":
                //pass in multiple files
                if(CheckMultipleGetPutCommand(UserInCom))
                    GetMultipleFiles(UserInCom);
                break;
            case "ls":
                ListDirectoriesAndFiles(UserInCom);
                break;
            case "local":
                if(CheckCommands(UserInCom))
                    ListLocalDirectoriesAndFiles(UserInCom);
                break;
            case "rename":
                if(RenameFileCheck(UserInCom))
                    RenameFileOnServer(UserInCom);
                break;
            case "help":
                help();
                break;
            case "h":
                help();
                break;
            case "--to":
                TOShift(UserInCom);
                break;
            default:
                System.out.println("Not a valid function. Type 'help' or 'h' to see functions.");
                System.out.println("Type 'q' or 'logout' to logout.");
        }
        return rv;
    }

    //Functions called by shell input
    //-------------------------------

    //sum function used for testing purposes
    private int sum (String[] inputs)
    {
        int ret = 0;
        for (String s:inputs) {
            try {
               ret += Integer.parseInt(s);
            }
            catch (NumberFormatException e)
            {
            }
        }
        return ret;
    }

    private void LogIn (String[] inputs)
    {
        try {
            //TODO: unhandled exception when entering a non-int as the port
            //create server

            ServerCheck server = new ServerCheck();

            //get server information
            String serverAddress = server.getServer();
            int port = server.getPort();
            String userId = server.getUser();
            String password = server.getPassword();
            //connect to server
            ftp.connect(serverAddress, port);

            //login to server
            if (!ftp.login(userId, password)) {
                ftp.logout();
                ftp.disconnect();
                System.out.println("Login Error");
            }
            else
            {
                System.out.println("Login successfull.\nYou are now connected.");
            }
        }
        catch (IOException ex) {
            System.out.println("Unable to connect to server");
            //ex.printStackTrace();
        }
    }

    private void LogOut (String[] inputs)
    {
        try {
            ftp.logout();
            ftp.disconnect();
            System.out.println("Logout successful.");
        }
        catch (IOException e)
        {
            //TODO: Only show this if there was actually a server connection to begin with
            //IE: currently, if you logout, then quit, this will always trigger
            System.out.println("Something went wrong.");
        }
    }

    //---------------DO NOT PLACE COMMAND FUNCTIONS BELOW THIS POINT----------------//
    //-----------------------INNER SHELL FUNCTIONALITY ONLY-------------------------//
    //Timeout Function
    private String TOInput()
    {
        try {
            BufferedReader UserIn = new BufferedReader(new InputStreamReader(System.in));
            long StartTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - StartTime) < TimeOut && !UserIn.ready())
            { /*Condition causes us to wait until timeout or user input*/ }

            if (UserIn.ready()) {
                return UserIn.readLine();
            }
            else
            {
                System.out.println("\nNo commands entered in " + TimeOut/1000 + " seconds.\n" +
                        "Shell timed out.");
                return "q";
            }
        }
        catch (IOException e)
        {
            System.out.println("If you're seeing this, I messed up!");
        }
        return "";
    }

    //modify timeout
    private void TOShift(String[] s)
    {
        try {
            TimeOut = Integer.parseInt(s[1]) * 1000;
            System.out.println("Timeout value changed to " + TimeOut/1000 + " seconds.");
        }
        catch (Exception e)
        {
            System.out.println("Not a valid timeout value.");
        }
    }

    // Put a single file onto remote server
    private void PutFile(String[] filePath) {

        // Initialize variable
        File fileTest = new File(filePath[1]);

        // Check if the file exists on local drive
        if (fileTest.exists()) {
            // If the file exists, store it onto the server
            try {
                // Enter passive mode to allow uploading to server?
                //ftp.enterLocalPassiveMode();

                // Store the file on server
                //FileInputStream fis = new FileInputStream(filePath[1]);
                //ftp.storeFile(filePath[1], fis);
                //fis.close();


                ftp.enterLocalPassiveMode();

                InputStream is = new FileInputStream(fileTest);

                ftp.setFileType(ftp.BINARY_FILE_TYPE,ftp.BINARY_FILE_TYPE);
                ftp.setFileTransferMode(ftp.BINARY_FILE_TYPE);
                ftp.storeFile(filePath[1], is);

                is.close();

            } catch (IOException e) { // Print Stack Trace if failed
                e.printStackTrace();
            }
        }
        // If the file does not exist, notify user that file does not exist
        else{
            System.out.println("File Does Not Exist.");
        }
    }

    // Put Multiple Files onto remote server
    private void PutMultipleFiles(String[] filePath){

        // Iterate through each file to check if they exist
        for(int i = 1; i < filePath.length; i++) {
            // Create array of files
            File [] fileTest = new File[i];
            fileTest[i] = new File(filePath[i]);


            // Check each file to see if it exists on drive
            if(fileTest[i].exists()) {
                try {
                    //Store the current file on server
                    FileInputStream fis = new FileInputStream(filePath[i]);
                    ftp.storeFile(filePath[i], fis);
                    fis.close();

                } catch (IOException e) { // Print Stack Trace if failed
                    e.printStackTrace();
                }
            }
            // If the specific file does not exist, notify user
            else{
                System.out.println("File Number: " + i + " Does Not Exist");
            }
        }
    }

    //get file from remote server
    private void GetFile(String[] filePath)
    {
        FTPFile[] ftpFiles;
        boolean checker = true;
        try {
            ftpFiles = ftp.listFiles();
                for (FTPFile file : ftpFiles) {
                    if(file.getName().contentEquals(filePath[1]))
                    {
                        File files = new File(filePath[1]);
                        ftp.enterLocalPassiveMode();
                        FileOutputStream dfile = new FileOutputStream(files);
                        ftp.retrieveFile(filePath[1],dfile);
                        dfile.close();
                        checker = false;
                        System.out.println("File exist on server");
                    }
                }
                if(checker)
                    System.out.println("File does not exist on server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check if too many command or too little command
    private boolean CheckCommands(String[] filePath){
        if(filePath.length >= 3)
        {
            System.out.println("Too many commands");
            return false;
        }
        else if(filePath.length == 1)
        {
            System.out.println("Please enter a existing path");
            return false;
        }
        return true;
    }

    private void GetMultipleFiles (String[] filePath)
    {
        FTPFile[] ftpFiles;
        try {
            ftpFiles = ftp.listFiles();
            for(int i = 1; i<filePath.length; ++i)
            {
                for (FTPFile file : ftpFiles) {
                    if(file.getName().contentEquals(filePath[i]))
                    {
                        File files = new File(filePath[i]);
                        ftp.enterLocalPassiveMode();
                        FileOutputStream dfile = new FileOutputStream(files);
                        ftp.retrieveFile(filePath[i],dfile);
                        dfile.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean CheckMultipleGetPutCommand(String[] filePath){
        if(filePath.length == 1)
        {
            System.out.println("Please enter a file path");
            return false;
        }
        return true;
    }

    //print out directories and files on remote server
    private void ListDirectoriesAndFiles(String[] input)
    {
        FTPFile[] ftpFiles;
        try {
            ftpFiles = ftp.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                //loop thru files
                for (FTPFile file : ftpFiles) {
                    if (file.isFile()) {
                        System.out.println("File is " + file.getName());
                    } else if (file.isDirectory()){
                        System.out.println("Directory is " + file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //list out all of the directories and files with a given directory
    private void ListLocalDirectoriesAndFiles(String[] input)
    {
        File directory = new File(input[1]);
        File [] list = directory.listFiles();
        if(list == null)
        {
            System.out.println("Directory does not exist");
            return;
        }
        for(File file:list)
        {
            if(file.isFile())
                System.out.println("File is " + file.getName());
            else if(file.isDirectory())
                System.out.println("Directory is " + file.getName());
        }
    }

    private boolean RenameFileCheck(String[] input)
    {
        if(input.length <= 2)
        {
            System.out.println("Please enter a existing file path and a new file path name");
            return false;
        }
        if(input.length >3)
        {
            System.out.println("Too many commands");
            return false;
        }
        return true;
    }

    //rename a file name on remote server
    private void RenameFileOnServer(String [] input)
    {
        String oldName = input[1];
        String newName = input[2];
        try {
            boolean check = ftp.rename(oldName, newName);
            if(check)
                System.out.println(oldName + " was successfully renamed to: " + newName);
            else
                System.out.println(oldName + " was not successfully renamed to: " + newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Help function
    //LEAVE THIS AT THE BOTTOM FOR READABILITY
    private void help ()
    {
        System.out.println("The following is a list of functions available in this client:\n" +
                "login\n\tStarts prompts to log in to a server.\n" +
                "logout\n\tLogs out of the currently connected server.\n" +
                "put>(local_filepath)\n\tPuts the specified file to the connected server.\n" +
                "get>(server_filepath)\n\tGets the specified file from the connected server.\n" +
                "");
    }
}

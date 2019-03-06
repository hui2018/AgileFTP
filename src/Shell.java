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
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

public class Shell {

    //Vars and constructors
    //------------------------------
    private int TimeOut;
    private Scanner sc;
    private FTPClient ftp;
    public HistoryLog Log;
    private String[] loginInfo;

    public Shell() {}

    public Shell(Scanner sc){
        this.sc = sc;
        this.TimeOut = 30*1000;
        loginInfo = new String[] {};
        ftp = new FTPClient();
    }

    public Shell(Scanner sc, int TimeOut) {
        this.sc = sc;
        this.TimeOut = TimeOut * 1000;
        loginInfo = new String[] {};
        ftp = new FTPClient();
        WhichLogIn(null);
        Log = new HistoryLog();
    }

    //User input gathering loop
    //-------------------------------
    public boolean UserInput () {
        System.out.print("shell->");
        boolean rv = true;

        String UserIn = TOInput();
        Log.AddLog(UserIn);
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
                // Put Error
                if(UserInCom.length < 2) {
                    System.out.println("Unable to execute put command");
                    System.out.println("Format: put>filepath");
                }
                // Single Put
                // Put file to remote server example: put>c:\filelocation\testing.txt
                if(UserInCom.length == 2) {
                    if (CheckCommands(UserInCom))
                        PutFile(UserInCom);
                }

                // Put Multi
                // Put multiple files to remote server example: putmulti>c:\filelocation\testing.txt>c:\other\second.txt
                if(UserInCom.length > 2) {
                    if (CheckMultipleGetPutCommand(UserInCom))
                        PutMultipleFiles(UserInCom);
                }
                break;
            case "get":
                // Get error
                if(UserInCom.length < 2) {
                    System.out.println("Unable to execute get command");
                    System.out.println("Format: get>filepath");
                }

                // Single Get
                // To test enter get>"path of file on server"
                if(UserInCom.length == 2) {
                    if (CheckCommands(UserInCom))
                        GetFile(UserInCom);
                }

                // Get Multi
                // To test enter get>"path of file on server">"path of file on server">
                if(UserInCom.length > 2) {
                    if (CheckMultipleGetPutCommand(UserInCom))
                        GetMultipleFiles(UserInCom);
                }
                break;
            case "ls":
                //to test just enter ls
                ListDirectoriesAndFiles(UserInCom);
                break;
            case "local":
                //to test enter local>"path on local machine"
                if(CheckCommands(UserInCom))
                    ListLocalDirectoriesAndFiles(UserInCom);
                break;
            case "rename":
                //to test enter rename>"file name on server">"new file name"
                if(RenameFileCheck(UserInCom))
                    RenameFileOnServer(UserInCom);
                break;
            case "renamelocal":
                //to test enter renamelocal>"file path on local machine">"new file name on local machine"
                if(RenameFileCheck(UserInCom))
                    RenameFileOnLocalMachine(UserInCom);
                break;
            case "cpydir":
                //to test enter cpydir>"directory path"
                if(CheckCommands(UserInCom))
                    CopyDirectoryFromServer(UserInCom);
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
            case "log":
                Log.DisplayLog();
                break;
            case "rmdir":
                DeleteDirectoryFromServer(UserInCom);
                break;
            case "rm":
                DeleteFileFromServer(UserInCom);
                break;
            case "saveLogin":
                SaveConnection();
                break;
            //Test command: mkdir > "to create directory name"
            case "mkdir":
                createDirectory(UserInCom);
                break;
            //Test command: cd > "to change directory name"
            case "cd":
                changeDirectory(UserInCom);
                break;
            //3 digit permission code = 777, 755, 666, etc...
            //Test command: chmod > "3 digit permission code" > "file or directory name"
            case "chmod":
                changePermission(UserInCom);
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

    //log in manually
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
                //keep login info if user wants to save it
                loginInfo = new String[] {serverAddress,Integer.toString(port),userId,password};
            }
        }
        catch (IOException ex) {
            System.out.println("Unable to connect to server");
            //ex.printStackTrace();
        }
    }

    //login from a file
    private boolean SavedLogIn (String[] inputs)
    {
        //only look 1 place for file
        String path = Shell.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File conInfo = new File(path + "loginInfo.txt");
        if (!conInfo.exists())
        {
            System.out.println("No saved connection exists.");
            return false;
        }
        try {
            //connection info
            String[] con = new String[3];
            //port value
            Integer p;
            FileReader fr = new FileReader(conInfo);
            BufferedReader br = new BufferedReader(fr);
            //Fill our info from the file.
            con[0] = br.readLine();
            p = Integer.parseInt(br.readLine());
            con[1] = br.readLine();
            con[2] = br.readLine();

            //connect
            ftp.connect(con[0], p);

            //login to server
            if (!ftp.login(con[1], con[2])) {
                ftp.logout();
                ftp.disconnect();
                System.out.println("Login Error");
            }
            else
            {
                System.out.println("Login successfull.\nYou are now connected.");
            }
        } catch (IOException e) {
            System.out.println("Connection in file is not valid");
            return false;
        }
        catch (NumberFormatException e)
        {
            System.out.println("Saved port is not valid");
            return false;
        }
        return true;
    }

    //on launch, choose which login to use: from file, or from user
    private void WhichLogIn (String[] inputs)
    {
        boolean doMe = false;
        System.out.print("Login using saved login info? (y/n) ");
        String ans = sc.next();
        if (ans.equals("y"))
        {
            doMe = SavedLogIn(inputs);
        }
        if (!doMe)
        {
            LogIn(inputs);
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
        File localFile = new File(filePath[1]);

        // Check if the file exists on local drive
        if (localFile.exists()) {
            String remoteFile = "\\" + localFile.getName();

            // If the file exists, store it onto the server
            try {
                // Create input and output streams to upload and store file remotely
                InputStream inputStream = new FileInputStream(localFile);
                OutputStream outputStream = ftp.storeFileStream(remoteFile);

                // Create buffer to transfer file
                byte [] bytesIn = new byte[4096];
                int read = 0;
                while ((read = inputStream.read(bytesIn)) != -1){
                    outputStream.write(bytesIn,0,read);
                }

                // Close input and output streams
                inputStream.close();
                outputStream.close();

                // Check to see if action is complete
                boolean completed = ftp.completePendingCommand();
                if(completed) {
                    System.out.println("File successfully uploaded.");
                }

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
            File [] localFile = new File[filePath.length-1];
            localFile[i-1] = new File(filePath[i]);

            // Check each file to see if it exists on drive
            if(localFile[i-1].exists()) {
                String remoteFile = "\\" + localFile[i-1].getName();
                try {
                    //Store the current file on server
                    InputStream inputStream = new FileInputStream(localFile[i-1]);
                    OutputStream outputStream = ftp.storeFileStream(remoteFile);

                    // Create buffer to read in each file
                    int read = 0;
                    byte [] buffer = new byte[4096];

                    while ((read = inputStream.read(buffer)) != -1){
                        outputStream.write(buffer,0,read);
                    }

                    // Close the input and output streams
                    inputStream.close();
                    outputStream.close();

                    // Check to make sure each upload finishes
                    boolean completed = ftp.completePendingCommand();
                    if(completed) {
                        System.out.println("File " + i + " successfully uploaded.");
                    }
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
                    System.out.println("File " + filePath[1] + " successfully retrieved.");
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
        boolean check;

        try {
            ftpFiles = ftp.listFiles();
            for(int i = 1; i<filePath.length; ++i)
            {
                check = true;
                for (FTPFile file : ftpFiles) {
                    if(file.getName().contentEquals(filePath[i]))
                    {
                        File files = new File(filePath[i]);
                        ftp.enterLocalPassiveMode();
                        FileOutputStream dfile = new FileOutputStream(files);
                        ftp.retrieveFile(filePath[i],dfile);
                        dfile.close();
                        System.out.println(file.getName() + " downloaded");
                        check = false;
                    }
                }
                if(check)
                    System.out.println(filePath[i] + " file can't be found");
            }
        } catch (IOException e) {
            System.out.println("File not found");
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

    //rename a file name on local machine
    private void RenameFileOnLocalMachine(String [] input)
    {
        File oldName = new File(input[1]);
        File newName = new File(input[2]);
        boolean check = oldName.renameTo(newName);
        if(check)
            System.out.println(oldName + " was successfully renamed to: " + newName);
        else
            System.out.println(oldName + " was not successfully renamed to: " + newName);
    }

    //copy all files from a input directory
    private void CopyDirectoryFromServer(String [] input)
    {
        //need to change savedir on different machine
        try {
            FTPUtil.downloadDirectory(ftp, "\\"+input[1], "", "C:\\Users\\Jack\\Desktop\\Testing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //delete directory from server
    private void DeleteDirectoryFromServer(String[] pathname)
    {
        try
        {
            for(int i = 1; i < pathname.length; i++)
            {
                if(ftp.removeDirectory(pathname[i]) == false)
                    System.out.println("Unable to delete: " + pathname[i]);
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

    //delete file from server
    private void DeleteFileFromServer(String[] pathname)
    {
        boolean del = false;
        try
        {
            for(int i = 1; i < pathname.length; i++)
            {
                if(ftp.deleteFile(pathname[i]) == false)
                    System.out.println("Unable to delete: " + pathname[i]);
                else
                    System.out.println(pathname[i] + " file is deleted");
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

    //save connection
    private void SaveConnection()
    {
        if (loginInfo.length == 4) {
            String path = Shell.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File conInfo = new File(path + "loginInfo.txt");
            try {
                PrintWriter conFile = new PrintWriter(conInfo);
                conFile.println(loginInfo[0]);
                conFile.println(loginInfo[1]);
                conFile.println(loginInfo[2]);
                conFile.println(loginInfo[3]);
                conFile.close();
                System.out.println("Current login info saved.");
            } catch (IOException e) {

            }
        }
        else
        {
            System.out.println("No successful logins to save this session.");
        }
    }

    //Check server respond to command
    private static void checkServerReply(FTPClient client)
    {

        String[] serverReplies = client.getReplyStrings();
        if (serverReplies != null && serverReplies.length > 0) {
            for (String printReplies : serverReplies) {
                System.out.println(printReplies);
            }
        }
    }

    //Create Directory
    private void createDirectory(String[] inputs){
        boolean create = false;
        try {
            create = ftp.makeDirectory(inputs[1]);
            if(create) {
                System.out.println("\"/" + inputs[1] + "\" Directory created.");
            }
            else {
                System.out.println("Failed! See server message below.");
                checkServerReply(ftp);
            }
        }catch(IOException error) {
            System.out.println("IO operation failed!");
            error.printStackTrace();
        }
    }

    //Change Directory
    private void changeDirectory(String[] inputs){

        boolean change = false;

        try {

            change = ftp.changeWorkingDirectory(inputs[1]);

            if(change) {
                System.out.println("\"/" + inputs[1]+ "\" is the current directory.");
            }
            else {
                System.out.println("Failed! See server message below.");
                checkServerReply(ftp);
            }

        }catch(IOException error) {

            System.out.println("IO operation failed!");
            error.printStackTrace();
        }

    }

    //Change Permission
    private void changePermission(String[] inputs){

        boolean change = false;

        try {
            change = ftp.sendSiteCommand("chmod " + inputs[1] + " " + inputs[2]);

            if(change) {
                System.out.println("\"/" + inputs[2] + "\" permission is: " + inputs[1]);
            }
            else {
                System.out.println("Failed! See server message below.");
                checkServerReply(ftp);
            }

        }catch(IOException error) {
            System.out.println("IO operation failed!");
            error.printStackTrace();
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
                "log\n\tDisplay recent commands" +
                "");
    }
}

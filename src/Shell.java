/*----------------------------/
FTP Client Shell code


Changelog (insert new changes at top)
-------------

11/31/2019: Created class - Nick H

/----------------------------*/

//READ ME
//To add functionality to the shell, add new function to call
//functionality. Then, go to the switch statement in UserInput()
//and add a case
//READ ME

import java.io.*;
import java.util.Scanner;

public class Shell {

    //Vars and constructors
    //------------------------------
    private int TimeOut;
    private int Timer;
    private Scanner sc;

    public Shell() {}

    public Shell(Scanner sc){
        this.sc = sc;
    }

    public Shell(Scanner sc, int TimeOut) {
        this.sc = sc;
        //If we get to timeouts, place timeout length here
        this.TimeOut = TimeOut;
    }

    //User input gathering loop
    //-------------------------------
    public boolean UserInput () {
        System.out.print("shell->");
        boolean rv = true;

        String UserIn = sc.nextLine();
        String [] UserInCom = UserIn.trim().split("\\s*>\\s*");

        //shell switch
        switch (UserInCom[0]) {
            case "sum":
                System.out.println(sum(UserInCom));
                break;
            case "q":
                rv = false;
                break;
            case "put":
                //put file to remote server example: put c:\filelocation\testing.txt
                break;
            case "putmulti":
                //put multiple files to remote server example: putmulti c:\filelocation\testing.txt c:\other\second.txt
                break;
            case "get":
                //get files from server example: get c:\server\file.txt
                break;
            case "getmulti":
                //get multiple files from server example: getmulti c:\server\file.txt c:\other\test.img
                break;
            case "help":
                help();
                break;
            case "h":
                help();
                break;
            default:
                System.out.println("Not a valid function. Type 'help' or 'h' to see functions.");
                System.out.println("Type 'q' to logout.");
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

    //---------------DO NOT PLACE FUNCTIONS BELOW THIS POINT----------------//
    //Help function
    //LEAVE THIS AT THE BOTTOM FOR READABILITY
    private void help ()
    {
        System.out.println("The following is a list of functions available in this client:\n" +
                "sum>(integer)>(integer)>...>(integer)\n\tSums up all following integers. Ignores other arguments.\n" +
                "put>(local_filepath)\n\tPuts the specified file to the connected server.\n" +
                "get>(server_filepath)\n\tGets the specified file from the connected server.\n" +
                "");
    }
}

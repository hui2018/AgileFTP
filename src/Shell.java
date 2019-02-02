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

import java.io.IOException;
import java.util.Scanner;

public class Shell {

    //Vars and constructors
    //------------------------------
    private int TimeOut;
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
        boolean rv = true;
        sc = new Scanner(System.in);
        String UserIn = sc.nextLine();
        String [] UserInCom = UserIn.split(">");

        //shell switch
        switch (UserInCom[0]) {
            case "sum":
                System.out.println(sum(UserInCom));
                break;
            case "q":
                rv = false;
                break;
            default:
                System.out.println("Not a valid function.");
                System.out.println("type 'q' to logout");
        }

        return rv;
    }

    //Functions called by shell input
    //-------------------------------
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
}

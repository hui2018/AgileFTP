/*----------------------------/
Main file


Changelog (insert new changes at top)
-------------

2/9/2019: Moved login/logout to shell

1/31/2019: Added Changelog, added shell - Nick H

/----------------------------*/

import java.io.IOException;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTPClient;

public class Main {

    public static void main(String[] args) {
        //TODO all shell commands
        //Build shell with IO scanner
        Scanner sc = new Scanner(System.in);
        int TimeOut = 120;
        System.out.println("Current shell timeout between commands is set to " + TimeOut + " seconds.");
        Shell s = new Shell(sc, TimeOut);
        //loop getting user input
        while (s.UserInput()) {
        }

    }
}


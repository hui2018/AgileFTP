/*----------------------------/
Main file


Changelog (insert new changes at top)
-------------

11/31/2019: Added Changelog, added shell - Nick H

/----------------------------*/

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        Scanner sc = new Scanner(System.in);
        Shell s = new Shell(sc);
        while (s.UserInput()) {}
    }
}

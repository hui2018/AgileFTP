import java.io.File;

public class RenameFileOnLocal {
    public static void main(String[] args) {
        RenameFileOnLocal s = new RenameFileOnLocal();
        String [] notWorking = {"fileNotFound", "C:\\Users\\Jack\\Desktop\\TestLocal\\rename.txt"};
        String [] working = {"C:\\Users\\Jack\\Desktop\\TestLocal\\test.txt", "C:\\Users\\Jack\\Desktop\\TestLocal\\rename.txt"};
        String [] returnBack = {"C:\\Users\\Jack\\Desktop\\TestLocal\\rename.txt", "C:\\Users\\Jack\\Desktop\\TestLocal\\test.txt"};
        //not working
        s.RenameFileOnLocalMachine(notWorking);
        //working
        s.RenameFileOnLocalMachine(working);
        //returnBack
        s.RenameFileOnLocalMachine(returnBack);
    }

    private void RenameFileOnLocalMachine(String [] input)
    {
        File oldName = new File(input[0]);
        File newName = new File(input[1]);
        boolean check = oldName.renameTo(newName);
        if(check)
            System.out.println(oldName + " was successfully renamed to: " + newName);
        else
            System.out.println(oldName + " was not successfully renamed to: " + newName);
    }
}

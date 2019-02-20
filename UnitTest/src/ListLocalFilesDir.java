import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;

public class ListLocalFilesDir {
    public static void main(String[] args) {
        File directory;
        directory = new File("asdfa");
        File [] list = directory.listFiles();
        if(list == null)
        {
            System.out.println("Directory does not exist");
            directory = new File("C:\\Users\\Jack\\Desktop\\TestLocal");
            list = directory.listFiles();
            System.out.println(directory);
            for(File file:list)
            {
                if(file.isFile())
                    System.out.println("File is " + file.getName());
                else if(file.isDirectory())
                    System.out.println("Directory is " + file.getName());
            }
        }
    }
}


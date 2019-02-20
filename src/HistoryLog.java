import java.io.IOException;
import java.util.*;

public class HistoryLog {
    private Stack<String> Log;

    HistoryLog()
    {
        Log = new Stack();
    }

    public void AddLog(String command)
    {
        try
        {
            Log.push(command);
        }
        catch(Exception e)
        {
            System.out.println("Error adding");
        }
    }

    public void DisplayLog()
    {
        Object[] history = Log.toArray();
        System.out.println("\nHistory:");
        for(Object i : history)
        {
            System.out.println("\t" + i);
        }
    }
}
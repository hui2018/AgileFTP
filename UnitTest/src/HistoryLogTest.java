public class HistoryLogTest {
    public static void main(String[] args){
        HistoryLog TestLog = new HistoryLog();
        System.out.println("LOG SHOULD BE EMPTY: PRINTING LOG BELOW");
        TestLog.DisplayLog();
        System.out.println("ADDING \'FIRST LOG\' PRINTING LOG BELOW");
        TestLog.AddLog("FIRST LOG");
        TestLog.DisplayLog();
        System.out.println("ADDING \'SECOND LOG\' PRINTING LOG BELOW");
        TestLog.AddLog("SECOND LOG");
        TestLog.DisplayLog();
        System.out.println("ADDING \'THIRD LOG\' PRINTING LOG BELOW");
        TestLog.AddLog("THIRD LOG");
        TestLog.DisplayLog();
    }
}

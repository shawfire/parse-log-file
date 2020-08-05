package net.shawfire.plf;

public class App {

    public static String MustPassFileName = "Pass log file name argument.";
    public static String ExpectedOneArgGotNMsg = "Expected 1 argument but received: %1d";
    public static String DefaultUsageMessage = "Default log file \"%1$s\" is being used.";
    public static String DefaultLogFileName = "/programming-task-example-data.log";
    public static boolean ProvideDefaultLogFile = true;

    private String fileName = null;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private static void print(String val) {
        System.out.print(val + "\n");
    }
    private static void println(String val) {
        print(val + "\n");
    }

    public App(String fileName) {
        setFileName(fileName);
    }

    public static void main(String[] args) throws Exception {
        /* Validate the number of parameters */
        String fileName;
        if (args.length != 1) {
            usage();
            if (ProvideDefaultLogFile) {
                fileName = DefaultLogFileName;
                println(String.format(DefaultUsageMessage, fileName));
            } else {
                throw new java.lang.IllegalArgumentException(
                        String.format(ExpectedOneArgGotNMsg, args.length));
            }
        } else {
            fileName = args[0];
        }
        App app = new App(fileName);
        app.run();
    }

    private static void usage() {
        println(MustPassFileName);
    }

    public void run() throws Exception {

        // Read logFile
        Parser parser = new Parser();
        parser.readFromInputStream(getFileName());

        // FIRST REQUIREMENT: The number of unique IP addresses
        parser.getIpAddressParser().getNumberOfIPs();

        // SECOND REQUIREMENT: The top 3 most visited URLs
        int limit = 3;
        parser.getUrlParser().getMostVisitedURLs(limit);

        // THIRD REQUIREMENT: The top 3 most active addresses
        parser.getIpAddressParser().getMostActiveIPs(limit);

    }

}

package clientAndServer.exeptions;

public class InvalidNameException extends Exception{

    public String getMessage(){
        return "Invalid command. Use \"help\" to get a list of available commands.";
    }
}

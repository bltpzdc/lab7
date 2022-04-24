package clientAndServer.exeptions;

public class TooManyArgsException extends Exception{
    public String getMessage(){
        return "This command has not any parameters. Use \"help\" to get list of commands.";
    }
}

package clientAndServer.exeptions;

public class NonArgsExeption extends Exception{
    public String getMessage(){
        return "You did not enter arguments. Use \"help\" to get a list of available commands.";
    }
}

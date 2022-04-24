package clientAndServer.startingData;

import java.time.LocalDate;
import java.util.Vector;

public class CustomVector<T> extends Vector<T> {
    private final LocalDate initTime;
    public CustomVector(){
        initTime = LocalDate.now();
    }
    public LocalDate getInitTime(){
        return initTime;
    }
}

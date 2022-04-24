package clientAndServer.startingData;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private double x;
    private Float y;

    public Coordinates(double x, Float y){
        this.x=x;
        this.y=y;
    }

    public String toString(){
        return "x: "+x+", y: "+y;
    }
}

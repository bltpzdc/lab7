package server.tools;

import lombok.Getter;

import java.io.Serializable;

public class ServerAnswer implements Serializable {
    @Getter
    private String message;
    public ServerAnswer(String message){
        this.message = message;
    }
}

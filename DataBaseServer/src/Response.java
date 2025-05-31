//ICSD21028 -- Konstantinos Katsaros

package DataBaseServer.src;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseStatus status;
    private String message;
    private Object data;

    public Response(ResponseStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    
}

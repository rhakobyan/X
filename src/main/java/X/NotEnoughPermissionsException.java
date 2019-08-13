package X;

public class NotEnoughPermissionsException extends Exception{

    private String message;

    public NotEnoughPermissionsException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

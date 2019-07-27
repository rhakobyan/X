package X;

public class NoSuchUserException extends Exception {

    private String message;

    public NoSuchUserException(String message){
        this.message = message;
    }

}

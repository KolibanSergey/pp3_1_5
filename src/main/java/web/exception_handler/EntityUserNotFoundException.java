package web.exception_handler;

public class EntityUserNotFoundException extends RuntimeException{
    public EntityUserNotFoundException(String message) {
        super(message);
    }
}
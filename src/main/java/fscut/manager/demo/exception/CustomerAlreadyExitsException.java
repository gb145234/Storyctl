package fscut.manager.demo.exception;

public class CustomerAlreadyExitsException extends Exception{
    public CustomerAlreadyExitsException(String msg){
        super(msg);
    }
}

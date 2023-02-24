package project.exception.custom_ex;

public class UserAlreadyRegisteredException extends RuntimeException{
    public UserAlreadyRegisteredException (String message){
        super(message);
    }
}

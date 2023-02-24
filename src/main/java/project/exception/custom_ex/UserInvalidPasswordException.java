package project.exception.custom_ex;

public class UserInvalidPasswordException extends RuntimeException{
    public UserInvalidPasswordException (String message){super(message);}
}

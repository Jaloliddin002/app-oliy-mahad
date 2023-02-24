package project.exception.custom_ex;

public abstract class UserAuthenticationException extends RuntimeException{
    public UserAuthenticationException (String message){super(message);}
}

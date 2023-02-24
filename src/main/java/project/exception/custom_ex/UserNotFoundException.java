package project.exception.custom_ex;

public class UserNotFoundException extends UserAuthenticationException{
    public UserNotFoundException(String message) {
        super(message);
    }
}

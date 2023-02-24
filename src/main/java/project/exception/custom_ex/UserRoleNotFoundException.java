package project.exception.custom_ex;

public class UserRoleNotFoundException extends RuntimeException{
    public UserRoleNotFoundException (String message){
        super(message);
    }
}

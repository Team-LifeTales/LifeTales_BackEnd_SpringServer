package com.LifeTales.domain.user.repository.DTO;

import com.LifeTales.domain.user.domain.AdminRole;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class UserAdminSignInDTO {
    private String id; //7~12 characters en&int
    private String pwd; //6~20 en&int& special symbol (!@#$)
    private String email; // 10 ~ 100
    private AdminRole role;

    public boolean nullCheck(){
        if(id.isEmpty() || id == null ||
                pwd.isEmpty() || pwd == null || email.isEmpty() || email == null || role == null){
            //not sent data
            return false;
        }else{
            return true;
        }
    }

    public boolean isValied(){
        if(id.length() < 7 || id.length() >12 || !id.matches("^[a-zA-Z0-9]*$")){
                //fail id valied
            return false;
        }
        if(pwd.length() < 6 || pwd.length() > 20 || !pwd.matches("^[a-zA-Z0-9!@#$]*$")){
            return false;
        }
        if(email.length() < 10 || email.length() > 100){
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if(matcher.matches() == false){
                return false;
            }
        }

        return true;
    }

}

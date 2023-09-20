package com.LifeTales.domain.user.repository.DTO.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class UserAdminLoginDTO {

    private String id; //7~12 characters en&int
    private String pwd; //6~20 en&int& special symbol (!@#$)

    public boolean nullCheck(){
        if(id.isEmpty() || id == null ||
                pwd.isEmpty() || pwd == null){
            //not sent data
            return true;
        }else{
            return false;
        }
    }

    public boolean isValied(){
        if(id.length() < 7 || id.length() >12 || !id.matches("^[a-zA-Z0-9]*$")){
            //fail id valied
            return true;
        }
        if(pwd.length() < 6 || pwd.length() > 20 || !pwd.matches("^[a-zA-Z0-9!@#$]*$")){
            return true;
        }

        return false;
    }
}

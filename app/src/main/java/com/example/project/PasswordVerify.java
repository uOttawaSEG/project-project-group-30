package com.example.project;

public class PasswordVerify {
    public boolean isValidPassword(String password){
        if(password==null ||password.isEmpty()){
            return false;
        }
        else if(password.length()<8){
            return false;
        }
        else {
            return true;
        }
    }
}

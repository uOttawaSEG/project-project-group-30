package com.example.project;

public class EmaiVerify {
    public boolean isValidEmail(String email){
        if(email==null ||email.isEmpty()){
            return false;
        }
        else {
            return (email.contains("@"));
        }

    }
}

package com.example.project;

public class AdminVerify {
     String adminEmail="admin@uottawa.ca";
     String adminPassword="Admin101";
     public boolean loginCheck(String email, String password){
        //checks if the email and password are correct
         return adminEmail.equals(email)&&adminPassword.equals(password);
     }
}

package com.example.project;

public class PhoneNumberVerify {
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        } else if (phoneNumber.length() != 10) {
            return false;
        } else {
            return true;
        }
    }

}

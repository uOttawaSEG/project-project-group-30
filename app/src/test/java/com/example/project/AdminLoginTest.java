package com.example.project;

import org.junit.Test;
import static org.junit.Assert.*;

public class AdminLoginTest {
    private AdminVerify verify = new AdminVerify();
    @Test
            public void correctTest() {
        boolean result = verify.loginCheck("admin@uottawa.ca", "Admin101");
        assertTrue("should be true", result);
    }
    @Test
    public void PasswordTest() {
        boolean result = verify.loginCheck("admin@uottawa.ca", "bad");
        assertFalse("should be false cause of wrong password", result);
    }

    @Test
    public void UserNameTest() {
        boolean result = verify.loginCheck("bad", "Admin101");
        assertFalse("should be false caused by wrong email", result);
    }
    @Test
    public void EmptyTest() {
        boolean result = verify.loginCheck("", "");
        assertFalse("should be false caused by nothing being entered", result);
    }
}

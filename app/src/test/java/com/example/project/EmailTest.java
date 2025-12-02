package com.example.project;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmailTest {
    private EmaiVerify verify = new EmaiVerify();
    @Test
    public void correctTest() {
        boolean result = verify.isValidEmail("admin@uottawa.ca");
        assertTrue("should be true", result);
    }
    @Test
    public void emptyTest() {
        boolean result = verify.isValidEmail("");
        assertFalse("should be false since the email is empty", result);
    }
    @Test
    public void wrongTest() {
        boolean result = verify.isValidEmail("asdf");
        assertFalse("should be false since the email has no @", result);
    }

}

package com.example.project;
import org.junit.Test;
import static org.junit.Assert.*;
public class PhoneNumberTest {
    private PhoneNumberVerify verify = new PhoneNumberVerify();
    @Test
    public void correctTest() {
        boolean result = verify.isValidPhoneNumber("1234567890");
        assertTrue("should be true", result);
    }
    @Test
    public void emptyTest() {
        boolean result = verify.isValidPhoneNumber("");
        assertFalse("should be false since the slot is empty", result);
    }
    @Test
    public void wrongTest() {
        boolean result = verify.isValidPhoneNumber("asdf");
        assertFalse("should be false since the slot isn't of length ten", result);
    }


}

package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Login class.
 * Test data is taken directly from the POE brief's assertEquals table,
 * which the brief states will be used to mark the task.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    // ---- assertTrue / assertFalse: username ----

    @Test
    void usernameCorrectlyFormattedReturnsTrue() {
        // "kyl_1" contains an underscore and is exactly five characters.
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormattedReturnsFalse() {
        // "kyle!!!!!!!" has no underscore and is longer than five characters.
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    // ---- assertTrue / assertFalse: password ----

    @Test
    void passwordMeetsComplexityReturnsTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordFailsComplexityReturnsFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // ---- assertTrue / assertFalse: cell phone ----

    @Test
    void cellPhoneCorrectlyFormattedReturnsTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormattedReturnsFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // ---- assertEquals: registration messages ----

    @Test
    void registerUserReturnsWelcomeOnSuccess() {
        // Correctly formatted username, password and cell number.
        String result = login.registerUser(
                "Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle Smith it is great to see you again.", result);
    }

    @Test
    void registerUserReturnsUsernameErrorWhenBadUsername() {
        String result = login.registerUser(
                "Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more than "
                + "five characters in length.", result);
    }

    @Test
    void registerUserReturnsPasswordErrorWhenBadPassword() {
        String result = login.registerUser(
                "Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a capital "
                + "letter, a number, and a special character.", result);
    }

    @Test
    void registerUserReturnsCellErrorWhenBadCell() {
        String result = login.registerUser(
                "Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not "
                + "contain international code.", result);
    }

    // ---- assertTrue / assertFalse: login ----

    @Test
    void loginSuccessfulReturnsTrue() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailedReturnsFalse() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong_user", "wrongpass"));
    }

    // ---- assertEquals: login status messages ----

    @Test
    void returnLoginStatusReturnsWelcomeOnSuccess() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", result);
    }

    @Test
    void returnLoginStatusReturnsErrorOnFailure() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.returnLoginStatus("wrong_user", "wrongpass");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}

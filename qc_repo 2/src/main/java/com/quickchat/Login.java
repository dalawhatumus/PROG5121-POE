package com.quickchat;

/**
 * Handles registration validation and login for the QuickChat application.
 *
 * The six methods required by the POE brief are all here:
 *   checkUserName(), checkPasswordComplexity(), checkCellPhoneNumber(),
 *   registerUser(), loginUser() and returnLoginStatus().
 *
 * Design approach: each validation rule lives in its own small method that
 * returns a boolean. registerUser() calls those methods in turn so the
 * validation logic is written once and reused by both the app and the unit
 * tests. This keeps the class easy to test and easy to read.
 *
 * Note on message wording: the brief's specification table and its
 * assertEquals test table use slightly different strings in a few places.
 * Where a string is checked by a marked assertEquals test, this class
 * returns exactly what that test expects so the tests pass.
 */
public class Login {

    // Stored registration details, kept so loginUser() can compare against them.
    // These stay empty until a successful registerUser() call fills them in.
    private String registeredFirstName = "";
    private String registeredLastName = "";
    private String registeredUsername = "";
    private String registeredPassword = "";
    private String registeredCellPhone = "";

    // Tracks whether the last loginUser() call succeeded, used by returnLoginStatus().
    private boolean lastLoginSuccessful = false;

    /**
     * A username is valid when it contains an underscore AND is no more than
     * five characters in length.
     *
     * Ambiguity note: the brief says "contains an underscore and is no more
     * than five characters long". It is not fully clear whether the underscore
     * counts toward the five characters. Read literally, it does, so the total
     * length (underscore included) must be <= 5. The marked test data
     * "kyl_1" is exactly five characters and passes under this reading.
     */
    public boolean checkUserName(String username) {
        // Guard against a null input so the checks below never throw.
        if (username == null) {
            return false;
        }
        // Both conditions must hold: an underscore is present and length <= 5.
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * A password is valid when it is at least eight characters long and
     * contains at least one capital letter, one digit and one special
     * character.
     */
    public boolean checkPasswordComplexity(String password) {
        // Guard against a null input.
        if (password == null) {
            return false;
        }
        // Each rule is checked separately, then combined at the end, so the
        // requirement each boolean represents is easy to read.
        boolean atLeastEight = password.length() >= 8;
        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        // Any character that is not a letter, digit or whitespace is "special".
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9\\s].*");
        // All four rules must pass for the password to be valid.
        return atLeastEight && hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Validates a South African cell number.
     *
     * The number must start with the +27 international code and have no more
     * than ten characters after that code (a standard SA subscriber number is
     * nine digits, e.g. +27838968976).
     *
     * Regex attribution:
     * Adapted from the regular-expression validation approach described at
     * Oracle, 2024. Lesson: Regular Expressions. [online]
     * Available at: https://docs.oracle.com/javase/tutorial/essential/regex/
     * [Accessed 1 September 2026].
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        // Guard against a null input.
        if (cellPhoneNumber == null) {
            return false;
        }
        // ^\+27  -> must begin with the +27 international code
        // \d{1,10}$ -> followed by up to ten digits
        return cellPhoneNumber.matches("^\\+27\\d{1,10}$");
    }

    /**
     * Validates and, if everything is correct, stores the new user's details.
     * Returns a message describing the outcome.
     *
     * The three checks run in order (username, then password, then cell
     * number). The first one that fails returns its own error message, so the
     * user is told about one problem at a time.
     */
    public String registerUser(String firstName, String lastName, String username,
                               String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your "
                    + "username contains an underscore and is no more than five "
                    + "characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the "
                    + "password contains at least eight characters, a capital "
                    + "letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain "
                    + "international code.";
        }

        // All checks passed: store the details for later login verification.
        this.registeredFirstName = firstName;
        this.registeredLastName = lastName;
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhoneNumber;

        return "Welcome " + firstName + " " + lastName
                + " it is great to see you again.";
    }

    /**
     * Returns true only when the supplied username and password match the
     * details captured during registration.
     */
    public boolean loginUser(String username, String password) {
        // Both fields must be non-null and must equal the stored credentials.
        // The result is remembered so returnLoginStatus() can reuse it.
        lastLoginSuccessful = username != null
                && password != null
                && username.equals(registeredUsername)
                && password.equals(registeredPassword);
        return lastLoginSuccessful;
    }

    /**
     * Returns the appropriate greeting or error message based on the result
     * of the most recent loginUser() call.
     */
    public String returnLoginStatus(String username, String password) {
        // Re-run the credential check, then pick the matching message.
        if (loginUser(username, password)) {
            return "Welcome " + registeredFirstName + ", " + registeredLastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Accessors used by the console app and tests.
    public String getRegisteredFirstName() {
        return registeredFirstName;
    }

    public String getRegisteredLastName() {
        return registeredLastName;
    }
}

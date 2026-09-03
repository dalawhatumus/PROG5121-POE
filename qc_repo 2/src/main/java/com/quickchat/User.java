package com.quickchat;

/**
 * Simple data holder for a registered user.
 * Stores the details captured during registration so the Login class
 * can verify them again when the user logs in.
 */
public class User {

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String cellPhoneNumber;

    public User(String firstName, String lastName, String username,
                String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}

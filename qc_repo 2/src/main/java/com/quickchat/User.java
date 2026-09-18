package com.quickchat;

/**
 * Simple data holder for a registered user.
 * Stores the details captured during registration so the Login class
 * can verify them again when the user logs in.
 *
 * All fields are final: once a User is created its details cannot change,
 * which keeps a registered user's data consistent for the life of the object.
 */
public class User {

    // The five registration details, set once via the constructor.
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String cellPhoneNumber;

    // Constructor: takes all five details and stores them in this User.
    public User(String firstName, String lastName, String username,
                String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // Getter methods below give read-only access to each stored detail.
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

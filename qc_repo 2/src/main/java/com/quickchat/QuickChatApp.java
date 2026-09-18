package com.quickchat;

import java.util.Scanner;

/**
 * Console entry point for QuickChat Part 1.
 * Walks the user through registration, then requires a successful login.
 * Console only, as required by the brief (no GUI / no JOptionPane).
 *
 * Flow: read and validate each field one at a time, register the user once
 * all fields are valid, then loop on the login step until the credentials
 * match. All validation is delegated to the Login class so this file only
 * deals with input and output.
 *
 * Module:  PROG5121 - Programming 1A
 * Student: Abdalla Adel (ST10536920)
 */
public class QuickChatApp {

    public static void main(String[] args) {
        // Scanner reads the user's keyboard input; Login holds the logic.
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== QuickChat Registration ===");

        // Keep asking until each field is valid, giving clear feedback each time.
        String firstName = prompt(scanner, "Enter your first name: ");
        String lastName = prompt(scanner, "Enter your last name: ");

        // Username loop: repeat until checkUserName() accepts the input.
        String username;
        do {
            username = prompt(scanner, "Choose a username "
                    + "(must contain an underscore, max five characters): ");
            if (!login.checkUserName(username)) {
                System.out.println("Username is not correctly formatted; please "
                        + "ensure that your username contains an underscore and "
                        + "is no more than five characters in length.");
            }
        } while (!login.checkUserName(username));
        System.out.println("Username successfully captured.");

        // Password loop: repeat until all four complexity rules are met.
        String password;
        do {
            password = prompt(scanner, "Choose a password "
                    + "(min 8 chars, a capital, a number and a special character): ");
            if (!login.checkPasswordComplexity(password)) {
                System.out.println("Password is not correctly formatted; please "
                        + "ensure that the password contains at least eight "
                        + "characters, a capital letter, a number, and a special "
                        + "character.");
            }
        } while (!login.checkPasswordComplexity(password));
        System.out.println("Password successfully captured.");

        // Cell number loop: repeat until the +27 format is matched.
        String cellPhone;
        do {
            cellPhone = prompt(scanner, "Enter your cell number "
                    + "(with +27 international code, e.g. +27838968976): ");
            if (!login.checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number incorrectly formatted or "
                        + "does not contain international code.");
            }
        } while (!login.checkCellPhoneNumber(cellPhone));
        System.out.println("Cell phone number successfully added.");

        // Store the fully validated user.
        String registrationResult = login.registerUser(
                firstName, lastName, username, password, cellPhone);
        System.out.println(registrationResult);

        // Login stage: verify credentials before granting access. The loop
        // keeps prompting until returnLoginStatus reports a successful match.
        System.out.println("\n=== QuickChat Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            String loginUsername = prompt(scanner, "Username: ");
            String loginPassword = prompt(scanner, "Password: ");
            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
            loggedIn = login.loginUser(loginUsername, loginPassword);
        }

        System.out.println("\nYou are now logged in. (Messaging features arrive in Part 2.)");
        // Close the scanner to release the input resource.
        scanner.close();
    }

    /** Helper that prints a prompt and returns the trimmed user input. */
    private static String prompt(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}

package com.quickchat;

import java.util.Scanner;

/**
 * Console entry point for QuickChat (Parts 1 and 2).
 *
 * Part 1: walks the user through registration, then requires a successful
 * login. Part 2: once logged in, shows the QuickChat messaging menu, which
 * lets the user send a set number of messages, build each message's hash, and
 * send, store or disregard it. Console only, as required by the brief
 * (no GUI / no JOptionPane).
 */
public class QuickChatApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== QuickChat Registration ===");

        // Keep asking until each field is valid, giving clear feedback each time.
        String firstName = prompt(scanner, "Enter your first name: ");
        String lastName = prompt(scanner, "Enter your last name: ");

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

        // Login stage: verify credentials before granting access.
        System.out.println("\n=== QuickChat Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            String loginUsername = prompt(scanner, "Username: ");
            String loginPassword = prompt(scanner, "Password: ");
            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
            loggedIn = login.loginUser(loginUsername, loginPassword);
        }

        // Messaging is only available after a successful login (brief, task 1).
        runMessagingMenu(scanner);

        scanner.close();
    }

    /**
     * Part 2 messaging menu. Displays the welcome message once, then loops on a
     * numeric menu (1 send, 2 coming soon, 3 quit) until the user quits.
     */
    private static void runMessagingMenu(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            String choice = prompt(scanner, "Enter your choice (1-3): ");

            switch (choice) {
                case "1":
                    sendMessages(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }

        System.out.println("\nTotal messages sent: " + new Message(0, "", "").returnTotalMessages());
        System.out.println("Goodbye.");
    }

    /**
     * Asks how many messages to send, then uses a for loop to collect exactly
     * that many. Each message is validated, its hash is built, and the user
     * chooses to send, store or disregard it.
     */
    private static void sendMessages(Scanner scanner) {
        int numMessages = readPositiveInt(scanner,
                "How many messages would you like to send? ");

        // For loop: runs once per message the user asked to enter.
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

            // Recipient: keep asking until it is correctly formatted.
            String recipient;
            String recipientResult;
            do {
                recipient = prompt(scanner, "Enter the recipient cell number "
                        + "(with +27 international code): ");
                recipientResult = new Message(i, recipient, "").checkRecipientCell();
                System.out.println(recipientResult);
            } while (!recipientResult.equals("Cell phone number successfully captured."));

            // Message body: keep asking until it is 250 characters or fewer.
            String text;
            Message message;
            while (true) {
                text = prompt(scanner, "Enter your message (max 250 characters): ");
                message = new Message(i, recipient, text);
                String lengthResult = message.checkMessageLength();
                if (lengthResult.equals("Message ready to send.")) {
                    System.out.println("Message sent.");
                    break;
                }
                System.out.println(lengthResult);
            }

            // Ask what to do with the completed message.
            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int action = readPositiveInt(scanner, "Enter your choice (1-3): ");
            System.out.println(message.sentMessage(action));

            // Show the full details of the message, in the required order.
            System.out.println("\n" + message.printMessages());
        }

        // Accumulated total once all the messages have been handled.
        System.out.println("\nTotal messages sent so far: "
                + new Message(0, "", "").returnTotalMessages());
    }

    /** Helper that prints a prompt and returns the trimmed user input. */
    private static String prompt(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a whole number that is zero or more, re-prompting on bad input so a
     * stray letter does not crash the program.
     */
    private static int readPositiveInt(Scanner scanner, String message) {
        while (true) {
            String input = prompt(scanner, message);
            try {
                int value = Integer.parseInt(input);
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the re-prompt below
            }
            System.out.println("Please enter a valid whole number.");
        }
    }
}

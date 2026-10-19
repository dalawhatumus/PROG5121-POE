package com.quickchat;

import java.util.Random;

/**
 * Represents a single QuickChat message and the operations the POE Part 2
 * brief requires on it: validating the message ID and recipient, building the
 * message hash, letting the user send/store/disregard, and reporting totals.
 *
 * The brief's "Message class" methods are all here:
 *   checkMessageID(), checkRecipientCell(), createMessageHash(),
 *   sentMessage(), printMessages(), returnTotalMessages()
 * plus the researched storeMessage() that writes to a JSON file
 * (see MessageStore, which this class delegates to).
 *
 * Note on wording: where a string is checked by a marked assertEquals test,
 * this class returns exactly what that test expects so the tests pass.
 */
public class Message {

    // Running total of messages sent in this program run. Static so it is
    // shared across every Message object and survives for the whole session.
    private static int totalMessagesSent = 0;

    // The details that make up one message.
    private final String messageID;      // random ten-digit tracking number
    private final int messageNumber;     // which message this is (0, 1, 2, ...)
    private final String recipient;      // recipient cell number (+27...)
    private final String messageText;    // the message body (max 250 chars)
    private String messageHash;          // auto-generated hash, set on build

    /**
     * Builds a message. The message ID is generated automatically; the message
     * number is supplied by the sending loop so the hash can include it.
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /**
     * Builds a message with a supplied message ID instead of a random one.
     * Used by the unit tests so the hash can be checked against the fixed test
     * data from the brief; not used during normal program use.
     */
    public Message(String messageID, int messageNumber, String recipient,
                   String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /**
     * Generates a random ten-digit message ID as a string. Kept as a string so
     * leading zeros are preserved and checkMessageID() can measure its length.
     */
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10)); // one digit 0-9 at a time
        }
        return id.toString();
    }

    /**
     * A message ID is valid when it is not more than ten characters long.
     * Returns true when the ID meets that rule.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Checks that the recipient cell number is no more than ten characters
     * after the international code and starts with the +27 code. The validation
     * rule is reused from Part 1 (Login.checkCellPhoneNumber), as the brief
     * hints, so the behaviour stays consistent across the project.
     *
     * Returns the exact success or failure message the marked tests expect.
     */
    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    /**
     * Creates the message hash from: the first two digits of the message ID,
     * a colon, the message number, a colon, then the first and last words of
     * the message joined together. The whole hash is returned in capitals.
     *
     * Example from the brief: ID starting "00", message 0, text "Hi ... tonight"
     * gives 00:0:HITONIGHT.
     */
    public String createMessageHash() {
        // First two characters of the ten-digit ID (string manipulation).
        String idStart = messageID.substring(0, 2);

        // Split the message into words so we can take the first and last.
        String trimmed = messageText == null ? "" : messageText.trim();
        String firstWord = "";
        String lastWord = "";
        if (!trimmed.isEmpty()) {
            String[] words = trimmed.split("\\s+");
            firstWord = words[0];
            lastWord = words[words.length - 1];
        }

        // Strip punctuation so "tonight?" becomes "tonight" in the hash.
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        String hash = idStart + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    /**
     * Lets the user choose what to do with a completed message. The numeric
     * choice is passed in (1 send, 2 disregard, 3 store) and the matching
     * message is returned. Sending or storing also updates the running total.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                MessageStore.storeMessage(this); // write to JSON (researched)
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    /**
     * Returns the full details of this message for display, in the order the
     * brief asks for: Message ID, Message Hash, Recipient, Message.
     */
    public String printMessages() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    /**
     * Returns the total number of messages sent so far in this run.
     */
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Checks the message length. Returns the success or failure string the
     * marked tests expect, including the number of characters the message is
     * over the 250 limit on failure.
     */
    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int over = (messageText == null ? 0 : messageText.length()) - 250;
        return "Message exceeds 250 characters by " + over
                + ", please reduce the size.";
    }

    // Accessors used by the app, the JSON store and the tests.
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Resets the running total. Used by the unit tests so each test starts from
     * a known count; not called during normal program use.
     */
    public static void resetTotal() {
        totalMessagesSent = 0;
    }
}

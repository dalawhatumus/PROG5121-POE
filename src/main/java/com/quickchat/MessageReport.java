package com.quickchat;

import java.util.ArrayList;
import java.util.List;

/**
 * Part 3 of the POE: stores messages in arrays and produces the task report.
 *
 * This class keeps the five arrays the brief asks for (sent, disregarded,
 * stored, message hashes and message IDs) and provides the operations on them:
 * displaying senders and recipients, finding the longest message, searching by
 * message ID or recipient, deleting by message hash, and printing a full
 * report. The stored-messages array can also be filled from the JSON file.
 *
 * The arrays are kept as ArrayLists (dynamic arrays) so they can be populated
 * at run time with no hard-coding, as the brief requires.
 */
public class MessageReport {

    // The five arrays required by the brief, kept in step with one another.
    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    /**
     * Adds a message to the correct array based on its flag, and records its
     * hash and ID in the parallel hash and ID arrays. "Sent" and "Stored"
     * messages are both kept (sent in the sent array, stored in the stored
     * array); "Disregarded" messages go to the disregarded array.
     */
    public void addMessage(Message message) {
        if (isDisregarded(message.getFlag())) {
            disregardedMessages.add(message);
            return; // disregarded messages are not tracked in the other arrays
        }

        // The message is kept, so record it in the hash and ID arrays.
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());

        if ("Stored".equalsIgnoreCase(message.getFlag())) {
            storedMessages.add(message);
        } else {
            sentMessages.add(message);
        }
    }

    /**
     * Returns true when a flag means the message was disregarded. Keeping this
     * in one place avoids repeating the same comparison in several methods.
     */
    private boolean isDisregarded(String flag) {
        return "Disregarded".equalsIgnoreCase(flag)
                || "Disregard".equalsIgnoreCase(flag);
    }

    /**
     * Reads the stored messages from the JSON file (written in Part 2) into the
     * stored-messages array. This reuses MessageStore, which uses Gson; the JSON
     * handling is attributed in that class.
     */
    public void loadStoredFromJson() {
        for (MessageStore.StoredMessage s : MessageStore.readStoredMessages()) {
            Message m = new Message(s.getMessageID(), s.getMessageNumber(),
                    s.getRecipient(), s.getMessage());
            m.setFlag("Stored");
            storedMessages.add(m);
        }
    }

    /**
     * Returns the sender and recipient of every sent message, one per line.
     * (The sender is the logged-in QuickChat user; the recipient is stored on
     * the message.)
     */
    public String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns the text of the longest message across the sent and stored
     * arrays. If there are no messages, an empty string is returned.
     */
    public String displayLongestMessage() {
        String longest = "";
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        for (Message m : all) {
            if (m.getMessageText() != null
                    && m.getMessageText().length() > longest.length()) {
                longest = m.getMessageText();
            }
        }
        return longest;
    }

    /**
     * Searches the sent messages for a message ID and returns the matching
     * recipient and message. Returns a not-found message when the ID is absent.
     */
    public String searchByMessageID(String id) {
        for (Message m : sentMessages) {
            if (m.getMessageID().equals(id)) {
                return m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    /**
     * Returns all the messages (sent or stored) for a particular recipient,
     * each on its own line. Returns a not-found message when there are none.
     */
    public String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                sb.append(m.getMessageText()).append("\n");
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                sb.append(m.getMessageText()).append("\n");
            }
        }
        String result = sb.toString().trim();
        return result.isEmpty() ? "No messages found for that recipient." : result;
    }

    /**
     * Deletes a message by its hash, searching the sent and stored arrays.
     * Returns a confirmation naming the deleted message, or a not-found message.
     */
    public String deleteByHash(String hash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                String text = sentMessages.get(i).getMessageText();
                sentMessages.remove(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hash)) {
                String text = storedMessages.get(i).getMessageText();
                storedMessages.remove(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message with that hash not found.";
    }

    /**
     * Builds a full report of every sent message, showing the message hash,
     * recipient and message for each, as the brief requires.
     */
    public String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Message Report ---\n");
        int count = 1;
        for (Message m : sentMessages) {
            sb.append("Message ").append(count).append(":\n");
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append("Message: ").append(m.getMessageText()).append("\n");
            sb.append("\n");
            count++;
        }
        return sb.toString().trim();
    }

    // Accessors for the arrays, used by the app and the unit tests.
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public List<Message> getDisregardedMessages() {
        return disregardedMessages;
    }

    public List<Message> getStoredMessages() {
        return storedMessages;
    }

    public List<String> getMessageHashes() {
        return messageHashes;
    }

    public List<String> getMessageIDs() {
        return messageIDs;
    }
}

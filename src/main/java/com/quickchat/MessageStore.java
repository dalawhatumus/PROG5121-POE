package com.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores QuickChat messages in a JSON file and reads them back.
 *
 * This is the researched "store the messages in JSON" part of the brief.
 * The JSON reading and writing is done with Gson, Google's JSON library.
 *
 * JSON handling attribution:
 * Google, 2024. Gson User Guide. [online]
 * Available at: https://github.com/google/gson/blob/main/UserGuide.md
 * [Accessed 15 October 2026].
 */
public class MessageStore {

    // The file the stored messages are written to and read from.
    private static final String STORE_FILE = "stored_messages.json";

    // Pretty-printing makes the saved JSON file easy to read when marking.
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Appends one message to the JSON store. Existing stored messages are read
     * first so nothing is lost, the new message is added, and the whole list is
     * written back out.
     */
    public static void storeMessage(Message message) {
        List<StoredMessage> messages = readStoredMessages();
        messages.add(StoredMessage.from(message));
        try (FileWriter writer = new FileWriter(STORE_FILE)) {
            GSON.toJson(messages, writer);
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    /**
     * Reads the stored messages back from the JSON file into a list. Returns an
     * empty list when the file does not exist yet or cannot be read.
     */
    public static List<StoredMessage> readStoredMessages() {
        try (FileReader reader = new FileReader(STORE_FILE)) {
            Type listType = new TypeToken<ArrayList<StoredMessage>>() { }.getType();
            List<StoredMessage> messages = GSON.fromJson(reader, listType);
            return messages == null ? new ArrayList<>() : messages;
        } catch (IOException e) {
            // No file yet (first run) or it could not be read: start empty.
            return new ArrayList<>();
        }
    }

    /**
     * A plain data holder describing one stored message as it appears in the
     * JSON file. Keeping this separate from Message keeps the JSON structure
     * simple and avoids storing the static total.
     */
    public static class StoredMessage {
        private String messageID;
        private int messageNumber;
        private String recipient;
        private String message;
        private String messageHash;

        public static StoredMessage from(Message m) {
            StoredMessage s = new StoredMessage();
            s.messageID = m.getMessageID();
            s.messageNumber = m.getMessageNumber();
            s.recipient = m.getRecipient();
            s.message = m.getMessageText();
            s.messageHash = m.getMessageHash();
            return s;
        }

        public String getMessageID() {
            return messageID;
        }

        public int getMessageNumber() {
            return messageNumber;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getMessage() {
            return message;
        }

        public String getMessageHash() {
            return messageHash;
        }
    }
}

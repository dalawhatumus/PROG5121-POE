package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the Message class (POE Part 2).
 *
 * The test data and the expected return strings are taken directly from the
 * brief's test tables, so passing these tests shows the application behaves the
 * way the marking guide expects.
 *
 * Test data from the brief:
 *   Message 1: recipient +27718693002,
 *              "Hi Mike, can you join us for dinner tonight?", Send.
 *   Message 2: recipient 08575975889,
 *              "Hi Keegan, did you receive the payment?", Discard.
 */
class MessageTest {

    // Reset the shared sent-message total before each test so counts are known.
    @BeforeEach
    void resetTotals() {
        Message.resetTotal();
    }

    // ---- Message length (max 250 characters) ----

    @Test
    void messageReadyWhenWithinLimit() {
        Message message = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    void messageFailsWhenTooLong() {
        // 255 characters: five over the 250 limit.
        String longText = "a".repeat(255);
        Message message = new Message(0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 5, please reduce the size.",
                message.checkMessageLength());
    }

    // ---- Recipient cell number (reused from Part 1) ----

    @Test
    void recipientSuccessWhenCorrectlyFormatted() {
        Message message = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    void recipientFailsWhenNoInternationalCode() {
        // Message 2's recipient from the brief has no +27 code.
        Message message = new Message(1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number and "
                + "try again.", message.checkRecipientCell());
    }

    // ---- Message hash ----

    @Test
    void messageHashIsCorrectForTestCaseOne() {
        // Fixed ID starting "00" so the hash is deterministic, message number 0.
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    // ---- Message ID ----

    @Test
    void messageIDIsNotMoreThanTenCharacters() {
        Message message = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
        assertTrue(message.getMessageID().length() <= 10);
    }

    @Test
    void messageIDFailsWhenTooLong() {
        // An eleven-character ID is too long.
        Message message = new Message("01234567890", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertFalse(message.checkMessageID());
    }

    // ---- SentMessage choices ----

    @Test
    void sentMessageReturnsSentOnSend() {
        Message message = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void sentMessageReturnsDeleteOnDisregard() {
        Message message = new Message(1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    void sentMessageReturnsStoredOnStore() {
        Message message = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    // ---- Total messages ----

    @Test
    void totalIncrementsOnlyWhenSent() {
        Message first = new Message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        first.sentMessage(1); // send -> counts

        Message second = new Message(1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        second.sentMessage(2); // disregard -> does not count

        assertEquals(1, second.returnTotalMessages());
    }
}

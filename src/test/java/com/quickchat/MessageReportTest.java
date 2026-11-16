package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Part 3 array and report features (MessageReport).
 *
 * The test data and expected responses are taken directly from the brief's
 * Part 3 test table, so passing these tests shows the arrays are populated and
 * searched the way the marking guide expects.
 *
 * Test data from the brief (five messages):
 *   1. +27834557896  "Did you get the cake?"                                  Sent
 *   2. +27838884567  "Where are you? You are late! I have asked you to be on time."  Stored
 *   3. +27834484567  "Yohoooo, I am at your gate."                            Disregarded
 *   4. 0838884567    "It is dinner time!"                                     Sent
 *   5. +27838884567  "Ok, I am leaving without you."                          Stored
 */
class MessageReportTest {

    private MessageReport report;
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;

    @BeforeEach
    void setUp() {
        report = new MessageReport();

        // Fixed message IDs so the hashes are deterministic for the delete test.
        m1 = new Message("1000000001", 0, "+27834557896", "Did you get the cake?");
        m1.setFlag("Sent");
        m2 = new Message("2000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        m2.setFlag("Stored");
        m3 = new Message("3000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
        m3.setFlag("Disregarded");
        m4 = new Message("4000000004", 3, "0838884567", "It is dinner time!");
        m4.setFlag("Sent");
        m5 = new Message("5000000005", 4, "+27838884567", "Ok, I am leaving without you.");
        m5.setFlag("Stored");

        report.addMessage(m1);
        report.addMessage(m2);
        report.addMessage(m3);
        report.addMessage(m4);
        report.addMessage(m5);
    }

    // ---- Sent messages array correctly populated ----

    @Test
    void sentArrayContainsExpectedMessages() {
        // The two Sent messages are message 1 and message 4.
        assertEquals(2, report.getSentMessages().size());
        assertEquals("Did you get the cake?",
                report.getSentMessages().get(0).getMessageText());
        assertEquals("It is dinner time!",
                report.getSentMessages().get(1).getMessageText());
    }

    // ---- Display the longest message ----

    @Test
    void longestMessageIsReturned() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                report.displayLongestMessage());
    }

    // ---- Search for a message ID ----

    @Test
    void searchByMessageIDReturnsMessage() {
        // Message 4's ID returns its message text.
        assertEquals("It is dinner time!",
                report.searchByMessageID("4000000004"));
    }

    // ---- Search for all messages for a recipient ----

    @Test
    void searchByRecipientReturnsAllMatches() {
        String result = report.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // ---- Delete a message using its hash ----

    @Test
    void deleteByHashRemovesMessage() {
        String hash = m2.getMessageHash();
        assertEquals("Message: \"Where are you? You are late! I have asked you to be "
                + "on time.\" successfully deleted.", report.deleteByHash(hash));
    }

    // ---- Disregarded and stored arrays populated correctly ----

    @Test
    void disregardedAndStoredArraysPopulated() {
        assertEquals(1, report.getDisregardedMessages().size());
        assertEquals(2, report.getStoredMessages().size());
    }

    // ---- Report displays the sent messages ----

    @Test
    void reportContainsSentMessageDetails() {
        String report1 = report.displayReport();
        assertTrue(report1.contains("Did you get the cake?"));
        assertTrue(report1.contains("It is dinner time!"));
        assertTrue(report1.contains("Message Hash:"));
        assertTrue(report1.contains("Recipient:"));
    }
}

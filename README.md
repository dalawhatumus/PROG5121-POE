# QuickChat (PROG5121 POE)

**Student:** Abdalla Adel
**Student Number:** ST10536920
**Module:** PROG5121 — Programming 1A

A console messaging application built over the three POE parts. Part 1 covers account registration, input validation, and login. Part 2 adds the messaging feature: a menu, sending a set number of messages, building a message hash, and storing messages in a JSON file.

## What it does

**Part 1 — registration and login.** The app asks for a first name, last name, username, password, and cell number. Each field is validated before it is accepted:

- **Username** must contain an underscore and be no more than five characters long.
- **Password** must be at least eight characters and include a capital letter, a number, and a special character.
- **Cell number** must start with the +27 international code and have up to ten digits after it. The number is checked with a regular expression.

Once all three are valid, the details are stored and you move on to login. Login only succeeds when the username and password match what was registered.

**Part 2 — sending messages.** After a successful login the app displays `Welcome to QuickChat.` and a numeric menu:

1. Send Messages
2. Show recently sent messages — displays `Coming Soon.`
3. Quit

The app loops until you quit. When you choose to send, it asks how many messages you want to enter and then uses a for loop to collect exactly that many. For each message it validates the recipient (+27 format) and the message length (max 250 characters), builds a message hash, and lets you send, store, or disregard it. Stored messages are written to a JSON file.

## Project layout

```
src/main/java/com/quickchat/
    User.java           Data holder for a registered user
    Login.java          Part 1 validation and login logic (six required methods)
    Message.java        Part 2 message logic (ID, recipient, hash, send/store, totals)
    MessageStore.java   Stores and reads messages as JSON (using Gson)
    QuickChatApp.java   Console entry point (registration, login, messaging menu)
src/test/java/com/quickchat/
    LoginTest.java      JUnit 5 tests for Part 1, using the brief's test data
    MessageTest.java    JUnit 5 tests for Part 2, using the brief's test data
.github/workflows/
    TestJava.yml        Runs all the tests on every push (GitHub Actions)
pom.xml                 Maven build with JUnit 5 and Gson
```

## The Login methods (Part 1)

| Method | What it does |
|---|---|
| `checkUserName` | Returns true when the username has an underscore and is five characters or fewer. |
| `checkPasswordComplexity` | Returns true when the password meets all four complexity rules. |
| `checkCellPhoneNumber` | Returns true when the number matches the +27 format. |
| `registerUser` | Runs all three checks and returns a message. Stores the user on success. |
| `loginUser` | Returns true when the supplied credentials match the stored ones. |
| `returnLoginStatus` | Returns the welcome message or the error message for the last login. |

## The Message methods (Part 2)

| Method | What it does |
|---|---|
| `checkMessageID` | Returns true when the message ID is not more than ten characters. |
| `checkRecipientCell` | Checks the recipient number (reusing the Part 1 rule) and returns the success or failure message. |
| `createMessageHash` | Builds the hash: first two ID digits, message number, first and last words, in capitals (e.g. `00:0:HITONIGHT`). |
| `sentMessage` | Lets the user send, disregard, or store the message, and returns the matching message. |
| `printMessages` | Returns the full message details in the required order (ID, hash, recipient, message). |
| `returnTotalMessages` | Returns the total number of messages sent in this run. |
| `storeMessage` (in `MessageStore`) | Writes the message to a JSON file. |

## Running it

The project is built with Maven and opened in IntelliJ IDEA.

To run the app in IntelliJ: open the project folder, then open `QuickChatApp.java` and run the `main` method.

To run it from the command line with Maven and a JDK installed:

```
mvn compile
mvn exec:java -Dexec.mainClass=com.quickchat.QuickChatApp
```

## Running the tests

```
mvn test
```

In IntelliJ you can also right-click `LoginTest.java` or `MessageTest.java` and choose Run. The tests use the exact input and output values from the assessment brief, so they double as a check that the app behaves the way the marking guide expects.

## A note on message wording

The brief's specification table and its test table use slightly different wording in a few spots. Where a value is checked by one of the marked tests, the code returns exactly what that test expects. The username length rule is read literally: five characters total, including the underscore, so `kyl_1` passes.

## Attribution

- The cell number regular expression was adapted from Oracle's regular expressions tutorial (https://docs.oracle.com/javase/tutorial/essential/regex/). See the comment above `checkCellPhoneNumber` in `Login.java`.
- The JSON storage uses Gson, Google's JSON library (https://github.com/google/gson). See the comment above `MessageStore`.

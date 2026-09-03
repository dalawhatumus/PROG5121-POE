# QuickChat (PROG5121 POE) - Part 1

**Student:** Abdalla Adel
**Student Number:** ST10536920
**Module:** PROG5121 - Programming 1A

A console application for registering and logging in users. This is the first of three parts. Part 1 covers account registration, input validation, and login.

## What it does

When you run the app, it asks for a first name, last name, username, password, and cell number. Each field is validated before it is accepted:

- **Username** must contain an underscore and be no more than five characters long.
- **Password** must be at least eight characters and include a capital letter, a number, and a special character.
- **Cell number** must start with the +27 international code and have up to ten digits after it. The number is checked with a regular expression.

Once all three are valid, the details are stored and you move on to the login step. Login only succeeds when the username and password match what was registered.

## Project layout

```
src/main/java/com/quickchat/
    User.java           Data holder for a registered user
    Login.java          Validation and login logic (six required methods)
    QuickChatApp.java   Console entry point
src/test/java/com/quickchat/
    LoginTest.java      JUnit 5 tests using the brief's test data
.github/workflows/
    TestJava.yml        Runs the tests on every push
pom.xml                 Maven build with JUnit 5
```

## The Login methods

| Method | What it does |
|---|---|
| `checkUserName` | Returns true when the username has an underscore and is five characters or fewer. |
| `checkPasswordComplexity` | Returns true when the password meets all four complexity rules. |
| `checkCellPhoneNumber` | Returns true when the number matches the +27 format. |
| `registerUser` | Runs all three checks and returns a message. Stores the user on success. |
| `loginUser` | Returns true when the supplied credentials match the stored ones. |
| `returnLoginStatus` | Returns the welcome message or the error message for the last login. |

## Running it

With Maven and a JDK installed:

```
mvn compile
mvn exec:java -Dexec.mainClass=com.quickchat.QuickChatApp
```

Or open the folder in NetBeans and run `QuickChatApp`.

## Running the tests

```
mvn test
```

The tests use the exact input and output values from the assessment brief, so they double as a check that the app behaves the way the marking guide expects.

## A note on message wording

The brief's specification table and its test table use slightly different wording in a few spots. Where a value is checked by one of the marked tests, the code returns exactly what that test expects. The username length rule is read literally: five characters total, including the underscore, so `kyl_1` passes.

## Attribution

The cell number regular expression was adapted from Oracle's regular expressions tutorial (https://docs.oracle.com/javase/tutorial/essential/regex/). See the comment above `checkCellPhoneNumber` in `Login.java`.

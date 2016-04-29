# PocketBookImporter

PocketBookImporter let's you reap the benefits of PocketBook (https://getpocketbook.com/overview) without sharing your banking credentials.

## Current functionality
- Supports BankWest export
- Can be re-run as often as you like. (Removes duplicates before uploading to PocketBook)
- Removes Authorisation only transactions

## Steps to improve
- Support multiple banks
- Working directory could set itself up
- Flexibility on how many accounts are used

## Steps to get started:
1. Import using existing resources
2. Go to Main.java and fill in the top variables
    private static String accountName = ""; // This is the label for account number
    private static String cardName = "";    // Allows for a another account to be imported

    private static String bankWestUserName = "";
    private static String bankWestPassword = "";

    private static String pocketBookUserName = "";
    private static String pocketBookPassword = "";

    private static String workingDirectory = "";  // This folder is the root working folder
    private static String importedTransactionsFolder = ""; // This is a sub-folder where your transactions get downloaded to
    private static String cleanTransactionsFolder = ""; // This stores the cleansed transactions (removes duplicates and authorisation only transactions
3. If you are with another bank, please contribute by extending "dowloadTransactions();"
4. Once done, build a jar and execute it using "java -jar pocketbookimporter.jar"
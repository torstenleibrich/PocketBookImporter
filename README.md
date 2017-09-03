# PocketBookImporter

PocketBookImporter let's you realise the benefits of PocketBook (https://getpocketbook.com/overview) without compromising your credentials of your bank.

## Current functionality
- Exports the last 90 days of your BankWest history
- You can re-run the script as often as you like. (Duplicates are removed before uploading to PocketBook)
- Removes Authorisation only transactions

## Steps to improve
- Support multiple banks
- Working directory could set itself up
- Flexibility on how many accounts are used

## Steps to get started:
1. Import project into your IDE of your choice using option: Existing resources
2. Go to resources/config.properties to enter your bank details
3. Update the script to load from accounts you prefer in Main.java/dowloadTransactions()
3. If you are with another bank, please contribute by extending "dowloadTransactions();"
4. Once done, build a jar and execute it using "java -jar pocketbookimporter.jar"
package com.company;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.List;

public class Main {
    private static String accountName = "";
    private static String cardName = "";

    private static String bankWestUserName = "";
    private static String bankWestPassword = "";

    private static String pocketBookUserName = "";
    private static String pocketBookPassword = "";

    private static String workingDirectory = "";
    private static String importedTransactionsFolder = "";
    private static String cleanTransactionsFolder = "";

    private static WebDriver driver = new Driver().getDriver(workingDirectory);
    private static Navigation navigateTo = new Navigation(driver);


    public static void main(String[] args) {
        dowloadTransactions();
        cleanTransactions();
        uploadTransactions();
        driver.quit();
    }

    private static void uploadTransactions() {
        LoginPageForPocketbook loginPageForPocketbook = navigateTo.loginPageForPocketBook();
        loginPageForPocketbook.login(pocketBookUserName, pocketBookPassword);

        SettingPage settingPage = navigateTo.settingPage();
        boolean uploadSuccessful = settingPage.uploadNewTransactions(cleanTransactionsFolder);

        if (uploadSuccessful){
            moveNewImportsToImportedFolder();
            deleteDownloadedFiles();
        }
    }

    private static void dowloadTransactions() {

        LoginPageForBankWest loginPageForBankWest = navigateTo.loginPageForBankWest();
        loginPageForBankWest.login(bankWestUserName, bankWestPassword);

        AccountPage accountPage = new AccountPage(driver);


        accountPage.openAccount(accountName);

        TransactionPage transactionPage = new TransactionPage(driver);
        transactionPage.downloadTransactions();

        accountPage = navigateTo.AccountPage();
        accountPage.openAccount(cardName);
        transactionPage.downloadTransactions();
    }

    private static void cleanTransactions() {
        TransactionsManager transactionsManager = new TransactionsManager();
        transactionsManager.cleanFilesIn(workingDirectory, cleanTransactionsFolder, importedTransactionsFolder);
    }

    private static void deleteDownloadedFiles() {
        TransactionsManager transactionsManager = new TransactionsManager();
        List<File> newFiles = transactionsManager.getAllFilesIn(workingDirectory);
        transactionsManager.deleteFilesInFolder(newFiles,workingDirectory);
    }

    private static void moveNewImportsToImportedFolder() {
        TransactionsManager transactionsManager = new TransactionsManager();
        transactionsManager.moveAndMergeTransactionFiles(cleanTransactionsFolder, importedTransactionsFolder);
        TransactionList mergedTransactionList = transactionsManager.getContentOfFiles(transactionsManager.getAllFilesIn(importedTransactionsFolder));

        TransactionList mergedListWithTransactionsYoungerThan90Days = transactionsManager.removeOutdatedTransactionsFrom(mergedTransactionList, 90);
        transactionsManager.saveTransactionInFiles(mergedListWithTransactionsYoungerThan90Days, importedTransactionsFolder);
        transactionsManager.deleteFilesInFolder(transactionsManager.getAllFilesIn(cleanTransactionsFolder), cleanTransactionsFolder);
    }
}

package com.company;

import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main {
    private static Properties properties = getProperties();

    private static String heroAccount = properties.getProperty("heroAccount");
    private static String spendingAccount = properties.getProperty("spendingAccount");
    private static String creditCard = properties.getProperty("creditCard");

    private static String bankWestUserName = properties.getProperty("bankWestUserName");
    private static String bankWestPassword = properties.getProperty("bankWestPassword");

    private static String pocketBookUserName = properties.getProperty("pocketBookUserName");
    private static String pocketBookPassword = properties.getProperty("pocketBookPassword");

    private static String workingDirectory = properties.getProperty("workingDirectory");
    private static String importedTransactionsFolder = properties.getProperty("importedTransactionsFolder");
    private static String cleanTransactionsFolder = properties.getProperty("cleanTransactionsFolder");

    private static WebDriver driver = new Driver().getDriver(workingDirectory);

    private static Navigation navigateTo = new Navigation(driver);


    public static void main(String[] args) {
        properties = getProperties();
        dowloadTransactions();
        cleanTransactions();
        uploadTransactions();
        driver.quit();
    }

    private static void uploadTransactions() {
        LoginPageForPocketbook loginPageForPocketbook = navigateTo.loginPageForPocketBook();
        loginPageForPocketbook.login(pocketBookUserName, pocketBookPassword);
        SettingPage settingPage = navigateTo.settingPage();
        boolean uploadSuccessfulToPocketBook = settingPage.uploadNewTransactions(cleanTransactionsFolder);
        navigateTo.logoutPageForPocketBook();
        if (uploadSuccessfulToPocketBook) {
            moveNewImportsToImportedFolder();
            deleteDownloadedFiles();
        }

    }

    private static void dowloadTransactions() {

        LoginPageForBankWest loginPageForBankWest = navigateTo.loginPageForBankWest();
        loginPageForBankWest.login(bankWestUserName, bankWestPassword);

        AccountPage accountPage = new AccountPage(driver);


        accountPage.openAccount(heroAccount);

        TransactionPage transactionPage = new TransactionPage(driver);
        transactionPage.downloadTransactions();

        accountPage = navigateTo.AccountPage();
        accountPage.openAccount(creditCard);
        transactionPage.downloadTransactions();

        accountPage = navigateTo.AccountPage();
        accountPage.openAccount(spendingAccount);
        transactionPage.downloadTransactions();

        transactionPage.logOut();
    }

    private static void cleanTransactions() {
        TransactionsManager transactionsManager = new TransactionsManager();
        transactionsManager.cleanFilesIn(workingDirectory, cleanTransactionsFolder, importedTransactionsFolder);
    }

    private static void deleteDownloadedFiles() {
        TransactionsManager transactionsManager = new TransactionsManager();
        List<File> newFiles = transactionsManager.getAllFilesIn(workingDirectory);
        transactionsManager.deleteFilesInFolder(newFiles, workingDirectory);
    }

    private static void moveNewImportsToImportedFolder() {
        TransactionsManager transactionsManager = new TransactionsManager();
        transactionsManager.moveAndMergeTransactionFiles(cleanTransactionsFolder, importedTransactionsFolder);
        TransactionList mergedTransactionList = transactionsManager.getContentOfFiles(transactionsManager.getAllFilesIn(importedTransactionsFolder));

        TransactionList mergedListWithTransactionsYoungerThan90Days = transactionsManager.removeOutdatedTransactionsFrom(mergedTransactionList, 90);
        transactionsManager.saveTransactionInFiles(mergedListWithTransactionsYoungerThan90Days, importedTransactionsFolder);
        transactionsManager.deleteFilesInFolder(transactionsManager.getAllFilesIn(cleanTransactionsFolder), cleanTransactionsFolder);
    }

    private static Properties getProperties() {
        Properties properties = new Properties();

        ClassLoader classLoader = Main.class.getClassLoader();


        String propertiesFile = "resources/config.properties";

        InputStream input = Main.class.getResourceAsStream(propertiesFile);

        try {
            try {
                if (input == null) {
                    System.out.println("Sorry, unable to find " + propertiesFile);
                    throw new IOException();
                }

                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;

    }
}

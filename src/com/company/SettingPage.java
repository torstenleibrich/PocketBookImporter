package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

public class SettingPage extends PageAction {
    public SettingPage(WebDriver driver) {
        super(driver);
    }

    public boolean uploadNewTransactions(String cleanTransactionsFolder) {
        openImportModal();
        selectFilesToUpload(cleanTransactionsFolder);
        selectImport();
        upload();

        return checkForSuccessMessage();
    }

    private boolean checkForSuccessMessage() {
        String result = findByXPath("//*[@id=\"uploadmsg\"]").getText();
        System.out.println("File uploaded to Pocketbook? "+result);
        if (result.contains("Success")) {
            return true;
        } else
            return false;
    }

    private void upload() {
        findById("uploadit").click();
    }

    private void selectImport() {
        selectByValue("647726", "//*[@id=\"accountId\"]");
    }

    private void selectFilesToUpload(String cleanTransactionsFolder) {
        TransactionsManager transactionsManager = new TransactionsManager();
        List<File> filesToBeUploaded = transactionsManager.getAllFilesIn(cleanTransactionsFolder);

        WebElement fileInput = findByXPath("//*[@id=\"uploadifive-file_upload\"]/input[2]");

        for (File file : filesToBeUploaded) {
            fileInput.sendKeys(file.getPath());
        }
    }

    private void openImportModal() {
        findByPartialLinkText("Import Transactions").click();
        if (findByXPath("//*[@id=\"importmodal\"]/div[1]/h3").getText().equals("Import")) {
            System.out.println("Import Modal opened...");
        } else {
            System.out.println("Import Modal not found");
        }
    }
}

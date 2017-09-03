package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by tleibri on 18/06/16.
 */
public class UploadPageForPocketSmith extends PageAction {
    public UploadPageForPocketSmith(WebDriver driver) {
        super(driver);
    }

    public boolean uploadNewTransactions(String cleanTransactionsFolder) {
        inputFilesToUpload(cleanTransactionsFolder);
        selectAccountName();
        upload();
        return checkForSuccessMessage();
    }

    private boolean checkForSuccessMessage() {
        WebElement statusProgress;

        try {
            for (int i = 0; i < 30; i++) {
                statusProgress = findByXPath("//h1[@class='title']");
                System.out.println("Progress Bar visible. I see ... " + statusProgress.getText());
                if (!statusProgress.isDisplayed()) {
                    i = 10;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Upload completed.");
        }

        WebElement summary = findByXPath("//div[@class='float-left summary']");
        String result = summary.getText();
        System.out.println("File uploaded to PocketSmith? " + result);
        if (result.contains("Sorry")) {
            return false;
        } else
            return true;
    }

    private void upload() {
        findByXPath("//a[@class='confirmation-button green_button']").click();
    }

    private void selectAccountName() {
        WebElement accountChoiceInputElement = findByXPath("//div[@id='s2id_autogen2']/input");
        accountChoiceInputElement.sendKeys("Manual");
        findByXPath("//div[@class='select2-result-label']").click();
    }

    private void inputFilesToUpload(String cleanTransactionsFolder) {
        WebElement uploadFileElement = findById("file");
        uploadFiles(cleanTransactionsFolder, uploadFileElement);
        String uploadCompleted = findByXPath("//div[@class='account_header']/h1[@class='title']").getText();
        if (!uploadCompleted.contains(".qif")) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

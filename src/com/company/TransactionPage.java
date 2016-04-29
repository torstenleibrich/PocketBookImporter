package com.company;

import org.openqa.selenium.WebDriver;

/**
 * Created by tleibri on 17/10/15.
 */
public class TransactionPage extends PageAction {
    public TransactionPage(WebDriver driver) {
        super(driver);
    }

    public void downloadTransactions() {
        selectDuration();
        selectExportType();
        exportFile();
    }

    private void exportFile() {
        findByXPath("//*[@id=\"_ctl0_ContentButtonsRight_btnExport\"]").click();
        giveDownloadToFinish(1000);
    }

    private void giveDownloadToFinish(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void selectExportType() {
        selectByValue("QIF2002", "//*[@id=\"_ctl0_ContentButtonsRight_ddlExportType\"]");
    }

    private void selectDuration() {
        selectByValue("90", "//*[@id=\"_ctl0_ContentButtonsRight_ddlExportPeriod\"]");
    }
}

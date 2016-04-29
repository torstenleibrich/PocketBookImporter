package com.company;

import org.openqa.selenium.WebDriver;

public class LoginPageForBankWest extends PageAction {

    public LoginPageForBankWest(WebDriver driver) {
        super(driver);
    }

    public void login(String bankWestUserName, String bankWestPassword) {
        printCurrentUrl();
        enterPan(bankWestUserName);
        enterPsw(bankWestPassword);
        submit();
        printCurrentUrl();
    }

    private void submit() {
        findById("AuthUC_btnLogin").click();
    }

    private void enterPsw(String bankWestPassword) {
        findById("AuthUC_txtData").sendKeys(bankWestPassword);
    }

    private void enterPan(String bankWestUserName) {
        findById("AuthUC_txtUserID").sendKeys(bankWestUserName);
    }

}

package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AccountPage extends PageAction {

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public void openAccount(String accountNumber) {
        WebElement element = findByXPath("//a[contains(text(), '" + accountNumber + "')]");
        element.click();
        printCurrentUrl();
    }
}

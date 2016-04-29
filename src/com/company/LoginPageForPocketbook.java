package com.company;

import org.openqa.selenium.WebDriver;

/**
 * Created by tleibri on 2/11/15.
 */
public class LoginPageForPocketbook extends PageAction{
    public LoginPageForPocketbook(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        printCurrentUrl();
        enterUserName(username);
        enterPsw(password);
        submit();
        printCurrentUrl();
    }

    private void submit() {
        findByXPath("//*[@id=\"signInBean\"]/fieldset/div[3]/button").click();
    }

    private void enterPsw(String password) {
        findById("password").sendKeys(password);
    }

    private void enterUserName(String username) {
        findById("username").sendKeys(username);
    }

}

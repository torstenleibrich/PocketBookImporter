package com.company;

import org.openqa.selenium.WebDriver;

/**
 * Created by tleibri on 2/11/15.
 */
public class LoginPageForPocketSmith extends PageAction{
    public LoginPageForPocketSmith(WebDriver driver) {
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
        findById("user_session_submit").click();
    }

    private void enterPsw(String password) {
        findById("user_session_password").sendKeys(password);
    }

    private void enterUserName(String username) {
        findById("user_session_login").sendKeys(username);
    }

}

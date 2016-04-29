package com.company;

import org.openqa.selenium.WebDriver;

/**
 * Created by tleibri on 15/10/15.
 */
public class Navigation {

    private final WebDriver driver;

    public Navigation(WebDriver driver) {
        this.driver = driver;
    }

    protected void goTo(String url) {
        driver.get(url);
        String title = driver.getTitle();
        System.out.print(title);
    }

    protected void printCurrentUrl() {
        String url = driver.getCurrentUrl();
        System.out.println(url);
    }

    public LoginPageForBankWest loginPageForBankWest() {
        String bankWestLogin = "https://ibs.bankwest.com.au/BWLogin/rib.aspx";
        goTo(bankWestLogin);
        return new LoginPageForBankWest(driver);
    }

    public AccountPage AccountPage() {
        goTo("https://ibs.bankwest.com.au/CMWeb/AccountInformation/AI/Balances.aspx");
        return new AccountPage(driver);
    }

    public LoginPageForPocketbook loginPageForPocketBook() {
        goTo("https://getpocketbook.com/signin");
        return new LoginPageForPocketbook(driver);
    }

    public SettingPage settingPage() {
        goTo("https://getpocketbook.com/settings");
        return new SettingPage(driver);
    }
}

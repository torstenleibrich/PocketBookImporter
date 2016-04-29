package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class Driver {

    public WebDriver getDriver(String workingDirectory) {
        FirefoxProfile fxProfile = new FirefoxProfile();

        fxProfile.setPreference("browser.download.folderList",2);
        fxProfile.setPreference("browser.download.manager.showWhenStarting",false);
        fxProfile.setPreference("browser.download.dir",workingDirectory);
        fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/qif");

        return new FirefoxDriver(fxProfile);
    }
}

package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PageAction extends Navigation {
    WebDriver driver;

    public PageAction(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    protected WebElement findById(String id) {
        WebElement element = driver.findElement(By.id(id));
        return element;
    }

    protected WebElement findByXPath(String xPath) {
        WebElement element = driver.findElement(By.xpath(xPath));
        return element;
    }

    protected WebElement findByPartialLinkText(String link) {
        WebElement element = driver.findElement(By.partialLinkText(link));
        return element;
    }


    protected Select selectByValue(String value, String xPathLocation) {
        WebElement webElement = findByXPath(xPathLocation);
        Select select = new Select(webElement);
        select.selectByValue(value);
        return select;
    }

}

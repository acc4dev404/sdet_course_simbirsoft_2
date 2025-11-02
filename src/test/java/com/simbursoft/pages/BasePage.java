package com.simbursoft.pages;

import com.simbursoft.utilities.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    WebDriver driver;
    WaitHelper waiter;

    public BasePage(WebDriver driver, WaitHelper waiter) {
        this.driver = driver;
        this.waiter = waiter;
        PageFactory.initElements(driver, this);
    }
}
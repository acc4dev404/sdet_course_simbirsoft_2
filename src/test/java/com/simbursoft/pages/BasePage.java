package com.simbursoft.pages;

import com.simbursoft.utilities.WaitHelper;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    WebDriver driver;
    WaitHelper waiter;

    public BasePage(WebDriver driver, WaitHelper waiter) {
        this.driver = driver;
        this.waiter = waiter;
    }
}
package com.simbursoft.utilities;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WaitHelper {

    WebDriverWait wait;

    public WaitHelper(WebDriverWait wait) {
        this.wait = wait;
    }

    public void untilToBeSelected(WebElement element) {
        wait.until(ExpectedConditions.elementToBeSelected(element));
    }

    public void untilClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void untilDisplayed(WebElement element) {
        wait.until(driver -> element.isDisplayed());
    }

    public void untilAlertIsPresent() {
        wait.until(ExpectedConditions.alertIsPresent());
    }

}
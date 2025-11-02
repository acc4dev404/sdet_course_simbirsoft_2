package com.simbirsoft.utilities;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Вспомогательный класс для работы с ожиданиями в Selenium.
 * Обертка над WebDriverWait для упрощения использования ожиданий.
 */
public final class WaitHelper {

    /** Экземпляр WebDriverWait для управления ожиданиями */
    private final WebDriverWait wait;

    /**
     * Конструктор с инициализацией WebDriverWait.
     *
     * @param wait экземпляр WebDriverWait
     */
    public WaitHelper(WebDriverWait wait) {
        this.wait = wait;
    }

    /**
     * Ожидает, пока элемент станет выбранным (selected).
     *
     * @param element веб-элемент для ожидания
     */
    public void untilToBeSelected(WebElement element) {
        wait.until(ExpectedConditions.elementToBeSelected(element));
    }

    /**
     * Ожидает, пока элемент станет кликабельным.
     *
     * @param element веб-элемент для ожидания
     */
    public void untilClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидает, пока элемент станет отображаемым на странице.
     *
     * @param element веб-элемент для ожидания
     */
    public void untilDisplayed(WebElement element) {
        wait.until(driver -> element.isDisplayed());
    }

    /**
     * Ожидает появления alert на странице.
     */
    public void untilAlertIsPresent() {
        wait.until(ExpectedConditions.alertIsPresent());
    }
}
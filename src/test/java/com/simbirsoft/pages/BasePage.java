package com.simbirsoft.pages;

import com.simbirsoft.utilities.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Абстрактный базовый класс для всех Page Object классов.
 * Содержит общую логику инициализации элементов страницы.
 * Предоставляет общий функционал для всех страниц приложения.
 */
public abstract class BasePage {

    /** Драйвер для управления браузером */
    WebDriver driver;

    /** Помощник для работы с ожиданиями */
    WaitHelper waiter;

    /**
     * Конструктор с инициализацией драйвера и помощника ожиданий.
     * Выполняет инициализацию элементов страницы через PageFactory.
     *
     * @param driver драйвер браузера
     * @param waiter помощник для работы с ожиданиями
     */
    public BasePage(WebDriver driver, WaitHelper waiter) {
        this.driver = driver;
        this.waiter = waiter;
        PageFactory.initElements(driver, this);
    }
}
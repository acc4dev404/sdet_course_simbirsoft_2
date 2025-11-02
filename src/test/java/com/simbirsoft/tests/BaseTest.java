package com.simbirsoft.tests;

import com.simbirsoft.utilities.ParameterProvider;
import com.simbirsoft.utilities.WaitHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовый класс для всех тестов.
 * Содержит общую логику настройки и завершения тестов.
 */
public abstract class BaseTest {

    /** Драйвер для управления браузером */
    protected WebDriver driver;

    /** Ожидание для синхронизации тестов */
    protected WebDriverWait webDriverWait;

    /** Вспомогательный класс для работы с ожиданиями */
    protected WaitHelper waiter;

    /**
     * Настройка тестового окружения перед каждым тестом.
     * Инициализирует драйвер, настраивает браузер и открывает базовый URL.
     */
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if ("true".equals(ParameterProvider.get("headless"))) {
            options.addArguments("--headless");
        }
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ParameterProvider.get("explicit.wait.time"))));
        waiter = new WaitHelper(webDriverWait);
        driver.get(ParameterProvider.get("base.url"));
        Allure.addAttachment("Browser", "Chrome");
        Allure.addAttachment("Base URL", ParameterProvider.get("base.url"));
    }

    /**
     * Завершение теста и освобождение ресурсов.
     * Закрывает браузер после выполнения теста.
     */
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
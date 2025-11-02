package com.simbirsoft.pages;

import com.simbirsoft.utilities.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы добавления клиента банковского приложения.
 * Содержит методы для взаимодействия с элементами страницы добавления клиента.
 * Предоставляет функциональность для создания новых клиентов и управления ими.
 */
public class BankManagerAddCustomerPage extends BasePage {

    /** Кнопка добавления клиента */
    @FindBy(xpath = "//button[@ng-click='addCust()']")
    private WebElement addCustomerButton;

    /** Поле ввода имени */
    @FindBy(xpath = "//input[@ng-model='fName']")
    private WebElement firstNameInput;

    /** Поле ввода фамилии */
    @FindBy(xpath = "//input[@ng-model='lName']")
    private WebElement lastNameInput;

    /** Поле ввода почтового кода */
    @FindBy(xpath = "//input[@ng-model='postCd']")
    private WebElement postCodeInput;

    /** Кнопка отправки формы */
    @FindBy(xpath = "//button[@type='submit'][contains(text(), 'Add Customer')]")
    private WebElement submitButton;

    /** Кнопка перехода к списку клиентов */
    @FindBy(xpath = "//button[@ng-click='showCust()']")
    private WebElement customersButton;

    /** Таблица клиентов */
    @FindBy(css = "div.ng-scope table.table")
    private WebElement customerTable;

    /** Строки таблицы клиентов */
    @FindBy(css = "div.ng-scope table.table tbody tr")
    private List<WebElement> customerRows;

    /**
     * Конструктор с инициализацией драйвера и помощника ожиданий.
     *
     * @param driver драйвер браузера
     * @param waiter помощник для работы с ожиданиями
     */
    public BankManagerAddCustomerPage(WebDriver driver, WaitHelper waiter) {
        super(driver, waiter);
    }

    /**
     * Вводит имя в соответствующее поле формы.
     *
     * @param firstName имя для ввода
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Ввод имени: {firstName}")
    public BankManagerAddCustomerPage enterFirstName(String firstName) {
        waiter.untilDisplayed(firstNameInput);
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    /**
     * Вводит фамилию в соответствующее поле формы.
     *
     * @param lastName фамилия для ввода
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Ввод фамилии: {lastName}")
    public BankManagerAddCustomerPage enterLastName(String lastName) {
        waiter.untilDisplayed(lastNameInput);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    /**
     * Вводит почтовый код в соответствующее поле формы.
     *
     * @param postCode почтовый код для ввода
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Ввод post-кода: {postCode}")
    public BankManagerAddCustomerPage enterPostCode(String postCode) {
        waiter.untilDisplayed(postCodeInput);
        postCodeInput.clear();
        postCodeInput.sendKeys(postCode);
        return this;
    }

    /**
     * Нажимает кнопку 'Add Customer' для начала процесса добавления клиента.
     *
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Нажатие кнопки 'Add Customer'")
    public BankManagerAddCustomerPage clickAddCustomer() {
        waiter.untilDisplayed(addCustomerButton);
        addCustomerButton.click();
        return this;
    }

    /**
     * Отправляет форму создания клиента.
     *
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Отправка формы создания клиента")
    public BankManagerAddCustomerPage submitCustomer() {
        waiter.untilClickable(submitButton);
        submitButton.click();
        return this;
    }

    /**
     * Получает текст из alert окна и закрывает его.
     *
     * @return текст сообщения из alert
     */
    @Step("Получение сообщение из Alert")
    public String getAlertText() {
        waiter.untilAlertIsPresent();
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }

    /**
     * Нажимает кнопку 'Customers' для перехода к списку клиентов.
     *
     * @return текущий экземпляр BankManagerAddCustomerPage
     */
    @Step("Нажатие кнопки 'Customers'")
    public BankManagerAddCustomerPage clickCustomers() {
        waiter.untilClickable(customersButton);
        customersButton.click();
        return this;
    }

    /**
     * Проверяет наличие клиента в списке по имени.
     *
     * @param customerName имя клиента для проверки
     * @return true если клиент присутствует в таблице, иначе false
     */
    @Step("Проверка наличие клиента в списке")
    public boolean isCustomerPresent(String customerName) {
        return getAllCustomerNames().contains(customerName);
    }

    /**
     * Получает список всех имен клиентов из таблицы.
     *
     * @return список имен клиентов
     */
    @Step("Получение списка клиентов")
    public List<String> getAllCustomerNames() {
        waiter.untilDisplayed(customerTable);
        if (customerRows.isEmpty()) {
            return List.of();
        }
        waiter.untilDisplayed(customerRows.get(0));
        return customerRows.stream()
                .map(row -> {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    return cells.size() > 0 ? cells.get(0).getText() : "";
                })
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toList());
    }

}

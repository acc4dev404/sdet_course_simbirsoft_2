package com.simbursoft.pages;

import com.simbursoft.utilities.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class BankManagerAddCustomerPage extends BasePage {

    @FindBy(xpath = "//button[@ng-click='addCust()']")
    private WebElement addCustomerButton;

    @FindBy(xpath = "//input[@ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@ng-model='postCd']")
    private WebElement postCodeInput;

    @FindBy(xpath = "//button[@type='submit'][contains(text(), 'Add Customer')]")
    private WebElement submitButton;

    @FindBy(xpath = "//button[@ng-click='showCust()']")
    private WebElement customersButton;

    @FindBy(css = "div.ng-scope table.table")
    private WebElement customerTable;

    @FindBy(css = "div.ng-scope table.table tbody tr")
    private List<WebElement> customerRows;

    public BankManagerAddCustomerPage(WebDriver driver, WaitHelper waiter) {
        super(driver, waiter);
    }

    @Step("Ввод имени: {firstName}")
    public BankManagerAddCustomerPage enterFirstName(String firstName) {
        waiter.untilDisplayed(firstNameInput);
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    @Step("Ввод фамилии: {lastName}")
    public BankManagerAddCustomerPage enterLastName(String lastName) {
        waiter.untilDisplayed(lastNameInput);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    @Step("Ввод post-кода: {postCode}")
    public BankManagerAddCustomerPage enterPostCode(String postCode) {
        waiter.untilDisplayed(postCodeInput);
        postCodeInput.clear();
        postCodeInput.sendKeys(postCode);
        return this;
    }

    @Step("Нажатие кнопки 'Add Customer'")
    public BankManagerAddCustomerPage clickAddCustomer() {
        waiter.untilDisplayed(addCustomerButton);
        addCustomerButton.click();
        return this;
    }

    @Step("Отправка формы создания клиента")
    public BankManagerAddCustomerPage submitCustomer() {
        waiter.untilClickable(submitButton);
        submitButton.click();
        return this;
    }

    @Step("Получение сообщение из Alert")
    public String getAlertText() {
        waiter.untilAlertIsPresent();
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }

    @Step("Нажатие кнопки 'Customers'")
    public BankManagerAddCustomerPage clickCustomers() {
        waiter.untilClickable(customersButton);
        customersButton.click();
        return this;
    }

    @Step("Проверка наличие клиента в списке")
    public boolean isCustomerPresent(String customerName) {
        return getAllCustomerNames().contains(customerName);
    }

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

package com.simbursoft.pages;

import com.simbursoft.utilities.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class BankManagerCustomersPage extends BasePage {

    @FindBy(xpath = "//button[@ng-click='showCust()']")
    private WebElement customersButton;

    @FindBy(css = "button[ng-click*='deleteCust']")
    private List<WebElement> deleteButtons;

    @FindBy(css = "div.ng-scope table.table tbody tr")
    private List<WebElement> customerRows;

    @FindBy(css = "div.ng-scope table.table")
    private WebElement customerTable;

    @FindBy(css = "div.ng-scope table.table thead tr a")
    private List<WebElement> tableHeaders;

    public BankManagerCustomersPage(WebDriver driver, WaitHelper waiter) {
        super(driver, waiter);
    }

    @Step("Нажатие кнопки 'Customers'")
    public BankManagerCustomersPage clickCustomers() {
        waiter.untilClickable(customersButton);
        customersButton.click();
        return this;
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

    @Step("Получение количества клиентов")
    public int getCustomerCount() {
        return customerRows.size();
    }

    @Step("Сортировка по имени клиента")
    public void sortByFirstName() {
        waiter.untilDisplayed(customerTable);
        waiter.untilClickable(tableHeaders.get(0));
        tableHeaders.get(0).click();
        tableHeaders.get(0).click();
    }

    @Step("Удаление клиента: {customerName}")
    public void deleteCustomer(String customerName) {
        List<String> names = getAllCustomerNames();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(customerName) && i < deleteButtons.size() ) {
                waiter.untilClickable(deleteButtons.get(i));
                deleteButtons.get(i).click();
                break;
            }
        }
    }

    @Step("Проверка наличие клиента в списке")
    public boolean isCustomerPresent(String customerName) {
        return getAllCustomerNames().contains(customerName);
    }
}

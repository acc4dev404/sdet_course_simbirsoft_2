package com.simbirsoft.pages;

import com.simbirsoft.utilities.WaitHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы управления клиентами банковского приложения.
 * Содержит методы для взаимодействия с элементами страницы списка клиентов.
 * Предоставляет функциональность для просмотра, сортировки и удаления клиентов.
 */
public class BankManagerCustomersPage extends BasePage {

    /** Кнопка перехода к списку клиентов */
    @FindBy(xpath = "//button[@ng-click='showCust()']")
    private WebElement customersButton;

    /** Список кнопок удаления клиентов */
    @FindBy(css = "button[ng-click*='deleteCust']")
    private List<WebElement> deleteButtons;

    /** Строки таблицы клиентов */
    @FindBy(css = "div.ng-scope table.table tbody tr")
    private List<WebElement> customerRows;

    /** Таблица клиентов */
    @FindBy(css = "div.ng-scope table.table")
    private WebElement customerTable;

    /** Заголовки таблицы для сортировки */
    @FindBy(css = "div.ng-scope table.table thead tr a")
    private List<WebElement> tableHeaders;

    /**
     * Конструктор с инициализацией драйвера и помощника ожиданий.
     *
     * @param driver драйвер браузера
     * @param waiter помощник для работы с ожиданиями
     */
    public BankManagerCustomersPage(WebDriver driver, WaitHelper waiter) {
        super(driver, waiter);
    }

    /**
     * Нажимает кнопку 'Customers' для перехода к списку клиентов.
     *
     * @return текущий экземпляр BankManagerCustomersPage
     */
    @Step("Нажатие кнопки 'Customers'")
    public BankManagerCustomersPage clickCustomers() {
        waiter.untilClickable(customersButton);
        customersButton.click();
        return this;
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

    /**
     * Получает текущее количество клиентов в таблице.
     *
     * @return количество клиентов
     */
    @Step("Получение количества клиентов")
    public int getCustomerCount() {
        return customerRows.size();
    }

    /**
     * Выполняет сортировку клиентов по имени.
     */
    @Step("Сортировка по имени клиента")
    public BankManagerCustomersPage clickSortByFirstName() {
        waiter.untilDisplayed(customerTable);
        waiter.untilClickable(tableHeaders.get(0));
        tableHeaders.get(0).click();
        return this;
    }

    /**
     * Удаляет клиента по имени из таблицы.
     *
     * @param customerName имя клиента для удаления
     */
    @Step("Удаление клиента: [{customerName}]")
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
}
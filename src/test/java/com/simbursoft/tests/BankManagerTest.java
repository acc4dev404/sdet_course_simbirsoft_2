package com.simbursoft.tests;

import com.simbursoft.pages.BankManagerAddCustomerPage;
import com.simbursoft.pages.BankManagerCustomersPage;
import com.simbursoft.utilities.CustomerHelper;
import com.simbursoft.utilities.NameGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование функциональности XYZ Bank")
@Epic("Операции по управлению клиентами")
@Feature("Управление клиентами")
@Execution(ExecutionMode.CONCURRENT)
public class BankManagerTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание клиента с валидными данными")
    @Description("Тест проверяет создание клиента с сгенерированными данными согласно требованиям задания")
    @Story("Пользователь может создать нового клиента с валидными данными")
    void createCustomerWithValidData() {
        String postCode = NameGenerator.generateRandomPostCode();
        String firstName = NameGenerator.generateNameFromPostCode(postCode);
        String lastName = NameGenerator.generateName();
        BankManagerAddCustomerPage bankManagerAddCustomerPage = new BankManagerAddCustomerPage(driver, waiter);
        bankManagerAddCustomerPage
                .clickAddCustomer()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostCode(postCode)
                .submitCustomer();
        String alertText = bankManagerAddCustomerPage.getAlertText();
        assertThat(alertText)
                .as("Alert должен содержать сообщение об успешном создании клиента")
                .contains("Customer added successfully with customer id");
        bankManagerAddCustomerPage.clickCustomers();
        boolean isCustomerPresent = bankManagerAddCustomerPage.isCustomerPresent(firstName);
        assertThat(isCustomerPresent)
                .as("Созданный клиент должен отображаться в таблице клиентов")
                .isTrue();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Сортировка клиентов по имени")
    @Description("Тест проверяет функциональность сортировки клиентов по имени в алфавитном порядке")
    @Story("Пользователь может сортировать клиентов по имени")
    void sortCustomersByFirstName() {
        BankManagerCustomersPage bankManagerCustomersPage = new BankManagerCustomersPage(driver, waiter);
        bankManagerCustomersPage
                .clickCustomers();
        List<String> namesBeforeSort = bankManagerCustomersPage.getAllCustomerNames();
        bankManagerCustomersPage.sortByFirstName();
        List<String> namesAfterSort = bankManagerCustomersPage.getAllCustomerNames();
        List<String> expectedSortedNames = namesBeforeSort.stream()
                .sorted()
                .toList();
        assertThat(namesAfterSort)
                .as("Клиенты должны быть отсортированы по имени в алфавитном порядке")
                .isEqualTo(expectedSortedNames);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление клиента по средней длине имени")
    @Description("Тест проверяет удаление клиента, чье имя имеет длину, наиболее близкую к средней")
    @Story("Пользователь может удалить клиента по алгоритму из задания")
    void deleteCustomerByAverageNameLength() {
        BankManagerCustomersPage bankManagerCustomersPage = new BankManagerCustomersPage(driver, waiter);
        bankManagerCustomersPage
                .clickCustomers();
        List<String> customerNames = bankManagerCustomersPage.getAllCustomerNames();
        if (customerNames.isEmpty()) {
            Allure.label("testrail", "skip");
            return;
        }
        String customerToDelete = CustomerHelper.findCustomerToDelete(customerNames);
        int initialCount = bankManagerCustomersPage.getCustomerCount();
        bankManagerCustomersPage.deleteCustomer(customerToDelete);
        boolean isCustomerStillPresent = bankManagerCustomersPage.isCustomerPresent(customerToDelete);
        int finalCount = bankManagerCustomersPage.getCustomerCount();
        assertThat(isCustomerStillPresent)
                .as("Клиент '%s' должен быть удален из таблицы", customerToDelete)
                .isFalse();
        assertThat(finalCount)
                .as("Количество клиентов должно уменьшиться на 1")
                .isEqualTo(initialCount - 1);
    }

}
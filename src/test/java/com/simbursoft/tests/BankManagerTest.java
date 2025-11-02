package com.simbursoft.tests;

import com.simbursoft.pages.BankManagerPage;
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

        BankManagerPage bankManagerPage = new BankManagerPage(driver, waiter);

        bankManagerPage
                .clickAddCustomer()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostCode(postCode)
                .submitCustomer();

        String alertText = bankManagerPage.getAlertText();
        assertThat(alertText)
                .as("Alert должен содержать сообщение об успешном создании клиента")
                .contains("Customer added successfully with customer id");

        bankManagerPage.clickCustomers();
        boolean isCustomerPresent = bankManagerPage.isCustomerPresent(firstName);
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

        BankManagerPage bankManagerPage = new BankManagerPage(driver, waiter);

        bankManagerPage
                .clickCustomers();

        List<String> namesBeforeSort = bankManagerPage.getAllCustomerNames();

        bankManagerPage.sortByFirstName();

        List<String> namesAfterSort = bankManagerPage.getAllCustomerNames();
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

        BankManagerPage bankManagerPage = new BankManagerPage(driver, waiter);

        bankManagerPage
                .clickCustomers();

        List<String> customerNames = bankManagerPage.getAllCustomerNames();

        if (customerNames.isEmpty()) {
            Allure.label("testrail", "skip");
            return;
        }

        String customerToDelete = CustomerHelper.findCustomerToDelete(customerNames);

        int initialCount = bankManagerPage.getCustomerCount();

        bankManagerPage.deleteCustomer(customerToDelete);

        boolean isCustomerStillPresent = bankManagerPage.isCustomerPresent(customerToDelete);
        int finalCount = bankManagerPage.getCustomerCount();

        assertThat(isCustomerStillPresent)
                .as("Клиент '%s' должен быть удален из таблицы", customerToDelete)
                .isFalse();

        assertThat(finalCount)
                .as("Количество клиентов должно уменьшиться на 1")
                .isEqualTo(initialCount - 1);
    }

}
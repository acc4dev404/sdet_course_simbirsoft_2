package com.simbirsoft.tests;

import com.simbirsoft.pages.BankManagerAddCustomerPage;
import com.simbirsoft.pages.BankManagerCustomersPage;
import com.simbirsoft.utilities.CustomerHelper;
import com.simbirsoft.utilities.NameGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестовый класс для проверки функциональности управления клиентами в банковском приложении.
 * Содержит тест-кейсы для создания, сортировки и удаления клиентов.
 */
@DisplayName("Тестирование функциональности XYZ Bank")
@Epic("Операции по управлению клиентами")
@Feature("Управление клиентами")
@Execution(ExecutionMode.CONCURRENT)
public class BankManagerTest extends BaseTest {

    /**
     * Тест создания клиента с валидными данными.
     * Проверяет успешное создание клиента и его отображение в списке.
     */
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

    /**
     * Тест сортировки клиентов по имени.
     * Проверяет корректность алфавитной сортировки клиентов.
     */
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

    /**
     * Тест удаления клиента по средней длине имени.
     * Если несколько клиентов имеют одинаковую близость к средней длине, удаляет всех подходящих.
     */
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление клиента по средней длине имени")
    @Description("Тест проверяет удаление клиента, чье имя имеет длину, наиболее близкую к средней. " +
            "Если таких клиентов несколько, удаляются все подходящие.")
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
        List<String> customersToDelete = CustomerHelper.findCustomersToDelete(customerNames);
        int initialCount = bankManagerCustomersPage.getCustomerCount();
        for (String customerToDelete : customersToDelete) {
            bankManagerCustomersPage.deleteCustomer(customerToDelete);
        }
        for (String customerToDelete : customersToDelete) {
            boolean isCustomerStillPresent = bankManagerCustomersPage.isCustomerPresent(customerToDelete);
            assertThat(isCustomerStillPresent)
                    .as("Клиент '%s' должен быть удален из таблицы", customerToDelete)
                    .isFalse();
        }
        int finalCount = bankManagerCustomersPage.getCustomerCount();
        assertThat(finalCount)
                .as("Количество клиентов должно уменьшиться на " + customersToDelete.size())
                .isEqualTo(initialCount - customersToDelete.size());
    }
}
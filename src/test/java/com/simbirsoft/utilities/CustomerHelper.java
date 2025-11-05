package com.simbirsoft.utilities;

import java.util.List;

/**
 * Вспомогательный класс для работы с клиентами.
 * Предоставляет методы для анализа и обработки данных клиентов.
 */
public final class CustomerHelper {
    private CustomerHelper() {}

    /**
     * Находит клиента для удаления на основе средней длины имени.
     * Если несколько клиентов имеют одинаковую близость к средней длине, возвращает список всех подходящих.
     *
     * @param customerNames список имен клиентов
     * @return список имен клиентов, наиболее близких к средней длине
     * @throws IllegalArgumentException если список клиентов пуст
     */
    public static List<String> findCustomersToDelete(List<String> customerNames) {
        validateCustomerList(customerNames);
        double averageLength = calculateAverageNameLength(customerNames);
        double minDifference = findMinDifferenceFromAverage(customerNames, averageLength);
        return filterCustomersByDifference(customerNames, averageLength, minDifference);
    }

    /**
     * Находит одного клиента для удаления.
     * Если несколько клиентов подходят, возвращает первого из списка.
     *
     * @param customerNames список имен клиентов
     * @return имя клиента для удаления
     */
    public static String findCustomerToDelete(List<String> customerNames) {
        List<String> candidates = findCustomersToDelete(customerNames);
        return candidates.get(0);
    }

    /**
     * Проверяет валидность списка клиентов.
     *
     * @param customerNames список имен клиентов для проверки
     * @throws AssertionError если список клиентов пуст
     */
    private static void validateCustomerList(List<String> customerNames) {
        if (customerNames.isEmpty()) {
            throw new AssertionError("Список клиентов не может быть пустым: ожидается хотя бы один клиент для анализа");
        }
    }

    /**
     * Вычисляет среднюю длину имен клиентов.
     *
     * @param customerNames список имен клиентов
     * @return средняя длина имени
     */
    private static double calculateAverageNameLength(List<String> customerNames) {
        return customerNames.stream()
                .mapToInt(String::length)
                .average()
                .getAsDouble();
    }

    /**
     * Находит минимальное отклонение длины имени от средней длины.
     *
     * @param customerNames список имен клиентов
     * @param averageLength средняя длина имени
     * @return минимальное отклонение от средней длины
     */
    private static double findMinDifferenceFromAverage(List<String> customerNames, double averageLength) {
        return customerNames.stream()
                .mapToDouble(name -> calculateDifference(name.length(), averageLength))
                .min()
                .orElse(0.0);
    }

    /**
     * Вычисляет абсолютное отклонение значения от среднего.
     *
     * @param value значение для сравнения
     * @param average среднее значение
     * @return абсолютное отклонение
     */
    private static double calculateDifference(int value, double average) {
        return Math.abs(value - average);
    }

    /**
     * Фильтрует клиентов по минимальному отклонению от средней длины.
     *
     * @param customerNames список имен клиентов
     * @param averageLength средняя длина имени
     * @param minDifference минимальное отклонение от средней длины
     * @return список клиентов с минимальным отклонением
     */
    private static List<String> filterCustomersByDifference(List<String> customerNames, double averageLength, double minDifference) {
        return customerNames.stream()
                .filter(name -> calculateDifference(name.length(), averageLength) == minDifference)
                .toList();
    }
}
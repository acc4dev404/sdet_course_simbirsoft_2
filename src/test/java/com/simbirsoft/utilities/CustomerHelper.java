package com.simbirsoft.utilities;

import java.util.List;
import java.util.OptionalDouble;

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
        if (customerNames.isEmpty()) {
            throw new IllegalArgumentException("Список клиентов не может быть пустой");
        }
        // Вычисляем среднюю длину имен
        OptionalDouble averageOpt = customerNames.stream()
                .mapToInt(String::length)
                .average();
        double averageLength = averageOpt.getAsDouble();
        // Находим минимальное отклонение от средней длины
        double minDifference = customerNames.stream()
                .mapToDouble(name -> Math.abs(name.length() - averageLength))
                .min()
                .orElse(0.0);
        // Возвращаем всех клиентов с минимальным отклонением
        return customerNames.stream()
                .filter(name -> Math.abs(name.length() - averageLength) == minDifference)
                .toList();
    }

    /**
     * Находит одного клиента для удаления (оригинальная логика для обратной совместимости).
     * Если несколько клиентов подходят, возвращает первого из списка.
     *
     * @param customerNames список имен клиентов
     * @return имя клиента для удаления
     * @throws IllegalArgumentException если список клиентов пуст
     */
    public static String findCustomerToDelete(List<String> customerNames) {
        List<String> candidates = findCustomersToDelete(customerNames);
        return candidates.get(0);
    }
}
package com.simbirsoft.utilities;

import java.util.Random;

/**
 * Генератор случайных имен и почтовых кодов для тестовых данных.
 * Предоставляет методы для создания уникальных тестовых данных.
 */
public final class NameGenerator {

    private NameGenerator() {}

    /**
     * Генерирует случайное имя длиной 5 символов из букв и цифр.
     *
     * @return сгенерированное имя
     */
    public static String generateName() {
        int length = 5;
        Random random = new Random();
        return random.ints('0', 'z' + 1)
                .filter(i -> Character.isLetterOrDigit(i))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Генерирует имя на основе почтового кода.
     * Преобразует пары цифр из почтового кода в буквы.
     *
     * @param postCode почтовый код для генерации имени
     * @return сгенерированное имя
     */
    public static String generateNameFromPostCode(String postCode) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < postCode.length(); i += 2) {
            if (i + 2 <= postCode.length()) {
                String twoDigits = postCode.substring(i, i + 2);
                int number = Integer.parseInt(twoDigits);
                char letter = (char) ('a' + (number % 26));
                name.append(letter);
            }
        }
        return name.toString();
    }

    /**
     * Генерирует случайный 10-значный почтовый код.
     *
     * @return сгенерированный почтовый код
     */
    public static String generateRandomPostCode() {
        StringBuilder postCode = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            postCode.append((int) (Math.random() * 10));
        }
        return postCode.toString();
    }
}
package com.simbursoft.utilities;

import java.util.List;
import java.util.OptionalDouble;

public final class CustomerHelper {

    private CustomerHelper() {}

    public static String findCustomerToDelete(List<String> customerNames) {
        if (customerNames.isEmpty()) {
            throw new IllegalArgumentException("Список клиентов не может быть пустой");
        }
        OptionalDouble averageOpt = customerNames.stream()
                .mapToInt(String::length)
                .average();
        double averageLength = averageOpt.getAsDouble();
        return customerNames.stream()
                .min((name1, name2) -> {
                    double diff1 = Math.abs(name1.length() - averageLength);
                    double diff2 = Math.abs(name2.length() - averageLength);
                    return Double.compare(diff1, diff2);
                })
                .orElse(customerNames.get(0));
    }
}
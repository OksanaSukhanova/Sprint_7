package ru.yandex.practicum.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {
    public static Order getRandom() {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String metroStation = RandomStringUtils.randomAlphabetic(10);
        String phone = RandomStringUtils.randomAlphabetic(10);
        int rentTime = new Random().nextInt();
        String deliveryDate = RandomStringUtils.randomNumeric(4);
        String comment = RandomStringUtils.randomAlphabetic(10);
        List<Color> colors = new ArrayList<>();
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);
    }
}

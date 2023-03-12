package ru.yandex.practicum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.client.base.ScooterRestClient;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {
    private static final String ORDER_URI = ScooterRestClient.BASE_URI + "/orders";

    @Step("Create order {order}")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when().post(ORDER_URI).then();
    }

    @Step("Get all orders list")
    public ValidatableResponse getAll() {
        return given()
                .spec(getBaseReqSpec())
                .when().get(ORDER_URI).then();
    }

    @Step("Cancel order")
    public ValidatableResponse cancel(int track) {
        return given()
                .spec(getBaseReqSpec())
                .body(track)
                .when().put(ORDER_URI + "/cancel").then();
    }
}

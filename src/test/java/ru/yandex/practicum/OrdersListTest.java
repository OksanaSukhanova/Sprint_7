package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OrdersListTest {
    OrderClient orderClient;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Receiving all orders list")
    @Description("Check that orders list request without parameters returns all orders list")
    public void orderListRequestWithoutParametersReturnsAllOrdersList() {
        ValidatableResponse getResponse = orderClient.getAll();

        int statusCode = getResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 200, statusCode);

        ArrayList<String> ordersList = getResponse.extract().path("orders");
        assertFalse("Orders list is empty", ordersList.isEmpty());
    }
}

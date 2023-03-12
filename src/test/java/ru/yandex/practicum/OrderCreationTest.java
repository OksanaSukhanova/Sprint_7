package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.model.Color;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderGenerator;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private OrderClient orderClient;
    private final List<Color> colors;
    private int track;

    public OrderCreationTest(List<Color> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] scooterColor() {
        return new Object[][]{
                new List[]{List.of(Color.BLACK)},
                new List[]{List.of(Color.GREY)},
                new List[]{List.of(Color.BLACK, Color.GREY)},
                new List[]{List.of()}
        };
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Order creation with valid data and different scooter colors")
    @Description("Check that order can be created with different scooter color options")
    public void createOrderWithScooterColorIsSuccessful() {
        Order order = OrderGenerator.getRandom();
        order.setColors(colors);

        ValidatableResponse createResponse = orderClient.create(order);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 201, statusCode);

        track = createResponse.extract().path("track");
        assertTrue("Track is not created ", track != 0);
    }

    @After
    public void clearData() {
        orderClient.cancel(track);
    }
}

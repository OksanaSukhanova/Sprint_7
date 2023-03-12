package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.CourierCredentials;
import ru.yandex.practicum.model.CourierGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Courier authorization with valid data")
    @Description("Check that courier can authorize")
    public void courierAuthorizationWithValidDataIsPossible() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 200, statusCode);

        assertTrue("Courier id is not created", courierId != 0);
    }

    @Test
    @DisplayName("Courier authorization without login")
    @Description("Check that courier without login can't authorize")
    public void courierAuthorizationWithoutLoginIsImpossible() {
        CourierCredentials courierCredentials = CourierCredentials.from(courier);
        courierCredentials.setLogin(null);

        ValidatableResponse loginResponse = courierClient.login(courierCredentials);

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 400, statusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Недостаточно данных для входа", message);
    }

    @Test
    @DisplayName("Courier authorization without password")
    @Description("Check that courier without password can't authorize")
    public void courierAuthorizationWithoutPasswordIsImpossible() {
        CourierCredentials courierCredentials = CourierCredentials.from(courier);
        courierCredentials.setPassword(null);

        ValidatableResponse loginResponse = courierClient.login(courierCredentials);

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 400, statusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Недостаточно данных для входа", message);
    }

    @Test
    @DisplayName("Courier authorization with invalid login")
    @Description("Check that courier with invalid login can't authorize")
    public void courierAuthorizationWithInvalidLoginIsImpossible() {
        CourierCredentials courierCredentials = CourierCredentials.from(courier);
        courierCredentials.setLogin(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse loginResponse = courierClient.login(courierCredentials);

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 404, statusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Courier authorization with invalid password")
    @Description("Check that courier with invalid password can't authorize")
    public void courierAuthorizationWithInvalidPasswordIsImpossible() {
        CourierCredentials courierCredentials = CourierCredentials.from(courier);
        courierCredentials.setPassword(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse loginResponse = courierClient.login(courierCredentials);

        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 404, statusCode);

        String message = loginResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Учетная запись не найдена", message);
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }
}
